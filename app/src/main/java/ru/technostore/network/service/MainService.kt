package ru.technostore.network.service

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import ru.technostore.model.Main

interface MainService {

    @Headers("Content-type:application/json")

    @GET("654bd15e-b121-49ba-a588-960956b15175")
    fun getListOfMain(): Call<Main>


}