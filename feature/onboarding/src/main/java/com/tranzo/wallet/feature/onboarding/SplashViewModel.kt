package com.tranzo.wallet.feature.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tranzo.wallet.core.keystore.SecureStorageManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface SplashState {
    data object Loading : SplashState
    data object NavigateToIntro : SplashState
    data object NavigateToMain : SplashState
}

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val secureStorageManager: SecureStorageManager
) : ViewModel() {

    private val _state = MutableStateFlow<SplashState>(SplashState.Loading)
    val state: StateFlow<SplashState> = _state.asStateFlow()

    init {
        checkWalletExists()
    }

    private fun checkWalletExists() {
        viewModelScope.launch {
            val hasWallet = secureStorageManager.hasWallet()
            _state.value = if (hasWallet) {
                SplashState.NavigateToMain
            } else {
                SplashState.NavigateToIntro
            }
        }
    }
}
