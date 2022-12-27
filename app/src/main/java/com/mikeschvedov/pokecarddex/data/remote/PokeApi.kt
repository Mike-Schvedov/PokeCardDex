package com.mikeschvedov.pokecarddex.data.remote

import com.mikeschvedov.pokecarddex.data.remote.response.Pokemon
import com.mikeschvedov.pokecarddex.data.remote.response.PokemonSearchResponse
import com.mikeschvedov.pokecarddex.data.remote.response.SingleCardResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeApi {

    //https://api.pokemontcg.io/v2/cards?q=name:lugia
    //https://api.pokemontcg.io/v2/cards/basep-8

    @GET("/v2/cards")
    suspend fun getMatchingCardsList(
        @Query("q") searchQuery: String,
        @Query("pageSize") pageSize: Int,
        @Query("page") page: Int,
        @Query("orderBy") orderBy: String
    ): PokemonSearchResponse

    @GET("/v2/cards/{id}")
    suspend fun getCardById(
        @Path("id") cardId: String,
    ): SingleCardResponse
}