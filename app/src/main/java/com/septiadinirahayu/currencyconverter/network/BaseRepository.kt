package com.septiadinirahayu.currencyconverter.network
import com.septiadinirahayu.currencyconverter.bean.ErrorBean
import com.google.gson.Gson
import com.septiadinirahayu.currencyconverter.bean.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.awaitResponse

open class BaseRepository {
    suspend inline fun <reified T> callApi (
        crossinline apiCall: () -> Call<T>,
        dispatcher: CoroutineDispatcher,
    ) : Resource<T> {
        return withContext(dispatcher) {
            val call = apiCall.invoke()
            val response = call.awaitResponse()
            if (response.isSuccessful) {
                val body = response.body()
                if (body is T) {
                    Resource.success(body)
                } else {
                   Resource.error(ErrorBean(error = true, status = -1, message = "Parsing Failed!", description = "Unable to parse response"))
                }
            } else {
                val errorBody = response.errorBody()?.string()
                val errorBean = Gson().fromJson(errorBody, ErrorBean::class.java)
                if (errorBean != null) {
                    Resource.error(errorBean)
                } else {
                    Resource.error(ErrorBean(error = true, status = -2, message = "An error has occurred", description = "Something went wrong. Please try again later!"))
                }
            }
        }
    }

}

