package com.septiadinirahayu.currencyconverter.network

import com.google.gson.Gson
import com.septiadinirahayu.currencyconverter.bean.Currency
import com.septiadinirahayu.currencyconverter.bean.CurrencyRateResponse
import com.septiadinirahayu.currencyconverter.domain.NetworkDomain
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.net.HttpURLConnection

class ApiCallRepositoryTest {
    private lateinit var repository: ApiCallRepository
    private lateinit var apiService: ApiService
    private lateinit var mockWebServer: MockWebServer

    @Before
    fun setupTest() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        repository = ApiCallRepository
    }

    @After
    fun shutDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun loadCurrencyListAtMainActivity() = runTest {
        val networkDomain = NetworkDomain()
        val rates = CurrencyRateResponse(
            disclaimer = "some disclaimer",
            license = "some license",
            timestamp = 1680891664,
            base = "USD",
            rates = mapOf(Currency.AFN to 86.5)
        )

        val expectedApiResponse = MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(Gson().toJson(rates))

        mockWebServer.enqueue(expectedApiResponse)

        val actualResponse = networkDomain.getCurrencyRate()
        assertTrue(actualResponse.data?.getListConversion()?.isNotEmpty() == true)

    }


}