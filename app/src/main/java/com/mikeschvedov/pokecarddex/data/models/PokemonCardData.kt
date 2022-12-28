package com.mikeschvedov.pokecarddex.data.models

import com.mikeschvedov.pokecarddex.data.remote.response.CardmarketX
import com.mikeschvedov.pokecarddex.data.remote.response.SetX

data class PokemonCardData(
    val cardImage: String,
    val cardId: String
) {
}