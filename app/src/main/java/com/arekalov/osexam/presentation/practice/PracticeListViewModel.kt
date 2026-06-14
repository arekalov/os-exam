package com.arekalov.osexam.presentation.practice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arekalov.osexam.domain.model.TicketSource
import com.arekalov.osexam.domain.usecase.GetTicketListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class PracticeListViewModel @Inject constructor(
    private val getTicketListUseCase: GetTicketListUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(PracticeListState())
    val state: StateFlow<PracticeListState> = _state

    private val _effect = Channel<PracticeListEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    init {
        load()
    }

    fun onIntent(intent: PracticeListIntent) {
        when (intent) {
            PracticeListIntent.Refresh -> load()
            is PracticeListIntent.TicketClicked -> {
                viewModelScope.launch {
                    _effect.send(PracticeListEffect.NavigateToTicket(intent.number))
                }
            }
        }
    }

    private fun load() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            runCatching { getTicketListUseCase(TicketSource.PRACTICE) }
                .onSuccess { tickets ->
                    _state.update { it.copy(isLoading = false, tickets = tickets) }
                }
                .onFailure { error ->
                    _state.update { it.copy(isLoading = false, error = error.message) }
                }
        }
    }
}
