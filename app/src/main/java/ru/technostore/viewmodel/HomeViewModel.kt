package ru.technostore.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.technostore.model.Main
import ru.technostore.network.service.MainService
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val mainService: MainService): ViewModel() {

    val mainRes = MutableLiveData<Main?>()

    fun apiGetAllMainData() {
        mainService.getListOfMain().enqueue(object : Callback<Main> {
            override fun onResponse(call: Call<Main>, response: Response<Main>) {
                mainRes.postValue(response.body())
                Log.d("mainn", response.body().toString())

            }

            override fun onFailure(call: Call<Main>, t: Throwable) {
                mainRes.value = null
            }

        })
    }
}