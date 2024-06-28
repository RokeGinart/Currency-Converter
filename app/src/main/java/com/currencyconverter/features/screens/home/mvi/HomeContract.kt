package com.currencyconverter.features.screens.home.mvi

import com.currencyconverter.data.model.ui.Balance
import com.currencyconverter.data.model.ui.Currency
import com.currencyconverter.data.model.ui.CurrencyRate
import com.currencyconverter.data.model.ui.ExchangeModel
import com.currencyconverter.data.model.ui.ExchangeType
import com.currencyconverter.features.base.mvi.UiEffect
import com.currencyconverter.features.base.mvi.UiEvent
import com.currencyconverter.features.base.mvi.UiState
import com.currencyconverter.utils.formatPrice
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf


class HomeContract {
    sealed class Event : UiEvent {
        data class OnSellAmountChange(val amount: String) : Event()
        data object OnRetryClick : Event()
        data object OnExchangeClick : Event()
        data class OnCurrencyPickerClick(val exchangeType: ExchangeType) : Event()
        data class OnCurrencySelected(val currency: Currency, val exchangeType: ExchangeType) :
            Event()

        data object OnCloseBottomSheet : Event()
        data object OnHideBottomSheet : Event()
        data object OnCloseCheckoutClick : Event()
        data object OnConfirmCheckoutClick : Event()
    }

    data class State(
        val exchangeRateIsLoading: Boolean = true,
        val showError: Boolean = false,
        val showExchangeButton: Boolean = false,
        val balance: ImmutableList<Balance> = persistentListOf(),
        val sellExchange: ExchangeModel = ExchangeModel(
            exchangeType = ExchangeType.SELL
        ),
        val receiveExchange: ExchangeModel = ExchangeModel(
            exchangeType = ExchangeType.RECEIVE
        ),
        val exchangeRate: CurrencyRate? = null,
        val bottomSheet: HomeBottomSheets? = null,
        val transactionCount: Int = 0,
        val fee: Fee = Fee(),
        val showCheckoutDialog: Boolean = false,
    ) : UiState

    sealed class Effect : UiEffect {
        data class ShowToast(val message: String) : Effect()
        data object CloseBottomSheet : Effect()
        data class ScrollToCard(val index: Int) : Effect()
        data object CloseKeyboard : Effect()
    }

    sealed class HomeBottomSheets {
        data class CurrencyPicker(val exchangeType: ExchangeType) : HomeBottomSheets()
    }

    data class Fee(
        val isFree: Boolean = false,
        val freeTransactionCount: Int = 0,
        val feeAmount: Double = 0.0,
        val currency: String = "",
    ) {
        fun getFee(): String? =
            if (feeAmount > 0) "${formatPrice(feeAmount)} $currency" else null
    }
}
