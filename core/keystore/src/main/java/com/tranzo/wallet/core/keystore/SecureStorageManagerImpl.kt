package com.tranzo.wallet.core.keystore

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import java.security.MessageDigest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SecureStorageManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : SecureStorageManager {

    private companion object {
        const val PREFS_FILE_NAME = "tranzo_secure_prefs"
        const val KEY_MNEMONIC_PREFIX = "mnemonic_"
        const val KEY_PRIVATE_KEY_PREFIX = "private_key_"
        const val KEY_PIN_HASH = "pin_hash"
        const val KEY_HAS_WALLET = "has_wallet"
    }

    private val masterKey: MasterKey by lazy {
        MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
    }

    private val encryptedPrefs: SharedPreferences by lazy {
        EncryptedSharedPreferences.create(
            context,
            PREFS_FILE_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    override suspend fun storeMnemonic(walletId: String, mnemonic: String) {
        encryptedPrefs.edit()
            .putString("$KEY_MNEMONIC_PREFIX$walletId", mnemonic)
            .putBoolean(KEY_HAS_WALLET, true)
            .apply()
    }

    override suspend fun getMnemonic(walletId: String): String? {
        return encryptedPrefs.getString("$KEY_MNEMONIC_PREFIX$walletId", null)
    }

    override suspend fun storePrivateKey(walletId: String, key: String) {
        encryptedPrefs.edit()
            .putString("$KEY_PRIVATE_KEY_PREFIX$walletId", key)
            .apply()
    }

    override suspend fun getPrivateKey(walletId: String): String? {
        return encryptedPrefs.getString("$KEY_PRIVATE_KEY_PREFIX$walletId", null)
    }

    override suspend fun storePin(pin: String) {
        val hashedPin = hashPin(pin)
        encryptedPrefs.edit()
            .putString(KEY_PIN_HASH, hashedPin)
            .apply()
    }

    override suspend fun verifyPin(pin: String): Boolean {
        val storedHash = encryptedPrefs.getString(KEY_PIN_HASH, null) ?: return false
        return hashPin(pin) == storedHash
    }

    override suspend fun clearAll() {
        encryptedPrefs.edit().clear().apply()
    }

    override suspend fun hasWallet(): Boolean {
        return encryptedPrefs.getBoolean(KEY_HAS_WALLET, false)
    }

    private fun hashPin(pin: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(pin.toByteArray(Charsets.UTF_8))
        return hashBytes.joinToString("") { "%02x".format(it) }
    }
}
