package com.musixmatch.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

abstract class BaseViewModel<Event : UiEvent, State : UiState, Effect : UiEffect> : ViewModel() {

    // Create Initial State of View
    private val initialState : State by lazy { createInitialState() }
    abstract fun createInitialState() : State

    // Get Current State
    val currentState: State
        get() = uiState.value

    // Like a LiveData but with initial value (initial state is always present).
    private val _uiState : MutableStateFlow<State> = MutableStateFlow(initialState)
    val uiState = _uiState.asStateFlow()

    // Events are broadcast to many (zero or more) subscribers.
    // If there are no subscribers, the event is dropped.
    private val _event : MutableSharedFlow<Event> = MutableSharedFlow()
    val event = _event.asSharedFlow()

    // Each effect is broadcast to a single subscriber.
    // If there is no subscriber, the channel suspends, waiting for a subscriber.
    // Channels are hot, so we don't show side effect again when orientation changes
    // or UI becomes visible again.
    private val _effect : Channel<Effect> = Channel()
    val effect = _effect.receiveAsFlow()


    init {
        subscribeEvents()
    }

    /**
     * Start listening to Event
     */
    private fun subscribeEvents() {
        viewModelScope.launch {
            event.collect {
                handleEvent(it)
            }
        }
    }

    /**
     * Handle each event
     */
    abstract fun handleEvent(event : Event)

    fun setEvent(event : Event) {
        val newEvent = event
        viewModelScope.launch { _event.emit(newEvent) }
    }


    protected fun setState(reduce: State.() -> State) {
        val newState = currentState.reduce()
        _uiState.value = newState
    }

    protected fun setEffect(builder: () -> Effect) {
        val effectValue = builder()
        viewModelScope.launch { _effect.send(effectValue) }
    }

}