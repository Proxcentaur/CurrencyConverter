package com.septiadinirahayu.currencyconverter.network

import com.septiadinirahayu.currencyconverter.bean.CurrencyRateResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiService {
    @GET("latest.json")
    @Headers("Accept: application/json", "Content-Type: application/json")
    fun getCurrencyRate(@Query("app_id") appId: String, @Query("base") baseCurrency: String? = null) : Call<CurrencyRateResponse>

}