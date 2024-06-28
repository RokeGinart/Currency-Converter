package com.currencyconverter.di

import com.currencyconverter.data.database.AppDatabase
import com.currencyconverter.data.database.dao.BalanceDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DaosModule {
    @Provides
    fun providesBalanceDao(
        database: AppDatabase,
    ): BalanceDao = database.balanceDao()
}
