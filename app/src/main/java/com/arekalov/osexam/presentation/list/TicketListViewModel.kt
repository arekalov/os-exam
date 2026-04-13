package com.arekalov.osexam.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arekalov.osexam.domain.model.TicketSummary
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
class TicketListViewModel @Inject constructor(
    private val getTicketListUseCase: GetTicketListUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(TicketListState())
    val state: StateFlow<TicketListState> = _state

    private val _effect = Channel<TicketListEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    init {
        load()
    }

    fun onIntent(intent: TicketListIntent) {
        when (intent) {
            TicketListIntent.Refresh -> load()
            is TicketListIntent.TicketClicked -> {
                viewModelScope.launch {
                    _effect.send(TicketListEffect.NavigateToTicket(intent.number))
                }
            }
            TicketListIntent.SearchClicked -> {
                viewModelScope.launch {
                    _effect.send(TicketListEffect.NavigateToSearch)
                }
            }
            TicketListIntent.ToggleSort -> {
                val newSort = if (_state.value.sortMode == SortMode.BY_NUMBER) SortMode.BY_TITLE else SortMode.BY_NUMBER
                _state.update { it.copy(sortMode = newSort, tickets = sortTickets(it.tickets, newSort)) }
            }
        }
    }

    private fun load() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            runCatching { getTicketListUseCase() }
                .onSuccess { tickets ->
                    val sorted = sortTickets(tickets, _state.value.sortMode)
                    _state.update { it.copy(isLoading = false, tickets = sorted) }
                }
                .onFailure { error ->
                    _state.update { it.copy(isLoading = false, error = error.message) }
                }
        }
    }

    private fun sortTickets(tickets: List<TicketSummary>, mode: SortMode): List<TicketSummary> =
        when (mode) {
            SortMode.BY_NUMBER -> tickets.sortedBy { it.number }
            SortMode.BY_TITLE -> tickets.sortedBy { it.title }
        }
}
