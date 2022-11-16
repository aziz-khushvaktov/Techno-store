package ru.technostore.network.service

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import ru.technostore.model.MyCart

interface CartService {

    @Headers("Content-type:application/json")

    @GET("53539a72-3c5f-4f30-bbb1-6ca10d42c149")
    fun apiGetCarts(): Call<MyCart>
}