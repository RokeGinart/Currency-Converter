package com.currencyconverter.data.repositories.balance

import com.currencyconverter.data.model.ui.Balance
import kotlinx.coroutines.flow.SharedFlow

interface BalanceRepository {

    val userBalance: SharedFlow<List<Balance>>

    suspend fun updateOrInsertBalance(amount: Double, currency: String)
}