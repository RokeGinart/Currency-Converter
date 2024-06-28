package com.currencyconverter.data.model.ui

import com.currencyconverter.utils.formatPriceWithoutZero

data class Balance(
    val amount: Double,
    val currency: String,
) {
    override fun toString() = "${formatPriceWithoutZero(amount)} $currency"
}