package ru.technostore.model

data class MyCart(
	val basket: List<BasketItem?>? = null,
	val delivery: String? = null,
	val total: Int? = null,
	val id: String? = null
)

data class BasketItem(
	val images: String? = null,
	val price: Int? = null,
	val id: Int? = null,
	val title: String? = null
)

