package com.kunle.aisle9b.models.apiModels.recipeModels

data class WinePairing(
    val pairedWines: List<String>,
    val pairingText: String,
    val productMatches: List<ProductMatche>
)