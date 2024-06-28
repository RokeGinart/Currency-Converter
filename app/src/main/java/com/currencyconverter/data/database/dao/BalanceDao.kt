package com.currencyconverter.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.currencyconverter.data.database.entity.BalanceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BalanceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserBalance(balanceEntity: BalanceEntity): Long

    @Query(value = "SELECT * FROM balance ORDER BY amount DESC")
    fun getUserBalance(): Flow<List<BalanceEntity>>

    @Query(value = "UPDATE balance SET amount=:amount WHERE currency=:currency")
    fun updateUserBalance(amount: Double, currency: String)
}