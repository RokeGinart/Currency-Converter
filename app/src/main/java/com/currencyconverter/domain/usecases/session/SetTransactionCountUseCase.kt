package com.currencyconverter.domain.usecases.session

import com.currencyconverter.data.cache.SessionCache
import javax.inject.Inject

class SetTransactionCountUseCase @Inject constructor(private val sessionCache: SessionCache) {
    suspend fun invoke(count: Int) = sessionCache.setTransactionCount(count)
}