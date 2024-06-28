package com.currencyconverter.data.repositories.balance

import com.currencyconverter.data.cache.SessionCache
import com.currencyconverter.data.database.AppDatabase
import com.currencyconverter.data.database.entity.BalanceEntity
import com.currencyconverter.data.model.ui.Balance
import com.currencyconverter.utils.ROOM_ERROR_CODE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class BalanceRepositoryImpl @Inject constructor(
    private val session: SessionCache,
    private val database: AppDatabase,
    coroutineScope: CoroutineScope,
) : BalanceRepository {

    private val _userBalance = MutableSharedFlow<List<Balance>>(replay = 1)
    override val userBalance: SharedFlow<List<Balance>>
        get() = _userBalance

    init {
        coroutineScope.launch {
            if (session.isFirstLaunch()) {
                updateOrInsertBalance(1000.0, "EUR")
                session.setFirstLaunch(false)
            }

            database.balanceDao().getUserBalance().collectLatest { balance ->
                _userBalance.emit(balance.map { it.mapToBalance() })
            }
        }
    }

    override suspend fun updateOrInsertBalance(amount: Double, currency: String) {
        val balanceEntity = BalanceEntity(
            currency = currency,
            amount = amount
        )
        val insertBalance = database.balanceDao().insertUserBalance(balanceEntity)

        if (insertBalance == ROOM_ERROR_CODE) database.balanceDao()
            .updateUserBalance(amount, currency)
    }
}

