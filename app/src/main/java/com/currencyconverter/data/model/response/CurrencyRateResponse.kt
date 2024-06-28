package com.currencyconverter.data.model.response

import com.currencyconverter.data.model.ui.Currency
import com.currencyconverter.data.model.ui.CurrencyRate
import com.squareup.moshi.Json

data class CurrencyRateResponse(
    @Json(name = "base") val base: String? = null,
    @Json(name = "date") val date: String? = null,
    @Json(name = "rates") val rates: Map<String, Double>? = null,
) {
    fun mapToCurrencyRate(): CurrencyRate {
        return CurrencyRate(
            date = date ?: "",
            currencies = rates?.map {
                Currency(
                    currency = it.key,
                    rate = it.value
                )
            } ?: emptyList()
        )
    }
}

