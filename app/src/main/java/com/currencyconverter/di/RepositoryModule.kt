package com.currencyconverter.di

import com.currencyconverter.data.repositories.balance.BalanceRepository
import com.currencyconverter.data.repositories.balance.BalanceRepositoryImpl
import com.currencyconverter.data.repositories.currency.CurrencyRepository
import com.currencyconverter.data.repositories.currency.CurrencyRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Suppress("unused")
@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoriesModule {

    @Singleton
    @Binds
    abstract fun provideCurrencyRepository(currencyRepository: CurrencyRepositoryImpl): CurrencyRepository

     @Singleton
    @Binds
    abstract fun provideBalanceRepository(balanceRepository: BalanceRepositoryImpl): BalanceRepository
}
