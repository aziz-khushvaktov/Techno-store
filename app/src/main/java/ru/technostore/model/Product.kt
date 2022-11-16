package ru.technostore.model

data class Product(
	val sd: String? = null,
	val images: List<String?>? = null,
	val color: List<String?>? = null,
	val ssd: String? = null,
	val price: Int? = null,
	val rating: Double? = null,
	val cPU: String? = null,
	val isFavorites: Boolean? = null,
	val id: String? = null,
	val camera: String? = null,
	val title: String? = null,
	val capacity: List<String?>? = null
)

