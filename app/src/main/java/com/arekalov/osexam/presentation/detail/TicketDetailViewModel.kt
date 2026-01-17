package com.arekalov.osexam.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arekalov.osexam.domain.usecase.GetTicketUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class TicketDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getTicketUseCase: GetTicketUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(TicketDetailState())
    val state: StateFlow<TicketDetailState> = _state

    private val _effect = Channel<TicketDetailEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    private val ticketNumber: Int? =
        savedStateHandle.get<String>(ARG_NUMBER)?.toIntOrNull()

    init {
        load()
    }

    fun onIntent(intent: TicketDetailIntent) {
        when (intent) {
            is TicketDetailIntent.ImageClicked -> {
                viewModelScope.launch { _effect.send(TicketDetailEffect.NavigateToImage(intent.path)) }
            }
        }
    }

    private fun load() {
        viewModelScope.launch {
            val number = ticketNumber
            if (number == null) {
                _state.update { it.copy(isLoading = false, error = "Не передан номер билета") }
                return@launch
            }
            _state.update { it.copy(isLoading = true, error = null) }
            runCatching { getTicketUseCase(number) }
                .onSuccess { ticket ->
                    if (ticket == null) {
                        _state.update { it.copy(isLoading = false, error = "Билет не найден") }
                    } else {
                        _state.update { it.copy(isLoading = false, ticket = ticket) }
                    }
                }
                .onFailure { error ->
                    _state.update { it.copy(isLoading = false, error = error.message) }
                }
        }
    }

    companion object {
        const val ARG_NUMBER = "number"
    }
}
