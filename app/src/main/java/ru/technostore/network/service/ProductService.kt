package ru.technostore.network.service


import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import ru.technostore.model.Product

interface ProductService {

    @Headers("Content-type:application/json")

    @GET("6c14c560-15c6-4248-b9d2-b4508df7d4f5")
    fun apiGetProducts(): Call<Product>
}