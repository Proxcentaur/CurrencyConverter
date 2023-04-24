package com.septiadinirahayu.currencyconverter.database

enum class CacheTimeout (val timeout: Long) {
    NoCache(1L),
    ThirtyMinutes(1800000L)
}