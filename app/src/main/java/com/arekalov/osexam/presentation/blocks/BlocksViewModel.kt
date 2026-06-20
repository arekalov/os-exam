package com.arekalov.osexam.presentation.blocks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arekalov.osexam.domain.usecase.GetTicketBlocksUseCase
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
class BlocksViewModel @Inject constructor(
    private val getTicketBlocksUseCase: GetTicketBlocksUseCase,
    private val getTicketListUseCase: GetTicketListUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(BlocksState())
    val state: StateFlow<BlocksState> = _state

    private val _effect = Channel<BlocksEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    init {
        load()
    }

    fun onIntent(intent: BlocksIntent) {
        when (intent) {
            is BlocksIntent.BlockClicked -> {
                viewModelScope.launch {
                    _effect.send(BlocksEffect.NavigateToBlock(intent.blockId))
                }
            }
        }
    }

    private fun load() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            runCatching {
                val blocks = getTicketBlocksUseCase()
                val tickets = getTicketListUseCase()
                BlockTitleFormatter.enrichBlocks(blocks, tickets)
            }
                .onSuccess { blocks ->
                    _state.update { it.copy(isLoading = false, blocks = blocks) }
                }
                .onFailure { error ->
                    _state.update { it.copy(isLoading = false, error = error.message) }
                }
        }
    }
}
