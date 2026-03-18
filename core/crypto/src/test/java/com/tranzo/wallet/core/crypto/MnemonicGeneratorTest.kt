package com.tranzo.wallet.core.crypto

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class MnemonicGeneratorTest {

    @Test
    fun `generateMnemonic produces 12 words`() {
        val mnemonic = MnemonicGenerator.generateMnemonic()
        val words = mnemonic.trim().split(" ")
        assertEquals(12, words.size)
    }

    @Test
    fun `validateMnemonic returns true for valid mnemonic`() {
        val mnemonic = MnemonicGenerator.generateMnemonic()
        assertTrue(MnemonicGenerator.validateMnemonic(mnemonic))
    }

    @Test
    fun `validateMnemonic returns false for invalid mnemonic`() {
        val invalidMnemonic = "invalid words that are not a real mnemonic phrase at all here now"
        assertFalse(MnemonicGenerator.validateMnemonic(invalidMnemonic))
    }

    @Test
    fun `mnemonicToSeed is deterministic for same mnemonic`() {
        val mnemonic = MnemonicGenerator.generateMnemonic()
        val seed1 = MnemonicGenerator.mnemonicToSeed(mnemonic)
        val seed2 = MnemonicGenerator.mnemonicToSeed(mnemonic)

        assertNotNull(seed1)
        assertNotNull(seed2)
        assertTrue(seed1.contentEquals(seed2))
    }
}
