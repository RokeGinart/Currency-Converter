package com.currencyconverter.domain.usecases.currency

import com.currencyconverter.data.repositories.currency.CurrencyRepository
import javax.inject.Inject

class GetCurrencyRateUseCase @Inject constructor(private val currencyRepository: CurrencyRepository) {
    suspend fun invoke(showLoading: Boolean = false) = currencyRepository.getCurrencyRate(showLoading)
}