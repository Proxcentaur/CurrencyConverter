package com.septiadinirahayu.currencyconverter.network

import androidx.lifecycle.MutableLiveData
import com.septiadinirahayu.currencyconverter.bean.Resource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NetworkCallback <T> (private val mData : MutableLiveData<Resource<T>>) : Callback<T>{
    override fun onResponse(call: Call<T>, response: Response<T>) {
        TODO("Not yet implemented")
    }

    override fun onFailure(call: Call<T>, t: Throwable) {
        TODO("Not yet implemented")
    }
}