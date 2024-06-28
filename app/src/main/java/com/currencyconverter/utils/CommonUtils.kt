package com.currencyconverter.utils

import android.content.Context
import android.widget.Toast

const val ROOM_ERROR_CODE = -1L
const val RATE_UPDATE_TIME = 5000L
const val EUR = "EUR"


fun formatPrice(value: Double) = String.format("%.2f", value)

fun formatPriceWithoutZero(value: Double) =
    String.format("%.2f", value)
        .replace(Regex("0*$"), "")
        .replace(Regex("\\.$"), "")

fun showToast(context: Context, text: String) {
    Toast.makeText(
        context,
        text,
        Toast.LENGTH_SHORT
    ).show()
}