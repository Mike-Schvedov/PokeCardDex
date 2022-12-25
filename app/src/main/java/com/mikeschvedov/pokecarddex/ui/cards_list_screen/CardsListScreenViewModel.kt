package com.mikeschvedov.pokecarddex.ui.cards_list_screen

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
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


    fun calcDominantColor(drawable: Drawable, onFinish: (Color) -> Unit) {
        val bitmap = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)

        Palette.from(bitmap).generate()
            .apply {
                dominantSwatch?.rgb?.let { colorValue ->
                    onFinish(Color(colorValue))
                }
            }
    }
}