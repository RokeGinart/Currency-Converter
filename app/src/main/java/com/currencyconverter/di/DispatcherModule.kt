package com.currencyconverter.di

import android.util.Log
import com.currencyconverter.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton
import kotlin.coroutines.cancellation.CancellationException

@InstallIn(SingletonComponent::class)
@Module
object DispatcherModule {

    @Provides
    fun providesCoroutineContext() =
        Dispatchers.Main + SupervisorJob() + CoroutineExceptionHandler { _, throwable ->
            if (throwable is CancellationException) {
                throw throwable
            } else {
                if (BuildConfig.DEBUG) {
                    throw throwable
                }
            }
        }

    @DefaultDispatcher
    @Provides
    fun providesDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @IoDispatcher
    @Provides
    fun providesIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @MainDispatcher
    @Provides
    fun providesMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @Provides
    @Singleton
    fun provideCoroutineScope(): CoroutineScope {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            if (throwable is CancellationException) {
                throw throwable
            } else {
                Log.e("ApplicationScope","$throwable")
                if (BuildConfig.DEBUG) {
                    throw throwable
                }
            }
        }
        return CoroutineScope(SupervisorJob() + Dispatchers.Main + exceptionHandler)
    }
}

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class DefaultDispatcher

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class IoDispatcher

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class MainDispatcher
