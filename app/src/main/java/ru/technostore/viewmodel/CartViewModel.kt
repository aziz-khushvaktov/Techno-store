package ru.technostore.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.technostore.model.MyCart
import ru.technostore.network.service.CartService
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(var cartService: CartService): ViewModel() {

    var cartList = MutableLiveData<MyCart?>()

    fun getCartList() {
        cartService.apiGetCarts().enqueue(object : Callback<MyCart>{
            override fun onResponse(call: Call<MyCart>, response: Response<MyCart>) {
                cartList.value = response.body()
            }

            override fun onFailure(call: Call<MyCart>, t: Throwable) {
                cartList.postValue(null)
            }

        })
    }
}