package com.tranzo.wallet.feature.wallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tranzo.wallet.core.keystore.SecureStorageManager
import com.tranzo.wallet.data.repository.TokenRepositoryImpl
import com.tranzo.wallet.data.repository.WalletRepositoryImpl
import com.tranzo.wallet.domain.model.Network
import com.tranzo.wallet.domain.model.Token
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

data class WalletHomeState(
    val isLoading: Boolean = true,
    val walletAddress: String = "",
    val totalBalance: BigDecimal = BigDecimal.ZERO,
    val tokens: List<Token> = emptyList(),
    val selectedNetwork: Network = Network.ETHEREUM,
    val error: String? = null
)

@HiltViewModel
class WalletHomeViewModel @Inject constructor(
    private val walletRepository: WalletRepositoryImpl,
    private val tokenRepository: TokenRepositoryImpl,
    private val secureStorageManager: SecureStorageManager
) : ViewModel() {

    private val _state = MutableStateFlow(WalletHomeState())
    val state: StateFlow<WalletHomeState> = _state.asStateFlow()

    init {
        loadWallet()
    }

    private fun loadWallet() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            try {
                val wallet = walletRepository.getActiveWallet()
                if (wallet != null) {
                    _state.update {
                        it.copy(walletAddress = wallet.address)
                    }
                    fetchTokens(wallet.address)
                } else {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = "No active wallet found"
                        )
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load wallet"
                    )
                }
            }
        }
    }

    private suspend fun fetchTokens(address: String) {
        try {
            val network = _state.value.selectedNetwork
            val tokens = tokenRepository.getTokens(address, network)
            val totalBalance = tokens.sumOf { token ->
                token.balance.multiply(BigDecimal.valueOf(token.priceUsd))
            }

            _state.update {
                it.copy(
                    isLoading = false,
                    tokens = tokens,
                    totalBalance = totalBalance
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

    fun selectNetwork(network: Network) {
        _state.update { it.copy(selectedNetwork = network) }
        val address = _state.value.walletAddress
        if (address.isNotEmpty()) {
            viewModelScope.launch {
                _state.update { it.copy(isLoading = true) }
                fetchTokens(address)
            }
        }
    }

    fun refresh() {
        loadWallet()
    }
}
