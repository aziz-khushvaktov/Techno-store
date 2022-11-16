package ru.technostore.model

import com.google.gson.annotations.SerializedName

data class HomeStore(
    @field:SerializedName("subtitle")
    val subtitle: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("title")
    val title: String? = null,

    @field:SerializedName("picture")
    val picture: String? = null,

    @field:SerializedName("is_buy")
    val isBuy: Boolean? = null,

    @field:SerializedName("is_new")
    val isNew: Boolean? = null
) {}
