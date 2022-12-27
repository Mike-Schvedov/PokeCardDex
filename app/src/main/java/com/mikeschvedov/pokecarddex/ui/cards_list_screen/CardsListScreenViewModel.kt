package com.mikeschvedov.pokecarddex.ui.cards_list_screen

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import coil.Coil
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.mikeschvedov.pokecarddex.data.models.PokemonCardData
import com.mikeschvedov.pokecarddex.repository.PokemonRepository
import com.mikeschvedov.pokecarddex.utils.Constants.PAGE_SIZE
import com.mikeschvedov.pokecarddex.utils.NetworkWrapper
import com.mikeschvedov.ultimate_utility_box.logger.LoggerService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardsListScreenViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    private var curPage = 1

    var pokemonList = mutableStateOf<List<PokemonCardData>>(listOf())
    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var endReached = mutableStateOf(false)
    var totalResultCount = mutableStateOf(0)

    var drawable = mutableStateOf(null)

    var queryText = mutableStateOf("")


    fun loadPokemonPaginated() {
        LoggerService.info("loading pagination - view model")
        viewModelScope.launch {
            isLoading.value = true

            val result = repository.getMatchingCardsList(
                query = "name:${queryText.value}",
                pageSize = PAGE_SIZE,
                page = curPage,
                "set.releaseDate"
            )
            when (result) {
                is NetworkWrapper.Success -> {
                    endReached.value = curPage * PAGE_SIZE >= result.data!!.totalCount

                    totalResultCount.value = result.data.totalCount

                    val cardsResults = result.data.data.mapIndexed { index, card ->
                        PokemonCardData(
                            cardId = card.id,
                            cardImage = card.images.large
                        )
                    }

                    curPage++
                    loadError.value = ""
                    isLoading.value = false
                    pokemonList.value += cardsResults
                }
                is NetworkWrapper.Error -> {
                    loadError.value = result.message!!
                    isLoading.value = false
                }
            }
        }
    }

    fun fetchColors(url: String, context: Context, onCalculated: (Color) -> Unit) {
        viewModelScope.launch {
            // Requesting the image using coil's ImageRequest
            val req = ImageRequest.Builder(context)
                .data(url)
                .allowHardware(false)
                .build()

            val result = Coil.execute(req)

            if (result is SuccessResult) {
                // Save the drawable as a state in order to use it on the composable
                // Converting it to bitmap and using it to calculate the palette
                calcDominantColor(result.drawable) { color ->
                    onCalculated(color)
                }
            }
        }
    }

    fun calcDominantColor(drawable: Drawable, onFinish: (Color) -> Unit) {
        LoggerService.info("calcDominantColor")

        val bmp = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)

        Palette.from(bmp).generate{ palette ->
            palette?.dominantSwatch?.rgb?.let { colorValue ->
                onFinish(Color(colorValue))
            }
        }
    }

    fun clearList() {
        pokemonList.value = listOf()
        curPage = 1
    }
}