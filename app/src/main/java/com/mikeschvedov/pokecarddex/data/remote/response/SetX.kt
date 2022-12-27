package com.mikeschvedov.pokecarddex.data.remote.response

data class SetX(
    val id: String,
    val images: ImagesXXX,
    val legalities: LegalitiesX,
    val name: String,
    val printedTotal: Int,
    val ptcgoCode: String,
    val releaseDate: String,
    val series: String,
    val total: Int,
    val updatedAt: String
)