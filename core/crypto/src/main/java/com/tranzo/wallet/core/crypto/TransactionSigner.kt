package com.tranzo.wallet.core.crypto

import org.web3j.abi.FunctionEncoder
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.Bool
import org.web3j.abi.datatypes.Function
import org.web3j.abi.datatypes.generated.Uint256
import org.web3j.crypto.Credentials
import org.web3j.crypto.RawTransaction
import org.web3j.crypto.TransactionEncoder
import java.math.BigInteger

class TransactionSigner {

    fun signTransaction(
        credentials: Credentials,
        to: String,
        value: BigInteger,
        gasPrice: BigInteger,
        gasLimit: BigInteger,
        nonce: BigInteger,
        chainId: Long,
        data: String = ""
    ): ByteArray {
        val rawTransaction = RawTransaction.createTransaction(
            nonce,
            gasPrice,
            gasLimit,
            to,
            value,
            data
        )
        return TransactionEncoder.signMessage(rawTransaction, chainId, credentials)
    }

    fun encodeErc20Transfer(toAddress: String, amount: BigInteger): String {
        val function = Function(
            "transfer",
            listOf(Address(toAddress), Uint256(amount)),
            listOf(object : TypeReference<Bool>() {})
        )
        return FunctionEncoder.encode(function)
    }
}
