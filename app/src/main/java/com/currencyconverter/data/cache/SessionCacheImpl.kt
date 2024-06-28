package com.currencyconverter.data.cache

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.currencyconverter.utils.getBooleanValue
import com.currencyconverter.utils.getIntValue
import com.currencyconverter.utils.setValue
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionCacheImpl @Inject constructor(
    @ApplicationContext context: Context,
    private val coroutineScope: CoroutineScope,
) : SessionCache {

    private val Context.dataStore by preferencesDataStore(
        name = PREFS_FILENAME,
        produceMigrations = { context ->
            listOf(SharedPreferencesMigration(context, PREFS_FILENAME, keysToMigrate))
        }
    )
    private val dataStore = context.dataStore

    override suspend fun isFirstLaunch(): Boolean =
        dataStore.getBooleanValue(KEY_IS_FIRST_LAUNCH) ?: true

    override suspend fun setFirstLaunch(isFirstLaunch: Boolean) {
        try {
            dataStore.edit { preferences ->
                preferences.setValue(KEY_IS_FIRST_LAUNCH, isFirstLaunch)
            }
        } catch (e: IOException) {
            Log.e(SessionCacheImpl::class.simpleName, "$e")
        }
    }

    override suspend fun getTransactionCount(): Int =
        dataStore.getIntValue(KEY_TRANSACTION_COUNT) ?: 0

    override suspend fun setTransactionCount(count: Int) {
        try {
            dataStore.edit { preferences ->
                preferences.setValue(KEY_TRANSACTION_COUNT, count)
            }
        } catch (e: IOException) {
            Log.e(SessionCacheImpl::class.simpleName, "$e")
        }
    }

    companion object {
        private const val PREFS_FILENAME = "sessionCache"
        private const val SESSION_ACCESS_TOKEN = "access_token"
        private const val FIRST_LAUNCH = "is_first_launching"
        private const val TRANSACTION_COUNT = "transaction_count"

        private val KEY_IS_FIRST_LAUNCH = booleanPreferencesKey(FIRST_LAUNCH)
        private val KEY_TRANSACTION_COUNT = intPreferencesKey(TRANSACTION_COUNT)

        private val keysToMigrate = setOf(
            SESSION_ACCESS_TOKEN
        )
    }
}