package com.tranzo.wallet.core.crypto

import org.web3j.crypto.Bip32ECKeyPair
import org.web3j.crypto.Credentials
import org.web3j.crypto.ECKeyPair
import org.web3j.crypto.MnemonicUtils

object KeyDerivation {

    // BIP44 path: m/44'/60'/0'/0/0
    private val BIP44_ETH_PATH = intArrayOf(
        44 or Bip32ECKeyPair.HARDENED_BIT,
        60 or Bip32ECKeyPair.HARDENED_BIT,
        0 or Bip32ECKeyPair.HARDENED_BIT,
        0,
        0
    )

    fun deriveEthereumKeyPair(seed: ByteArray): ECKeyPair {
        val masterKeyPair = Bip32ECKeyPair.generateKeyPair(seed)
        return Bip32ECKeyPair.deriveKeyPair(masterKeyPair, BIP44_ETH_PATH)
    }

    fun deriveEthereumCredentials(mnemonic: String): Credentials {
        val seed = MnemonicUtils.generateSeed(mnemonic, "")
        val keyPair = deriveEthereumKeyPair(seed)
        return Credentials.create(keyPair)
    }

    fun getEthereumAddress(credentials: Credentials): String {
        return credentials.address
    }
}
