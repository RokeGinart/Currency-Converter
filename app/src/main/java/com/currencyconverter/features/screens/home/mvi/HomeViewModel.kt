package com.currencyconverter.features.screens.home.mvi

import androidx.lifecycle.viewModelScope
import com.currencyconverter.data.model.ui.Currency
import com.currencyconverter.data.model.ui.ExchangeType
import com.currencyconverter.domain.usecases.balance.SubscribeToUserBalanceUseCase
import com.currencyconverter.domain.usecases.balance.UpdateOrInsertBalanceUseCase
import com.currencyconverter.domain.usecases.currency.GetCurrencyRateUseCase
import com.currencyconverter.domain.usecases.currency.SubscribeToCurrencyRateUseCase
import com.currencyconverter.domain.usecases.session.GetTransactionCountUseCase
import com.currencyconverter.domain.usecases.session.SetTransactionCountUseCase
import com.currencyconverter.features.base.mvi.MviViewModel
import com.currencyconverter.features.screens.home.mvi.HomeContract.Effect
import com.currencyconverter.features.screens.home.mvi.HomeContract.Event
import com.currencyconverter.features.screens.home.mvi.HomeContract.State
import com.currencyconverter.utils.EUR
import com.currencyconverter.utils.ResultReceiver
import com.currencyconverter.utils.formatPrice
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCurrencyRateUseCase: GetCurrencyRateUseCase,
    private val getTransactionCountUseCase: GetTransactionCountUseCase,
    private val setTransactionCountUseCase: SetTransactionCountUseCase,
    private val updateOrInsertBalanceUseCase: UpdateOrInsertBalanceUseCase,
    private val subscribeToUserBalanceUseCase: SubscribeToUserBalanceUseCase,
    private val subscribeToCurrencyRateUseCase: SubscribeToCurrencyRateUseCase,
) : MviViewModel<Event, State, Effect>() {

    init {
        setState {
            copy(
                sellExchange = sellExchange.copy(currency = EUR),
                receiveExchange = receiveExchange.copy(amount = "0", currency = "USD"),
            )
        }

        subscribeToUserBalance()
        subscribeToExchangeRate()
    }

    override fun createInitialState(): State = State()

    override fun handleEvent(event: Event) {
        when (event) {
            is Event.OnSellAmountChange -> changeSellAmount(event.amount)
            Event.OnRetryClick -> getCurrency()
            Event.OnExchangeClick -> setState { copy(showCheckoutDialog = true) }
            is Event.OnCurrencyPickerClick -> setState {
                copy(
                    bottomSheet = HomeContract.HomeBottomSheets.CurrencyPicker(event.exchangeType)
                )
            }

            Event.OnCloseBottomSheet -> setEffect { Effect.CloseBottomSheet }
            Event.OnHideBottomSheet -> setState {
                copy(
                    bottomSheet = null
                )
            }

            is Event.OnCurrencySelected -> changeConvertorCurrency(
                event.currency,
                event.exchangeType
            )

            Event.OnCloseCheckoutClick -> setState { copy(showCheckoutDialog = false) }
            Event.OnConfirmCheckoutClick -> exchangeCurrency()
        }
    }

    private fun subscribeToUserBalance() {
        viewModelScope.launch {
            subscribeToUserBalanceUseCase.invoke().collectLatest {
                setState {
                    copy(
                        balance = it.toImmutableList(),
                    )
                }

                manageTransactionCount()
            }
        }
    }

    private fun subscribeToExchangeRate() {
        viewModelScope.launch {
            subscribeToCurrencyRateUseCase.invoke().collectLatest { result ->
                when (result) {
                    is ResultReceiver.Error -> {
                        setState {
                            copy(
                                exchangeRateIsLoading = false,
                                showError = true
                            )
                        }
                        setEffect { Effect.ShowToast(result.message.message ?: "") }
                    }

                    ResultReceiver.Loading -> {
                        setState {
                            copy(
                                exchangeRateIsLoading = true,
                                showError = false
                            )
                        }
                    }

                    is ResultReceiver.Success -> {
                        setState {
                            copy(
                                exchangeRateIsLoading = false,
                                exchangeRate = result.data
                            )
                        }
                    }
                }
            }
        }
    }

    private fun getCurrency() {
        viewModelScope.launch {
            getCurrencyRateUseCase.invoke(true)
        }
    }

    private fun changeSellAmount(amount: String) {
        val sellExchangeModel = uiState.value.sellExchange
        val receiveExchangeModel = uiState.value.receiveExchange

        val receiveCurrency =
            uiState.value.exchangeRate?.currencies?.firstOrNull { it.currency == receiveExchangeModel.currency }
        val selectedBalance =
            uiState.value.balance.firstOrNull { it.currency == sellExchangeModel.currency }

        try {
            val sellInputAmount = amount.toDouble()

            if (selectedBalance != null && receiveCurrency != null) {
                val sellTextValue: String
                val sellAmount: Double

                if (sellInputAmount > selectedBalance.amount) {
                    sellTextValue = formatPrice(selectedBalance.amount)
                    sellAmount = selectedBalance.amount
                } else {
                    sellTextValue = amount
                    sellAmount = sellInputAmount
                }

                val receivingAmount = if (selectedBalance.currency == EUR) {
                    sellAmount * receiveCurrency.rate
                } else {
                    val sellCurrencyRate =
                        uiState.value.exchangeRate?.currencies?.first { it.currency == selectedBalance.currency }

                    val amountInEuro = sellAmount / (sellCurrencyRate?.rate ?: 0.0)
                    amountInEuro * receiveCurrency.rate
                }

                setState {
                    copy(
                        fee = calculateFee(
                            sellAmount,
                            receivingAmount,
                            receiveExchangeModel.currency
                        ),
                        showExchangeButton = true,
                        sellExchange = sellExchange.copy(amount = sellTextValue),
                        receiveExchange = receiveExchange.copy(
                            amount = formatPrice(receivingAmount)
                        )
                    )
                }
            } else setEffect { Effect.ShowToast("Can't calculate") }
        } catch (e: NumberFormatException) {
            setState {
                copy(
                    showExchangeButton = false,
                    sellExchange = sellExchange.copy(amount = ""),
                    receiveExchange = receiveExchange.copy(
                        amount = "0"
                    )
                )
            }
            if (amount.isNotEmpty()) setEffect { Effect.ShowToast("Invalid number format") }
        }
    }

    private fun exchangeCurrency() {
        viewModelScope.launch {
            val receiveExchange = uiState.value.receiveExchange
            val sellExchange = uiState.value.sellExchange
            val balance = uiState.value.balance
            val fee = uiState.value.fee

            try {
                val sellInputAmount = sellExchange.amount.toDouble()
                val receiveOutputAmount = receiveExchange.amount.toDouble() - fee.feeAmount

                if (!validateCurrency(sellInputAmount)) return@launch

                setTransactionCountUseCase.invoke(uiState.value.transactionCount + 1)

                val sellCurrency = async {
                    val updateBalance = balance.first { it.currency == sellExchange.currency }
                    val doubleCheckSellAmount =
                        if (sellInputAmount > updateBalance.amount) updateBalance.amount
                        else sellInputAmount


                    updateOrInsertBalanceUseCase.invoke(
                        amount = updateBalance.amount - doubleCheckSellAmount,
                        currency = updateBalance.currency
                    )
                }

                val receiveCurrency = async {
                    val updateBalance =
                        balance.firstOrNull { it.currency == receiveExchange.currency }
                    val amount = updateBalance?.let { it.amount + receiveOutputAmount }
                        ?: receiveOutputAmount

                    updateOrInsertBalanceUseCase.invoke(
                        amount = amount,
                        currency = receiveExchange.currency
                    )
                }

                sellCurrency.await()
                receiveCurrency.await()

                launch {
                    setState {
                        copy(
                            showCheckoutDialog = false,
                            showExchangeButton = false,
                            receiveExchange = receiveExchange.copy(
                                amount = "0"
                            ),
                            sellExchange = sellExchange.copy(
                                amount = ""
                            )
                        )
                    }
                    delay(100)
                }.invokeOnCompletion {
                    setEffect { Effect.CloseKeyboard }
                }
            } catch (e: NumberFormatException) {
                setEffect { Effect.ShowToast("Invalid number format") }
            }
        }
    }

    private fun changeConvertorCurrency(currency: Currency, exchangeType: ExchangeType) {
        when (exchangeType) {
            ExchangeType.SELL -> {
                setState {
                    copy(
                        sellExchange = sellExchange.copy(currency = currency.currency)
                    )
                }

                val position =
                    uiState.value.balance.indexOfFirst { it.currency == currency.currency }
                setEffect { Effect.ScrollToCard(position) }
            }

            ExchangeType.RECEIVE -> {
                setState {
                    copy(
                        receiveExchange = receiveExchange.copy(currency = currency.currency)
                    )
                }
            }
        }

        changeSellAmount(uiState.value.sellExchange.amount)
        setEffect { Effect.CloseBottomSheet }
    }

    private fun manageTransactionCount() {
        viewModelScope.launch {
            val count = getTransactionCountUseCase()

            setState {
                copy(transactionCount = count)
            }

            when {
                count < 5 -> setState {
                    copy(
                        fee = fee.copy(
                            isFree = true,
                            feeAmount = 0.0,
                            currency = "",
                            freeTransactionCount = 5 - count
                        )
                    )
                }

                count % 10 == 0 -> setState {
                    copy(
                        fee = fee.copy(
                            isFree = true,
                            feeAmount = 0.0,
                            currency = "",
                            freeTransactionCount = 1
                        )
                    )
                }

                else -> setState {
                    copy(
                        fee = fee.copy(
                            isFree = false,
                            freeTransactionCount = 0
                        )
                    )
                }
            }
        }
    }

    private fun validateCurrency(sellAmount: Double): Boolean {
        val receiveExchange = uiState.value.receiveExchange
        val sellExchange = uiState.value.sellExchange

        if (sellAmount <= 0) {
            setEffect { Effect.ShowToast("Sell amount can't be 0") }
            return false
        }

        if (receiveExchange.currency == sellExchange.currency) {
            setEffect { Effect.ShowToast("You can't buy same currency") }
            return false
        }

        return true
    }

    private fun calculateFee(sell: Double, receive: Double, currency: String): HomeContract.Fee {
        val fee = uiState.value.fee
        return if (fee.isFree) fee.copy() else {
            val feePercent = if (sell >= 1000) 0.5 else 0.7
            fee.copy(
                feeAmount = (feePercent / 100) * receive,
                currency = currency
            )
        }
    }
}
