package com.tranzo.wallet.feature.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tranzo.wallet.core.keystore.SecureStorageManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class PinPhase {
    Enter,
    Confirm
}

data class PinSetupState(
    val pin: String = "",
    val confirmPin: String = "",
    val phase: PinPhase = PinPhase.Enter,
    val error: String? = null,
    val isComplete: Boolean = false
)

@HiltViewModel
class PinSetupViewModel @Inject constructor(
    private val secureStorageManager: SecureStorageManager
) : ViewModel() {

    private val _state = MutableStateFlow(PinSetupState())
    val state: StateFlow<PinSetupState> = _state.asStateFlow()

    fun onDigitEntered(digit: String) {
        val currentState = _state.value
        if (currentState.isComplete) return

        when (currentState.phase) {
            PinPhase.Enter -> {
                if (currentState.pin.length < 6) {
                    val newPin = currentState.pin + digit
                    _state.update { it.copy(pin = newPin, error = null) }

                    if (newPin.length == 6) {
                        _state.update { it.copy(phase = PinPhase.Confirm) }
                    }
                }
            }

            PinPhase.Confirm -> {
                if (currentState.confirmPin.length < 6) {
                    val newConfirmPin = currentState.confirmPin + digit
                    _state.update { it.copy(confirmPin = newConfirmPin, error = null) }

                    if (newConfirmPin.length == 6) {
                        validateAndStorePin(currentState.pin, newConfirmPin)
                    }
                }
            }
        }
    }

    fun onBackspace() {
        val currentState = _state.value
        if (currentState.isComplete) return

        when (currentState.phase) {
            PinPhase.Enter -> {
                if (currentState.pin.isNotEmpty()) {
                    _state.update { it.copy(pin = it.pin.dropLast(1)) }
                }
            }

            PinPhase.Confirm -> {
                if (currentState.confirmPin.isNotEmpty()) {
                    _state.update { it.copy(confirmPin = it.confirmPin.dropLast(1)) }
                }
            }
        }
    }

    private fun validateAndStorePin(pin: String, confirmPin: String) {
        if (pin == confirmPin) {
            viewModelScope.launch {
                secureStorageManager.storePin(pin)
                _state.update { it.copy(isComplete = true, error = null) }
            }
        } else {
            _state.update {
                PinSetupState(
                    error = "PINs do not match. Please try again."
                )
            }
        }
    }
}
