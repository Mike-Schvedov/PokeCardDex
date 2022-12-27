package com.mikeschvedov.pokecarddex.data.remote.response

data class Data(
    val artist: String,
    val attacks: List<AttackX>,
    val cardmarket: CardmarketX,
    val convertedRetreatCost: Int,
    val flavorText: String,
    val hp: String,
    val id: String,
    val images: ImagesXX,
    val legalities: LegalitiesX,
    val level: String,
    val name: String,
    val nationalPokedexNumbers: List<Int>,
    val number: String,
    val rarity: String,
    val retreatCost: List<String>,
    val `set`: SetX,
    val subtypes: List<String>,
    val supertype: String,
    val tcgplayer: TcgplayerX,
    val types: List<String>,
    val weaknesses: List<WeaknesseX>
)