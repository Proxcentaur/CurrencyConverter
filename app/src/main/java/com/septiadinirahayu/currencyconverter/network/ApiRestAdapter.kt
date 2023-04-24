package com.septiadinirahayu.currencyconverter.network

import com.septiadinirahayu.currencyconverter.App
import com.google.gson.Gson
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiRestAdapter {

    private const val cacheSize = (6 * 1024 * 1024).toLong()

    private val respCache = Cache(App.applicationContext.cacheDir, cacheSize)

    private val okHttpClient: OkHttpClient by lazy {
        baseOkHttpClientBuilder().cache(respCache).addInterceptor(CachingInterceptor()).build()
    }

    fun <T> createDefaultRestService(
        url: String,
        classObj: Class<T>
    ) : T = createBaseRestService(
        url,
        classObj,
        okHttpClient,
        GsonConverterFactory.create(Gson())
    )

    private fun <T> createBaseRestService(
        url: String,
        classObj: Class<T>,
        client: OkHttpClient,
        converterFactory: Converter.Factory
    ): T {
        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .client(client)
            .addConverterFactory(converterFactory)
            .build()
        return retrofit.create(classObj)
    }

    private fun baseOkHttpClientBuilder(): OkHttpClient.Builder {
        return OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.MINUTES)
            .readTimeout(5, TimeUnit.MINUTES)
            .retryOnConnectionFailure(false)
    }
}