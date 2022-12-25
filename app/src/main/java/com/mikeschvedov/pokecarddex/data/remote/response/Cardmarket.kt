package com.mikeschvedov.pokecarddex.data.remote.response

data class Cardmarket(
    val prices: Prices,
    val updatedAt: String,
    val url: String
)