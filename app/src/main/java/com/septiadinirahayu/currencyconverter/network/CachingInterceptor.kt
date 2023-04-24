package com.septiadinirahayu.currencyconverter.network

import com.septiadinirahayu.currencyconverter.database.CacheTimeout
import com.septiadinirahayu.currencyconverter.isInternetAvailable
import okhttp3.Interceptor
import okhttp3.Response

class CachingInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val headerValue = if (isInternetAvailable()) {
            "public, max-age=" + CacheTimeout.ThirtyMinutes
        } else {
            "public, only-if-cached, max-stale=" + CacheTimeout.ThirtyMinutes
        }
        val newRequest = request.newBuilder()
            .header(
            "Cache-Control",
            headerValue)
            .removeHeader("Pragma")

        return chain.proceed(newRequest.build())
    }
}