package com.mikeschvedov.pokecarddex.data.remote.response

data class PricesX(
    val `1stEdition`: StEdition,
    val `1stEditionHolofoil`: StEditionHolofoil,
    val holofoil: Holofoil,
    val normal: Normal,
    val reverseHolofoil: ReverseHolofoil,
    val unlimited: Unlimited,
    val unlimitedHolofoil: UnlimitedHolofoil
)