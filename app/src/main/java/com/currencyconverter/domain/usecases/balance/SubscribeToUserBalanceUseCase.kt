package com.currencyconverter.domain.usecases.balance

import com.currencyconverter.data.repositories.balance.BalanceRepository
import javax.inject.Inject

class SubscribeToUserBalanceUseCase @Inject constructor(private val balanceRepository: BalanceRepository) {
    fun invoke() = balanceRepository.userBalance
}