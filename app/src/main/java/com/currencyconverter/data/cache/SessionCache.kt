package com.currencyconverter.data.cache

@Suppress("TooManyFunctions")
interface SessionCache {

    suspend fun isFirstLaunch(): Boolean

    suspend fun setFirstLaunch(isFirstLaunch: Boolean)

    suspend fun getTransactionCount(): Int

    suspend fun setTransactionCount(count: Int)
}
