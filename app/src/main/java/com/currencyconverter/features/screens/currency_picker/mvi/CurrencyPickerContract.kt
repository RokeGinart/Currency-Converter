package com.currencyconverter.features.screens.currency_picker.mvi

import com.currencyconverter.data.model.ui.Currency
import com.currencyconverter.data.model.ui.ExchangeType
import com.currencyconverter.features.base.mvi.UiEffect
import com.currencyconverter.features.base.mvi.UiEvent
import com.currencyconverter.features.base.mvi.UiState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf


class CurrencyPickerContract {
    sealed class Event : UiEvent {
        data class OnValueChanged(val search: String) : Event()
        data object OnCloseClick : Event()
        data class InitScreen(val type: ExchangeType) : Event()
        data class OnCurrencyClicked(val currency: Currency) : Event()
        data object OnRetryClick : Event()
    }

    data class State(
        val isLoading: Boolean = false,
        val showError: Boolean = false,
        val search: String = "",
        val searchResult: ImmutableList<Currency>? = null,
        val lastUpdate: String = "",
        val type: ExchangeType = ExchangeType.SELL,
        val currencyRate: ImmutableList<Currency> = persistentListOf(),
        val userBalance: ImmutableList<Currency> = persistentListOf(),
        val currencies: ImmutableList<Currency> = persistentListOf()
    ) : UiState

    sealed class Effect : UiEffect {
        data object Close: Effect()
        data class ReturnResult(val currency: Currency, val exchangeType: ExchangeType): Effect()
    }
}
