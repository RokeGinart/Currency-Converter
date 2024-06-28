package com.currencyconverter.data.repositories.currency

import android.util.Log
import com.currencyconverter.data.api.CurrencyApi
import com.currencyconverter.data.model.ui.CurrencyRate
import com.currencyconverter.utils.RATE_UPDATE_TIME
import com.currencyconverter.utils.ResultReceiver
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class CurrencyRepositoryImpl @Inject constructor(
    private val api: CurrencyApi,
) : CurrencyRepository {

    private var repeatableJob: Job? = null

    private val _currencyRate = MutableSharedFlow<ResultReceiver<CurrencyRate>>(replay = 1)
    override val currencyRate: SharedFlow<ResultReceiver<CurrencyRate>>
        get() = _currencyRate

    override suspend fun getCurrencyRate(showLoading: Boolean) {
        if (showLoading) _currencyRate.emit(ResultReceiver.Loading)
        repeatableJob?.cancel()
        fetchData()
    }

    private suspend fun fetchData() = coroutineScope {
        repeatableJob = launch {
            Log.d("TAGS", "HERE")
            while (true) {
                try {
                    val response = api.getCurrencyExchangeRate()
                    if (response.isSuccessful) {
                        Log.d("TAGS", "response $response")

                        response.body()?.let { body ->
                            _currencyRate.emit(ResultReceiver.Success(body.mapToCurrencyRate()))
                        } ?: let {
                            _currencyRate.emit(ResultReceiver.Error(Throwable("Empty body")))
                        }
                    } else {
                        _currencyRate.emit(ResultReceiver.Error(Throwable("Something went wrong")))
                    }
                } catch (e: Exception) {
                    _currencyRate.emit(ResultReceiver.Error(Throwable("Check internet connection")))
                }

                delay(RATE_UPDATE_TIME)
            }
        }
    }
}