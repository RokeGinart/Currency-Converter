package com.currencyconverter.di

import com.currencyconverter.data.cache.SessionCache
import com.currencyconverter.data.cache.SessionCacheImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class CacheModule {

    @Singleton
    @Binds
    abstract fun provideSessionCache(sessionCacheImpl: SessionCacheImpl): SessionCache
}
