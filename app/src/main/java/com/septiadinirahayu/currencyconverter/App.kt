package com.septiadinirahayu.currencyconverter

import android.app.Application
import android.content.Context

class App : Application() {

    companion object {
        lateinit var applicationContext : Context
    }
    override fun onCreate() {
        super.onCreate()
        Companion.applicationContext = applicationContext
    }

}