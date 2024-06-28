package com.currencyconverter.domain.usecases.currency

import com.currencyconverter.data.repositories.currency.CurrencyRepository
import javax.inject.Inject

class SubscribeToCurrencyRateUseCase @Inject constructor(private val currencyRepository: CurrencyRepository) {
    fun invoke() = currencyRepository.currencyRate
}