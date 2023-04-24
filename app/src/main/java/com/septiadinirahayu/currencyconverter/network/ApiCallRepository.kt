package com.septiadinirahayu.currencyconverter.network

import com.septiadinirahayu.currencyconverter.bean.CurrencyRateResponse
import com.septiadinirahayu.currencyconverter.bean.Resource
import kotlinx.coroutines.Dispatchers

object ApiCallRepository : BaseRepository() {

    private const val BASE_URL = "https://openexchangerates.org/api/"
    private const val APP_ID = "12011c82d73d473a8dff9fc8544154be"

    private val restApi: ApiService by lazy {
        ApiRestAdapter.createDefaultRestService(BASE_URL, ApiService::class.java)
    }

    suspend fun getCurrencyRate(baseCurrency: String? = null) : Resource<CurrencyRateResponse> {
        return callApi({ restApi.getCurrencyRate(APP_ID, baseCurrency)}, Dispatchers.IO)
    }

}