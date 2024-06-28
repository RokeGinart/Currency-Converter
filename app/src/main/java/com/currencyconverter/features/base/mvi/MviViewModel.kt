package com.currencyconverter.features.base.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.currencyconverter.BuildConfig
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.coroutines.CancellationException

abstract class MviViewModel<Event : UiEvent, State : UiState, Effect : UiEffect> : ViewModel() {

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        if (throwable is CancellationException) {
            throw throwable
        } else {
            handleCoroutineException(throwable)
        }
    }

    protected var coroutineScope = viewModelScope + coroutineExceptionHandler

    private val initialState: State by lazy { createInitialState() }

    val currentState: State
        get() = uiState.value

    private val _uiState: MutableStateFlow<State> = MutableStateFlow(initialState)
    val uiState = _uiState.asStateFlow()

    private val _event: MutableSharedFlow<Event> = MutableSharedFlow()
    val event = _event.asSharedFlow()

    private val _effect: Channel<Effect> = Channel()
    val effect = _effect.receiveAsFlow()

    init {
        subscribeEvents()
    }

    abstract fun createInitialState(): State

    protected open fun scope() = coroutineScope

    /**
     * Start listening to Event
     */
    private fun subscribeEvents() {
        scope().launch {
            event.collect {
                handleEvent(it)
            }
        }
    }

    /**
     * Handle each event
     */
    abstract fun handleEvent(event: Event)

    /**
     * Set new Event
     */
    fun setEvent(event: Event) {
        val newEvent = event
        scope().launch { _event.emit(newEvent) }
    }

    /**
     * Set new Ui State
     */
    protected fun setState(reduce: State.() -> State) {
        val newState = currentState.reduce()
        _uiState.value = newState
    }

    /**
     * Set new Effect
     */
    protected fun setEffect(builder: () -> Effect) {
        val effectValue = builder()
        scope().launch { _effect.send(effectValue) }
    }

    protected open fun handleCoroutineException(throwable: Throwable) {
        if (BuildConfig.DEBUG) {
            throw throwable
        }
    }

    protected fun <T> Flow<T>.bind(
        action: (suspend (value: T) -> Unit)? = null
    ) = (action?.let { onEach(action) } ?: this).launchIn(coroutineScope)
}
