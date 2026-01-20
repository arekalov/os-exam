package com.arekalov.osexam.presentation.blocktickets

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arekalov.osexam.domain.model.TICKET_BLOCKS
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
class BlockTicketsViewModel @Inject constructor(
    private val getTicketListUseCase: GetTicketListUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val blockId: Int = savedStateHandle.get<String>("blockId")?.toIntOrNull() ?: 1
    
    private val _state = MutableStateFlow(BlockTicketsState(blockId = blockId))
    val state: StateFlow<BlockTicketsState> = _state

    private val _effect = Channel<BlockTicketsEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    init {
        load()
    }

    fun onIntent(intent: BlockTicketsIntent) {
        when (intent) {
            is BlockTicketsIntent.TicketClicked -> {
                viewModelScope.launch {
                    _effect.send(BlockTicketsEffect.NavigateToTicket(intent.number))
                }
            }
        }
    }

    private fun load() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            
            val block = TICKET_BLOCKS.find { it.id == blockId }
            if (block == null) {
                _state.update { it.copy(isLoading = false, error = "Блок не найден") }
                return@launch
            }
            
            runCatching { getTicketListUseCase() }
                .onSuccess { allTickets ->
                    val blockTickets = allTickets.filter { ticket ->
                        ticket.number in block.ticketNumbers
                    }
                    _state.update { 
                        it.copy(
                            isLoading = false, 
                            blockTitle = block.title,
                            tickets = blockTickets
                        ) 
                    }
                }
                .onFailure { error ->
                    _state.update { it.copy(isLoading = false, error = error.message) }
                }
        }
    }
}
