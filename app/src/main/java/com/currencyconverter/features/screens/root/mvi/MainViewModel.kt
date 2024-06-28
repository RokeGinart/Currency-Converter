package com.currencyconverter.features.screens.root.mvi

import androidx.lifecycle.viewModelScope
import com.currencyconverter.domain.usecases.currency.GetCurrencyRateUseCase
import com.currencyconverter.features.base.mvi.MviViewModel
import com.currencyconverter.features.screens.root.mvi.MainContract.Effect
import com.currencyconverter.features.screens.root.mvi.MainContract.Event
import com.currencyconverter.features.screens.root.mvi.MainContract.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getCurrencyRateUseCase: GetCurrencyRateUseCase,
) : MviViewModel<Event, State, Effect>() {

    init {
        getCurrencyRate()
    }

    override fun createInitialState(): State = State()

    override fun handleEvent(event: Event) {

    }

    private fun getCurrencyRate() {
        viewModelScope.launch { getCurrencyRateUseCase.invoke(true) }
    }
}
