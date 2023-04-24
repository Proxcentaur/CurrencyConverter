package com.septiadinirahayu.currencyconverter.bean.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.septiadinirahayu.currencyconverter.domain.NetworkDomain

class CurrencyConversionViewModelFactory(
    private val networkDomain: NetworkDomain)
    : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass
            .getConstructor(NetworkDomain::class.java)
            .newInstance(networkDomain)
    }

}