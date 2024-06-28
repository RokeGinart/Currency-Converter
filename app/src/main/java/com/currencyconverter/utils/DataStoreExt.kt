package com.currencyconverter.utils

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import java.io.IOException

fun <T> MutablePreferences.setValue(key: Preferences.Key<T>, value: T?) {
    if (value == null) {
        remove(key)
    } else {
        this[key] = value
    }
}

fun DataStore<Preferences>.catchIOExceptions(): Flow<Preferences> =
    data.catch { exception ->
        // dataStore.data throws an IOException when an error is encountered when reading data
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }

suspend fun DataStore<Preferences>.getSafePreferences(): Preferences =
    catchIOExceptions().first()

suspend fun <T> DataStore<Preferences>.setValue(key: Preferences.Key<T>, value: T?) {
    try {
        edit { preferences ->
            preferences.setValue(key, value)
        }
    } catch (e: IOException) {
        Log.e("Error" , "$e")
    }
}


suspend fun DataStore<Preferences>.getStringValue(
    key: Preferences.Key<String>,
    defValue: String? = null
): String? =
    getSafePreferences()[key] ?: defValue

suspend fun DataStore<Preferences>.getIntValue(
    key: Preferences.Key<Int>,
    defValue: Int? = null
): Int? =
    getSafePreferences()[key] ?: defValue

suspend fun DataStore<Preferences>.getBooleanValue(
    key: Preferences.Key<Boolean>,
    defValue: Boolean? = null
): Boolean? =
    getSafePreferences()[key] ?: defValue