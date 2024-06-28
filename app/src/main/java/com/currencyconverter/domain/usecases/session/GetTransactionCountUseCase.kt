package com.currencyconverter.domain.usecases.session

import com.currencyconverter.data.cache.SessionCache
import javax.inject.Inject

class GetTransactionCountUseCase @Inject constructor(private val sessionCache: SessionCache) {
    suspend operator fun invoke() = sessionCache.getTransactionCount()
}