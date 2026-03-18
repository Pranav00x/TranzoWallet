package com.tranzo.wallet.feature.wallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tranzo.wallet.data.repository.TokenRepositoryImpl
import com.tranzo.wallet.data.repository.WalletRepositoryImpl
import com.tranzo.wallet.domain.model.Network
import com.tranzo.wallet.domain.model.SendRequest
import com.tranzo.wallet.domain.model.Token
import com.tranzo.wallet.domain.usecase.SendTokenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.BigInteger
import javax.inject.Inject

data class SendTokenState(
    val toAddress: String = "",
    val amount: String = "",
    val selectedToken: Token? = null,
    val availableTokens: List<Token> = emptyList(),
    val gasEstimate: BigInteger = BigInteger.ZERO,
    val gasPriceGwei: String = "20",
    val isSending: Boolean = false,
    val isEstimatingGas: Boolean = false,
    val txHash: String? = null,
    val error: String? = null,
    val walletAddress: String = "",
    val selectedNetwork: Network = Network.ETHEREUM,
    val showSuccessDialog: Boolean = false,
    val showFailureDialog: Boolean = false
)

@HiltViewModel
class SendTokenViewModel @Inject constructor(
    private val walletRepository: WalletRepositoryImpl,
    private val tokenRepository: TokenRepositoryImpl,
    private val sendTokenUseCase: SendTokenUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(SendTokenState())
    val state: StateFlow<SendTokenState> = _state.asStateFlow()

    init {
        loadTokens()
    }

    private fun loadTokens() {
        viewModelScope.launch {
            try {
                val wallet = walletRepository.getActiveWallet() ?: return@launch
                _state.update { it.copy(walletAddress = wallet.address) }

                val network = _state.value.selectedNetwork
                val tokens = tokenRepository.getTokens(wallet.address, network)

                _state.update {
                    it.copy(
                        availableTokens = tokens,
                        selectedToken = tokens.firstOrNull(),
                    )
                }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message) }
            }
        }
    }

    fun updateToAddress(address: String) {
        _state.update { it.copy(toAddress = address, error = null) }
    }

    fun updateAmount(amount: String) {
        _state.update { it.copy(amount = amount, error = null) }
    }

    fun selectToken(token: Token) {
        _state.update { it.copy(selectedToken = token, error = null) }
    }

    fun setMaxAmount() {
        val token = _state.value.selectedToken ?: return
        _state.update {
            it.copy(amount = token.balance.stripTrailingZeros().toPlainString())
        }
    }

    fun estimateGas() {
        viewModelScope.launch {
            _state.update { it.copy(isEstimatingGas = true) }

            try {
                // Standard ETH transfer gas limit
                val gasLimit = BigInteger.valueOf(21_000)
                _state.update {
                    it.copy(
                        gasEstimate = gasLimit,
                        isEstimatingGas = false,
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isEstimatingGas = false,
                        error = "Gas estimation failed: ${e.message}",
                    )
                }
            }
        }
    }

    fun send() {
        val currentState = _state.value
        val token = currentState.selectedToken ?: return

        if (currentState.toAddress.isBlank()) {
            _state.update { it.copy(error = "Recipient address is required") }
            return
        }

        if (currentState.amount.isBlank()) {
            _state.update { it.copy(error = "Amount is required") }
            return
        }

        val amount = try {
            BigDecimal(currentState.amount)
        } catch (e: NumberFormatException) {
            _state.update { it.copy(error = "Invalid amount") }
            return
        }

        if (amount <= BigDecimal.ZERO) {
            _state.update { it.copy(error = "Amount must be greater than zero") }
            return
        }

        if (amount > token.balance) {
            _state.update { it.copy(error = "Insufficient balance") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isSending = true, error = null) }

            try {
                val gasPrice = try {
                    BigInteger(currentState.gasPriceGwei).multiply(BigInteger.TEN.pow(9))
                } catch (e: Exception) {
                    null
                }

                val request = SendRequest(
                    toAddress = currentState.toAddress,
                    amount = amount,
                    token = token,
                    network = currentState.selectedNetwork,
                    gasPrice = gasPrice,
                    gasLimit = currentState.gasEstimate.takeIf { it > BigInteger.ZERO },
                )

                val wallet = walletRepository.getActiveWallet()
                if (wallet == null) {
                    _state.update {
                        it.copy(
                            isSending = false,
                            error = "No active wallet",
                            showFailureDialog = true,
                        )
                    }
                    return@launch
                }

                val result = sendTokenUseCase(request, wallet.id)
                result.fold(
                    onSuccess = { hash ->
                        _state.update {
                            it.copy(
                                isSending = false,
                                txHash = hash,
                                showSuccessDialog = true,
                            )
                        }
                    },
                    onFailure = { error ->
                        _state.update {
                            it.copy(
                                isSending = false,
                                error = error.message ?: "Transaction failed",
                                showFailureDialog = true,
                            )
                        }
                    }
                )
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isSending = false,
                        error = e.message ?: "Unexpected error",
                        showFailureDialog = true,
                    )
                }
            }
        }
    }

    fun dismissDialogs() {
        _state.update {
            it.copy(showSuccessDialog = false, showFailureDialog = false)
        }
    }

    fun resetState() {
        _state.update {
            SendTokenState(
                availableTokens = it.availableTokens,
                selectedToken = it.availableTokens.firstOrNull(),
                walletAddress = it.walletAddress,
                selectedNetwork = it.selectedNetwork,
            )
        }
    }
}
