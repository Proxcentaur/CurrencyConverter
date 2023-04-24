package com.septiadinirahayu.currencyconverter.bean.model

import com.septiadinirahayu.currencyconverter.bean.CurrencyRateResponse
import com.septiadinirahayu.currencyconverter.bean.ErrorBean

data class CurrencyConversionUiState (
    val successResponse: CurrencyRateResponse? = null,
    val errorResponse: ErrorBean? = null
)