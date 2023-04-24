package com.septiadinirahayu.currencyconverter.domain

import com.septiadinirahayu.currencyconverter.bean.CurrencyRateResponse
import com.septiadinirahayu.currencyconverter.bean.Resource
import com.septiadinirahayu.currencyconverter.network.ApiCallRepository

class NetworkDomain : NetworkUseCase {

    private val apiCallRepository by lazy {
        ApiCallRepository
    }

    override suspend fun getCurrencyRate(baseCurrency: String?): Resource<CurrencyRateResponse> {
        return apiCallRepository.getCurrencyRate(baseCurrency)
    }
}