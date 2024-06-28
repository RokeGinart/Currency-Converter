package com.currencyconverter.data.repositories.currency

import com.currencyconverter.data.model.ui.CurrencyRate
import com.currencyconverter.utils.ResultReceiver
import kotlinx.coroutines.flow.SharedFlow


interface CurrencyRepository {

    val currencyRate: SharedFlow<ResultReceiver<CurrencyRate>>

    suspend fun getCurrencyRate(showLoading: Boolean)
}