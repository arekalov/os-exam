package com.arekalov.osexam.presentation.blocks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arekalov.osexam.domain.model.TICKET_BLOCKS
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class BlocksViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(BlocksState())
    val state: StateFlow<BlocksState> = _state

    private val _effect = Channel<BlocksEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    init {
        _state.update { it.copy(blocks = TICKET_BLOCKS) }
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
}
