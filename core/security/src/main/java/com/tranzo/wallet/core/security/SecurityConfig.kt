package com.tranzo.wallet.core.security

data class SecurityConfig(
    val pinLength: Int = 6,
    val biometricEnabled: Boolean = false,
    val sessionTimeoutMs: Long = 5 * 60 * 1000L,
    val maxPinAttempts: Int = 5
)
