package com.mikeschvedov.pokecarddex.data.remote.response

data class AttackX(
    val convertedEnergyCost: Int,
    val cost: List<String>,
    val damage: String,
    val name: String,
    val text: String
)