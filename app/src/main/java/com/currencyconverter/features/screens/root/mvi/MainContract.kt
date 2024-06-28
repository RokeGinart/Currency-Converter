package com.currencyconverter.features.screens.root.mvi

import com.currencyconverter.features.base.mvi.UiEffect
import com.currencyconverter.features.base.mvi.UiEvent
import com.currencyconverter.features.base.mvi.UiState


class MainContract {
    sealed class Event : UiEvent {

    }

    data class State(
        val isLoading: Boolean = false,
    ) : UiState

    sealed class Effect : UiEffect {

    }
}
