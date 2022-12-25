package com.mikeschvedov.pokecarddex.data.remote.response

data class PokemonSearchResponse(
    val count: Int,
    val data: List<Pokemon>,
    val page: Int,
    val pageSize: Int,
    val totalCount: Int
)