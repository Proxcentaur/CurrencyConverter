package com.septiadinirahayu.currencyconverter.domain

import com.septiadinirahayu.currencyconverter.bean.CurrencyRateResponse
import com.septiadinirahayu.currencyconverter.bean.Resource

interface NetworkUseCase {
    suspend fun getCurrencyRate(baseCurrency: String? = null) : Resource<CurrencyRateResponse>
}