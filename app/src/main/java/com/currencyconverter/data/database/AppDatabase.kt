package com.currencyconverter.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.currencyconverter.data.database.dao.BalanceDao
import com.currencyconverter.data.database.entity.BalanceEntity

@Database(
    entities = [
        BalanceEntity::class,
    ],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun balanceDao(): BalanceDao
}
