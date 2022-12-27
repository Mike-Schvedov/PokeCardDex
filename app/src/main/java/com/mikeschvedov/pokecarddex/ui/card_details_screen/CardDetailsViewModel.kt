package com.mikeschvedov.pokecarddex.ui.card_details_screen

import androidx.lifecycle.ViewModel
import com.mikeschvedov.pokecarddex.data.models.PokemonCardData
import com.mikeschvedov.pokecarddex.data.remote.response.Pokemon
import com.mikeschvedov.pokecarddex.data.remote.response.SingleCardResponse
import com.mikeschvedov.pokecarddex.repository.PokemonRepository
import com.mikeschvedov.pokecarddex.utils.NetworkWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CardDetailsViewModel @Inject constructor(
    private val repository: PokemonRepository
): ViewModel() {

    suspend fun getCardById(cardId: String) : NetworkWrapper<SingleCardResponse>{
        return repository.getCardById(cardId)
    }

}