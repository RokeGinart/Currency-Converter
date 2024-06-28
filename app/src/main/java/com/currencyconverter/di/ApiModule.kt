package com.currencyconverter.di

import com.currencyconverter.data.api.CurrencyApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ApiModule {

    @Singleton
    @Provides
    fun provideCurrencyApi(retrofit: Retrofit): CurrencyApi =
        retrofit.create(CurrencyApi::class.java)
}
