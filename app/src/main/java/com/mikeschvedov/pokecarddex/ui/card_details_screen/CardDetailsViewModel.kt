package com.mikeschvedov.pokecarddex.ui.card_details_screen

import androidx.lifecycle.ViewModel
import com.mikeschvedov.pokecarddex.data.remote.response.CardData
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


    fun calculatePriceChartingUrl(cardDetails: CardData?): String {
        //"https://www.pricecharting.com/game/pokemon-neo-genesis/lugia-9"
        val cardNumber = cardDetails?.number
        val cardName = cardDetails?.name?.lowercase()

        return "https://www.pricecharting.com/search-products?q=${cardName}+${cardNumber}+&type=prices"
    }

    fun calculateEBayUrl(cardDetails: CardData?): String {
        val cardNumber = cardDetails?.number
        val cardName = cardDetails?.name?.lowercase()
        val cardSet = cardDetails?.set?.name?.lowercase()
        val setLimit = cardDetails?.set?.total

        return "https://www.ebay.com/sch/i.html?&_nkw=${cardName}+${cardNumber}/${setLimit}+${cardSet}"
    }
}