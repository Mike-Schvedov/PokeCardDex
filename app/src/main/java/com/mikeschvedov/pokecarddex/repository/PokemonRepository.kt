package com.mikeschvedov.pokecarddex.repository

import com.mikeschvedov.pokecarddex.data.models.PokemonCardData
import com.mikeschvedov.pokecarddex.data.remote.PokeApi
import com.mikeschvedov.pokecarddex.data.remote.response.Pokemon
import com.mikeschvedov.pokecarddex.data.remote.response.PokemonSearchResponse
import com.mikeschvedov.pokecarddex.data.remote.response.SingleCardResponse
import com.mikeschvedov.pokecarddex.utils.NetworkWrapper
import com.mikeschvedov.ultimate_utility_box.logger.LoggerService
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class PokemonRepository @Inject constructor(
    private val api: PokeApi
) {

    suspend fun getMatchingCardsList(
        query: String,
        pageSize: Int,
        page: Int,
        orderBy: String
    ): NetworkWrapper<PokemonSearchResponse> {

        val response = try {
            api.getMatchingCardsList(
                searchQuery = query,
                pageSize = pageSize,
                page = page,
                orderBy = orderBy
            )

        } catch (exception: Exception) {
            return NetworkWrapper.Error(exception.userFriendlyExplanation())
        }

        LoggerService.info("This is the response ${response.data.toString()}")
        return NetworkWrapper.Success(response)
    }

    suspend fun getCardById(cardId: String): NetworkWrapper<SingleCardResponse> {

        val response = try {
            api.getCardById(
                cardId = cardId
            )



        } catch (exception: Exception) {
            return NetworkWrapper.Error(exception.userFriendlyExplanation())
        }
        LoggerService.info("This is the response ${response.data.images.large}")
        return NetworkWrapper.Success(response)
    }

}

fun Exception.userFriendlyExplanation() : String{
    return when(this.javaClass.simpleName){
        "UnknownHostException" -> "No Internet Connection"
        else -> "Unknown Error"
    }
}