package com.septiadinirahayu.currencyconverter.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.septiadinirahayu.currencyconverter.bean.CurrencyRateResponse
import com.septiadinirahayu.currencyconverter.bean.ErrorBean
import com.septiadinirahayu.currencyconverter.bean.Status
import com.septiadinirahayu.currencyconverter.bean.model.CurrencyConversionUiState
import com.septiadinirahayu.currencyconverter.domain.NetworkDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CurrencyConversionViewModel (private val networkDomain: NetworkDomain)
    : ViewModel() {
    private val _uiState = MutableLiveData(CurrencyConversionUiState())
    val uiState: LiveData<CurrencyConversionUiState> = _uiState

    fun getCurrencyRate(baseCurrency: String? = null) {
        viewModelScope.launch {
            val result = networkDomain.getCurrencyRate()
            withContext(Dispatchers.Main) {
                when (result.status) {
                    Status.SUCCESS -> {
                        onSuccessGetCurrencyRates(result.data)
                    }
                    Status.ERROR -> {
                        onErrorGetCurrencyRates(result.error)
                    }
                    else -> {}
                }
            }

        }
    }

    private fun onSuccessGetCurrencyRates(rates: CurrencyRateResponse?) {
        val currentState = _uiState.value?.copy(successResponse = rates)
        _uiState.postValue(currentState)
    }

    private fun onErrorGetCurrencyRates(error: ErrorBean?) {
        val currentState = _uiState.value?.copy(errorResponse = error)
        _uiState.postValue(currentState)
    }
}