package com.currencyconverter.data.model.ui

data class CurrencyRate(
    val date: String,
    val currencies: List<Currency>
)

data class Currency(
    val currency: String,
    val rate: Double,
)