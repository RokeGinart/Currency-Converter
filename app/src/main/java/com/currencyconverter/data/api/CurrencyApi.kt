package com.currencyconverter.data.api

import com.currencyconverter.data.model.response.CurrencyRateResponse
import retrofit2.Response
import retrofit2.http.GET

interface CurrencyApi {

    @GET("currency-exchange-rates")
    suspend fun getCurrencyExchangeRate(): Response<CurrencyRateResponse>
}
