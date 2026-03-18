package com.tranzo.wallet.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tranzo.wallet.core.keystore.SecureStorageManager
import com.tranzo.wallet.domain.model.Network
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsState(
    val biometricEnabled: Boolean = false,
    val selectedCurrency: String = "USD",
    val selectedNetwork: Network = Network.ETHEREUM,
    val developerModeEnabled: Boolean = false,
    val testnetEnabled: Boolean = false,
    val appVersion: String = "1.0.0"
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val secureStorageManager: SecureStorageManager
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsState())
    val state: StateFlow<SettingsState> = _state.asStateFlow()

    fun toggleBiometric(enabled: Boolean) {
        _state.update { it.copy(biometricEnabled = enabled) }
    }

    fun selectCurrency(currency: String) {
        _state.update { it.copy(selectedCurrency = currency) }
    }

    fun selectNetwork(network: Network) {
        _state.update { it.copy(selectedNetwork = network) }
    }

    fun toggleDeveloperMode(enabled: Boolean) {
        _state.update { it.copy(developerModeEnabled = enabled) }
    }

    fun toggleTestnet(enabled: Boolean) {
        _state.update { it.copy(testnetEnabled = enabled) }
    }

    fun getSeedPhrase(walletId: String, onResult: (String?) -> Unit) {
        viewModelScope.launch {
            val mnemonic = secureStorageManager.getMnemonic(walletId)
            onResult(mnemonic)
        }
    }

    fun getPrivateKey(walletId: String, onResult: (String?) -> Unit) {
        viewModelScope.launch {
            val key = secureStorageManager.getPrivateKey(walletId)
            onResult(key)
        }
    }
}
