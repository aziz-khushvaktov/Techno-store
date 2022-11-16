package ru.technostore.model

import com.google.gson.annotations.SerializedName

data class BestSeller(
    @field:SerializedName("is_favorites")
    val isFavorites: Boolean? = null,

    @field:SerializedName("discount_price")
    val discountPrice: Int? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("title")
    val title: String? = null,

    @field:SerializedName("price_without_discount")
    val priceWithoutDiscount: Int? = null,

    @field:SerializedName("picture")
    val picture: String? = null
) {}
