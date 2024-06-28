package com.currencyconverter.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.currencyconverter.data.model.ui.Balance

@Entity(
    tableName = "balance"
)
data class BalanceEntity(
    @PrimaryKey val currency: String,
    @ColumnInfo(name = "amount") val amount: Double,
) {
    fun mapToBalance() = Balance(
        currency = currency,
        amount = amount
    )
}
