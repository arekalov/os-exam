package com.arekalov.osexam.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arekalov.osexam.domain.model.TicketSummary
import com.arekalov.osexam.domain.usecase.GetTicketListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.math.min
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getTicketListUseCase: GetTicketListUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(SearchState())
    val state: StateFlow<SearchState> = _state

    private val _effect = Channel<SearchEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    private val _queryFlow = MutableStateFlow("")
    private var allTickets: List<TicketSummary> = emptyList()

    init {
        loadTickets()
        viewModelScope.launch {
            _queryFlow
                .debounce(200)
                .distinctUntilChanged()
                .collect { query -> applySearch(query) }
        }
    }

    fun onIntent(intent: SearchIntent) {
        when (intent) {
            is SearchIntent.QueryChanged -> {
                _state.update { it.copy(query = intent.query) }
                _queryFlow.value = intent.query
            }
            is SearchIntent.TicketClicked -> {
                viewModelScope.launch {
                    _effect.send(SearchEffect.NavigateToTicket(intent.number))
                }
            }
        }
    }

    private fun loadTickets() {
        viewModelScope.launch {
            runCatching { getTicketListUseCase() }
                .onSuccess { tickets ->
                    allTickets = tickets
                    _state.update { it.copy(isLoading = false, results = tickets) }
                }
                .onFailure {
                    _state.update { it.copy(isLoading = false) }
                }
        }
    }

    private fun applySearch(query: String) {
        val trimmed = query.trim()
        if (trimmed.isBlank()) {
            _state.update { it.copy(results = allTickets) }
            return
        }
        val queryNorm = trimmed.normalize()
        val tokens = queryNorm.split("\\s+".toRegex()).filter { it.isNotBlank() }

        val scored = allTickets.mapNotNull { ticket ->
            val titleNorm = ticket.title.normalize()
            val keywordsNorm = ticket.keywords.map { it.normalize() }
            var score = 0

            if (titleNorm == queryNorm) score += 100
            if (tokens.all { it in titleNorm }) score += 50
            tokens.forEach { token ->
                if (keywordsNorm.any { kw -> kw == token || kw.contains(token) || fuzzyMatch(token, kw) }) score += 30
            }
            tokens.forEach { token ->
                if (token in titleNorm) score += 10
            }

            if (score > 0) ticket to score else null
        }
        _state.update { it.copy(results = scored.sortedByDescending { (_, s) -> s }.map { (t, _) -> t }) }
    }

    // Lowercase + убирает спецсимволы, оставляет буквы, цифры и пробелы
    private fun String.normalize(): String =
        this.lowercase().replace("[^a-zа-яё0-9 ]".toRegex(), " ").trim()

    // Расстояние Левенштейна. Считаем fuzzy-совпадением если:
    // длина токена >= 4 и расстояние <= 1, или длина >= 6 и расстояние <= 2
    private fun fuzzyMatch(token: String, keyword: String): Boolean {
        if (token.length < 4) return false
        val maxDist = if (token.length >= 6) 2 else 1
        return levenshtein(token, keyword) <= maxDist
    }

    private fun levenshtein(a: String, b: String): Int {
        val dp = Array(a.length + 1) { IntArray(b.length + 1) }
        for (i in 0..a.length) dp[i][0] = i
        for (j in 0..b.length) dp[0][j] = j
        for (i in 1..a.length) {
            for (j in 1..b.length) {
                dp[i][j] = if (a[i - 1] == b[j - 1]) dp[i - 1][j - 1]
                else 1 + min(dp[i - 1][j - 1], min(dp[i - 1][j], dp[i][j - 1]))
            }
        }
        return dp[a.length][b.length]
    }
}
