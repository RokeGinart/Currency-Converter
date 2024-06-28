package com.currencyconverter.features.screens.currency_picker.mvi

import androidx.lifecycle.viewModelScope
import com.currencyconverter.data.model.ui.Currency
import com.currencyconverter.data.model.ui.ExchangeType
import com.currencyconverter.domain.usecases.balance.SubscribeToUserBalanceUseCase
import com.currencyconverter.domain.usecases.currency.GetCurrencyRateUseCase
import com.currencyconverter.domain.usecases.currency.SubscribeToCurrencyRateUseCase
import com.currencyconverter.features.base.mvi.MviViewModel
import com.currencyconverter.features.screens.currency_picker.mvi.CurrencyPickerContract.*
import com.currencyconverter.utils.ResultReceiver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyPickerViewModel @Inject constructor(
    private val getCurrencyRateUseCase: GetCurrencyRateUseCase,
    private val subscribeToUserBalanceUseCase: SubscribeToUserBalanceUseCase,
    private val subscribeToCurrencyRateUseCase: SubscribeToCurrencyRateUseCase,
) : MviViewModel<Event, State, Effect>() {

    private var job: Job? = null

    init {
        subscribeToUserBalance()
        subscribeToCurrencyRate()
    }

    override fun createInitialState(): State = State()

    override fun handleEvent(event: Event) {
        when (event) {
            Event.OnCloseClick -> setEffect { Effect.Close }
            is Event.OnValueChanged -> searchCurrency(event.search)
            is Event.InitScreen -> initScreen(event.type)
            is Event.OnCurrencyClicked -> setEffect {
                Effect.ReturnResult(
                    event.currency,
                    uiState.value.type
                )
            }

            Event.OnRetryClick -> getCurrency()
        }
    }

    private fun getCurrency() {
        viewModelScope.launch {
            getCurrencyRateUseCase.invoke(true)
        }
    }

    private fun subscribeToCurrencyRate() {
        viewModelScope.launch {
            subscribeToCurrencyRateUseCase.invoke().collectLatest { result ->
                when (result) {
                    is ResultReceiver.Error -> {
                        setState {
                            copy(
                                isLoading = false,
                                showError = uiState.value.type == ExchangeType.RECEIVE
                            )
                        }
                    }

                    ResultReceiver.Loading -> {
                        setState {
                            copy(
                                isLoading = true,
                                showError = false
                            )
                        }
                    }

                    is ResultReceiver.Success -> {
                        setState {
                            copy(
                                lastUpdate = result.data.date,
                                currencyRate = result.data.currencies.toImmutableList(),
                                isLoading = false,
                                showError = false
                            )
                        }
                    }
                }
            }
        }
    }

    private fun subscribeToUserBalance() {
        viewModelScope.launch {
            subscribeToUserBalanceUseCase.invoke().collectLatest { result ->
                setState {
                    copy(
                        lastUpdate = "",
                        userBalance = result.map {
                            Currency(
                                currency = it.currency,
                                rate = it.amount
                            )
                        }.toImmutableList(),
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun initScreen(type: ExchangeType) {
        job?.cancel()
        setState {
            copy(
                search = "",
                isLoading = false,
                searchResult = null,
            )
        }

        setState {
            copy(
                currencies = when (type) {
                    ExchangeType.SELL -> userBalance
                    ExchangeType.RECEIVE -> currencyRate
                },
                type = type
            )
        }
    }

    private fun searchCurrency(search: String) {
        setState {
            copy(
                search = search,
            )
        }

        viewModelScope.launch(Dispatchers.IO) {
            job?.cancel()

            if (search.isEmpty()) {
                setState {
                    copy(
                        searchResult = null,
                    )
                }
            } else {
                job = launch {
                    setState {
                        copy(
                            searchResult = currencies
                                .filter { it.currency.lowercase().contains(search.lowercase()) }
                                .toImmutableList()
                        )
                    }
                }
            }
        }
    }
}
