package ru.technostore.model

import com.google.gson.annotations.SerializedName

data class Main(
	@field:SerializedName("best_seller")
	val bestSeller: List<BestSeller?>? = null,

	@field:SerializedName("home_store")
	val homeStore: List<HomeStore?>? = null
)


