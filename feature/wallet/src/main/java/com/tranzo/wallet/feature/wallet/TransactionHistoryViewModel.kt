package com.tranzo.wallet.feature.wallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tranzo.wallet.data.repository.TransactionRepositoryImpl
import com.tranzo.wallet.data.repository.WalletRepositoryImpl
import com.tranzo.wallet.domain.model.Network
import com.tranzo.wallet.domain.model.Transaction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TransactionHistoryState(
    val isLoading: Boolean = true,
    val transactions: List<Transaction> = emptyList(),
    val walletAddress: String = "",
    val error: String? = null
)

@HiltViewModel
class TransactionHistoryViewModel @Inject constructor(
    private val transactionRepository: TransactionRepositoryImpl,
    private val walletRepository: WalletRepositoryImpl
) : ViewModel() {

    private val _state = MutableStateFlow(TransactionHistoryState())
    val state: StateFlow<TransactionHistoryState> = _state.asStateFlow()

    init {
        loadTransactions()
    }

    private fun loadTransactions() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            try {
                val wallet = walletRepository.getActiveWallet()
                if (wallet == null) {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = "No active wallet found",
                        )
                    }
                    return@launch
                }

                _state.update { it.copy(walletAddress = wallet.address) }

                val transactions = transactionRepository.getTransactions(
                    address = wallet.address,
                    network = Network.ETHEREUM,
                )

                _state.update {
                    it.copy(
                        isLoading = false,
                        transactions = transactions.sortedByDescending { tx -> tx.timestamp },
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load transactions",
                    )
                }
            }
        }
    }

    fun refresh() {
        loadTransactions()
    }
}
