package com.tranzo.wallet.feature.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tranzo.wallet.core.keystore.SecureStorageManager
import com.tranzo.wallet.domain.model.Wallet
import com.tranzo.wallet.domain.usecase.CreateWalletUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface CreateWalletState {
    data object Idle : CreateWalletState
    data object Loading : CreateWalletState
    data class Success(val wallet: Wallet, val mnemonic: String) : CreateWalletState
    data class Error(val message: String) : CreateWalletState
}

@HiltViewModel
class CreateWalletViewModel @Inject constructor(
    private val createWalletUseCase: CreateWalletUseCase,
    private val secureStorageManager: SecureStorageManager
) : ViewModel() {

    private val _state = MutableStateFlow<CreateWalletState>(CreateWalletState.Idle)
    val state: StateFlow<CreateWalletState> = _state.asStateFlow()

    fun createWallet() {
        if (_state.value is CreateWalletState.Loading) return

        viewModelScope.launch {
            _state.value = CreateWalletState.Loading
            createWalletUseCase("My Wallet")
                .onSuccess { wallet ->
                    val mnemonic = secureStorageManager.getMnemonic(wallet.id) ?: ""
                    _state.value = CreateWalletState.Success(
                        wallet = wallet,
                        mnemonic = mnemonic
                    )
                }
                .onFailure { error ->
                    _state.value = CreateWalletState.Error(
                        message = error.message ?: "Failed to create wallet"
                    )
                }
        }
    }
}
