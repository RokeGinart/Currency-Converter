package com.currencyconverter.di

import com.currencyconverter.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface NetworkModule {

    @Suppress("TooManyFunctions")
    companion object {
        private const val REQUEST_TIMEOUT = 20L
        private const val INTERCEPTOR_LOGGING = "logging-interceptor"

        @Singleton
        @Provides
        @SuppressWarnings("LongParameterList")
        fun provideOkHttpClient(
            @Named(INTERCEPTOR_LOGGING) loggingInterceptor: Interceptor,
        ): OkHttpClient =
            OkHttpClient.Builder().apply {
                connectTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
                readTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
                writeTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
                addInterceptor(loggingInterceptor)
            }.build()

        @Provides
        @Named(INTERCEPTOR_LOGGING)
        fun provideLoggingInterceptor(): Interceptor =
            HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor.Level.BODY
                } else {
                    HttpLoggingInterceptor.Level.NONE
                }
            }

        @Provides
        @Singleton
        fun provideRetrofit(
            okHttpClient: Lazy<OkHttpClient>,
            moshi: Moshi,
        ): Retrofit =
            Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .callFactory { call -> okHttpClient.get().newCall(call) }
                .baseUrl(BuildConfig.BASE_URL)
                .build()

        @Provides
        @Singleton
        fun providesMoshi(): Moshi =
            Moshi.Builder()
                .addLast(KotlinJsonAdapterFactory())
                .build()

    }
}
