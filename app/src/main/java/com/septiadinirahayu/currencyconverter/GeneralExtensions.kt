package com.septiadinirahayu.currencyconverter


import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.google.gson.annotations.SerializedName
import java.text.DecimalFormat

val <E : Enum<E>> Enum<E>.serializedName: String?
    get() = javaClass
        .getField(name)
        .getAnnotation(SerializedName::class.java)
        ?.value

fun isInternetAvailable() : Boolean {
    val connectivityManager = App.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
    val networkCapabilities = connectivityManager?.getNetworkCapabilities(connectivityManager.activeNetwork) ?: return false
    val isWifi = networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
    val isCellular = networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
    val isEthernet = networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
    return isWifi || isCellular || isEthernet
}

fun showSnackBar(view: View, message: String) {
    val snackBar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
    val snackBarView = snackBar.view
    snackBarView.setBackgroundColor(ContextCompat.getColor(App.applicationContext, R.color.teal_200))
    val snackBarText = snackBarView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
    snackBarText.setTextColor(ContextCompat.getColor(App.applicationContext, R.color.black))
    snackBar.show()
}

fun getFormattedRates(value: Double) : String {
    return DecimalFormat("#,##0.00").format(value)
}