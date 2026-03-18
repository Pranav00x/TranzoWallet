package com.tranzo.wallet.feature.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tranzo.wallet.domain.model.Wallet
import com.tranzo.wallet.domain.usecase.ImportWalletUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ImportWalletState(
    val mnemonic: String = "",
    val isValid: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val importedWallet: Wallet? = null
)

@HiltViewModel
class ImportWalletViewModel @Inject constructor(
    private val importWalletUseCase: ImportWalletUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ImportWalletState())
    val state: StateFlow<ImportWalletState> = _state.asStateFlow()

    fun onMnemonicChanged(value: String) {
        val wordCount = value.trim().split("\\s+".toRegex()).filter { it.isNotEmpty() }.size
        _state.update {
            it.copy(
                mnemonic = value,
                isValid = wordCount == 12,
                error = null
            )
        }
    }

    fun importWallet() {
        val currentState = _state.value
        if (!currentState.isValid || currentState.isLoading) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            importWalletUseCase(currentState.mnemonic.trim(), "Imported Wallet")
                .onSuccess { wallet ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            importedWallet = wallet
                        )
                    }
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = error.message ?: "Failed to import wallet"
                        )
                    }
                }
        }
    }
}
