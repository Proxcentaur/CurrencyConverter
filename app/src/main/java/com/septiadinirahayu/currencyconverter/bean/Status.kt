package com.septiadinirahayu.currencyconverter.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
enum class Status : Parcelable {
    SUCCESS,
    ERROR,
    CACHE
}