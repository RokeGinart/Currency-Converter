package com.currencyconverter.domain.usecases.balance

import com.currencyconverter.data.repositories.balance.BalanceRepository
import javax.inject.Inject

class UpdateOrInsertBalanceUseCase @Inject constructor(private val balanceRepository: BalanceRepository) {
    suspend fun invoke(amount: Double, currency: String) =
        balanceRepository.updateOrInsertBalance(amount, currency)
}