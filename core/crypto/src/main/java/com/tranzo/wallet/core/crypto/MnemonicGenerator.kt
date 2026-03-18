package com.tranzo.wallet.core.crypto

import org.web3j.crypto.MnemonicUtils
import java.security.SecureRandom

object MnemonicGenerator {

    private const val ENTROPY_BITS_128 = 16 // 128 bits = 16 bytes = 12-word mnemonic

    fun generateMnemonic(): String {
        val entropy = ByteArray(ENTROPY_BITS_128)
        SecureRandom().nextBytes(entropy)
        return MnemonicUtils.generateMnemonic(entropy)
    }

    fun validateMnemonic(mnemonic: String): Boolean {
        return MnemonicUtils.validateMnemonic(mnemonic)
    }

    fun mnemonicToSeed(mnemonic: String, passphrase: String = ""): ByteArray {
        return MnemonicUtils.generateSeed(mnemonic, passphrase)
    }
}
