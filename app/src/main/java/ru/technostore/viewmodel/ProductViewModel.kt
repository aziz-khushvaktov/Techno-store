package ru.technostore.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.technostore.model.Product
import ru.technostore.network.service.ProductService
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(private val productService: ProductService): ViewModel() {

    val productResponseList = MutableLiveData<Product?>()

    fun getProductResponseList() {
        productService.apiGetProducts().enqueue(object : Callback<Product> {
            override fun onResponse(call: Call<Product>, response: Response<Product>) {
                productResponseList.value = response.body()
            }

            override fun onFailure(call: Call<Product>, t: Throwable) {
                productResponseList.value = null
            }

        })
    }
}