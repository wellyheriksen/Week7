package com.example.week7.soal1.model

import com.google.gson.annotations.SerializedName

data class PokemonCard(
    val id: String = "",
    val name: String = "",
    @SerializedName("setName", alternate = ["set_name", "set"])
    val setName: String = "",
    @SerializedName("cardNumber", alternate = ["card_number", "number"])
    val cardNumber: String = "",
    @SerializedName("imageUrl", alternate = ["image_url", "image"])
    val imageUrl: String = "",
    @SerializedName("marketPrice", alternate = ["market_price", "price"])
    val marketPrice: Double = 0.0,
    @SerializedName("lowestPrice", alternate = ["lowest_price"])
    val lowestPrice: Double = 0.0,
    @SerializedName("activeListings", alternate = ["active_listings"])
    val activeListings: Int = 0,
    @SerializedName("verifiedSellers", alternate = ["verified_sellers"])
    val verifiedSellers: Int = 0,
    @SerializedName("primaryPrinting", alternate = ["primary_printing"])
    val primaryPrinting: String = "",
    val rarity: String = "",
    val type: String = "",
    val illustrator: String = ""
)

data class PokemonResponse(
    val data: List<PokemonCard>? = null,
    val count: Int? = 0,
    val totalCount: Int? = 0
)

sealed class UiState {
    object Loading : UiState()
    data class Success(val cards: List<PokemonCard>) : UiState()
    data class Error(val code: Int = 401, val message: String = "HTTP 401") : UiState()
}
