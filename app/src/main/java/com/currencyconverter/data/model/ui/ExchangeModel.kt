package com.currencyconverter.data.model.ui

data class ExchangeModel(
    val amount: String = "",
    val currency: String = "",
    val exchangeType: ExchangeType
) {
    override fun toString() = "$amount $currency"
}