package com.tranzo.wallet.feature.tokens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tranzo.wallet.data.repository.TokenRepositoryImpl
import com.tranzo.wallet.domain.model.Network
import com.tranzo.wallet.domain.model.Token
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TokenListState(
    val isLoading: Boolean = true,
    val tokens: List<Token> = emptyList(),
    val filteredTokens: List<Token> = emptyList(),
    val searchQuery: String = "",
    val error: String? = null
)

@HiltViewModel
class TokenViewModel @Inject constructor(
    private val tokenRepository: TokenRepositoryImpl
) : ViewModel() {

    private val _state = MutableStateFlow(TokenListState())
    val state: StateFlow<TokenListState> = _state.asStateFlow()

    init {
        loadTokens()
    }

    private fun loadTokens() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            try {
                // Use a placeholder address; in production this comes from the active wallet
                val tokens = tokenRepository.getTokens("", Network.ETHEREUM)

                _state.update {
                    it.copy(
                        isLoading = false,
                        tokens = tokens,
                        filteredTokens = tokens
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load tokens"
                    )
                }
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _state.update { currentState ->
            val filtered = if (query.isBlank()) {
                currentState.tokens
            } else {
                currentState.tokens.filter { token ->
                    token.name.contains(query, ignoreCase = true) ||
                            token.symbol.contains(query, ignoreCase = true)
                }
            }
            currentState.copy(
                searchQuery = query,
                filteredTokens = filtered
            )
        }
    }

    fun refresh() {
        loadTokens()
    }
}
