package com.tranzo.wallet.feature.wallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tranzo.wallet.core.keystore.SecureStorageManager
import com.tranzo.wallet.data.network.BlockchainService
import com.tranzo.wallet.data.repository.WalletRepositoryImpl
import com.tranzo.wallet.domain.model.Network
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.web3j.crypto.Credentials
import org.web3j.crypto.RawTransaction
import org.web3j.crypto.TransactionEncoder
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.utils.Numeric
import java.math.BigInteger
import javax.inject.Inject

data class DAppBrowserState(
    val url: String = "https://app.uniswap.org",
    val currentUrl: String = "https://app.uniswap.org",
    val pageTitle: String = "Browser",
    val isLoading: Boolean = false,
    val canGoBack: Boolean = false,
    val canGoForward: Boolean = false,
    val walletAddress: String = "",
    val selectedNetwork: Network = Network.ETHEREUM,
    val isConnected: Boolean = false,
    val error: String? = null,
)

/**
 * One-shot events the ViewModel pushes to the WebView layer.
 */
sealed class BrowserEvent {
    /** Tell the WebView to execute arbitrary JS. */
    data class ExecuteJs(val script: String) : BrowserEvent()
}

@HiltViewModel
class DAppBrowserViewModel @Inject constructor(
    private val walletRepository: WalletRepositoryImpl,
    private val blockchainService: BlockchainService,
    private val secureStorageManager: SecureStorageManager,
) : ViewModel() {

    private val _state = MutableStateFlow(DAppBrowserState())
    val state: StateFlow<DAppBrowserState> = _state.asStateFlow()

    private val _events = MutableSharedFlow<BrowserEvent>(extraBufferCapacity = 16)
    val events: SharedFlow<BrowserEvent> = _events.asSharedFlow()

    init {
        loadWalletAddress()
    }

    private fun loadWalletAddress() {
        viewModelScope.launch {
            try {
                val wallet = walletRepository.getActiveWallet()
                if (wallet != null) {
                    _state.update { it.copy(walletAddress = wallet.address) }
                }
            } catch (_: Exception) { }
        }
    }

    // ─── Navigation helpers ────────────────────────────────────────────
    fun onUrlSubmit(url: String) {
        val finalUrl = if (!url.startsWith("http://") && !url.startsWith("https://")) {
            if (url.contains(".") && !url.contains(" ")) {
                "https://$url"
            } else {
                "https://www.google.com/search?q=${url.replace(" ", "+")}"
            }
        } else {
            url
        }
        _state.update { it.copy(url = finalUrl) }
    }

    fun onPageStarted(url: String) {
        _state.update { it.copy(currentUrl = url, isLoading = true) }
    }

    fun onPageFinished(url: String) {
        _state.update { it.copy(currentUrl = url, isLoading = false) }
    }

    fun onTitleChanged(title: String) {
        _state.update { it.copy(pageTitle = title.ifEmpty { "Browser" }) }
    }

    fun updateNavState(canGoBack: Boolean, canGoForward: Boolean) {
        _state.update { it.copy(canGoBack = canGoBack, canGoForward = canGoForward) }
    }

    // ─── Network switching ─────────────────────────────────────────────
    fun selectNetwork(network: Network) {
        _state.update { it.copy(selectedNetwork = network) }
        if (_state.value.isConnected) {
            notifyChainChanged(network)
        }
    }

    private fun notifyChainChanged(network: Network) {
        val chainIdHex = "0x" + network.chainId.toString(16)
        val js = """
            if (window.ethereum && window.ethereum._tranzoEmit) {
                window.ethereum._tranzoEmit('chainChanged', '$chainIdHex');
            }
        """.trimIndent()
        _events.tryEmit(BrowserEvent.ExecuteJs(js))
    }

    private fun notifyAccountsChanged(address: String) {
        val lower = address.lowercase()
        val js = """
            if (window.ethereum && window.ethereum._tranzoEmit) {
                window.ethereum._tranzoEmit('accountsChanged', ['$lower']);
            }
        """.trimIndent()
        _events.tryEmit(BrowserEvent.ExecuteJs(js))
    }

    // ─── EIP-1193 request handling ─────────────────────────────────────
    /**
     * Called from the JS bridge.  The [callbackId] is echoed back so the
     * injected provider can pair request → response.
     */
    fun handleProviderRequest(method: String, paramsJson: String, callbackId: String) {
        viewModelScope.launch {
            try {
                val result = processRpcMethod(method, paramsJson)
                sendResult(callbackId, result)
            } catch (e: Exception) {
                sendError(callbackId, 4001, e.message ?: "Unknown error")
            }
        }
    }

    private suspend fun processRpcMethod(method: String, paramsJson: String): String {
        val address = _state.value.walletAddress.lowercase()
        val network = _state.value.selectedNetwork

        return when (method) {
            "eth_requestAccounts", "eth_accounts" -> {
                if (address.isEmpty()) throw Exception("No wallet available")
                _state.update { it.copy(isConnected = true) }
                """["$address"]"""
            }

            "eth_chainId" -> {
                val hex = "0x" + network.chainId.toString(16)
                "\"$hex\""
            }

            "net_version" -> {
                "\"${network.chainId}\""
            }

            "eth_getBalance" -> {
                val balance = withContext(Dispatchers.IO) {
                    blockchainService.getBalance(address, network.rpcUrl)
                }
                "\"0x${balance.toString(16)}\""
            }

            "eth_blockNumber" -> {
                val blockNumber = withContext(Dispatchers.IO) {
                    val web3 = blockchainService.getWeb3(network.rpcUrl)
                    web3.ethBlockNumber().send().blockNumber
                }
                "\"0x${blockNumber.toString(16)}\""
            }

            "eth_estimateGas" -> {
                val params = parseTransactionParams(paramsJson)
                val gas = withContext(Dispatchers.IO) {
                    blockchainService.estimateGas(
                        from = params["from"] ?: address,
                        to = params["to"] ?: "",
                        value = parseBigInteger(params["value"]),
                        rpcUrl = network.rpcUrl,
                    )
                }
                "\"0x${gas.toString(16)}\""
            }

            "eth_gasPrice" -> {
                val gasPrice = withContext(Dispatchers.IO) {
                    blockchainService.getGasPrice(network.rpcUrl)
                }
                "\"0x${gasPrice.toString(16)}\""
            }

            "eth_sendTransaction" -> {
                val params = parseTransactionParams(paramsJson)
                val txHash = signAndSend(params, network)
                "\"$txHash\""
            }

            "personal_sign", "eth_sign" -> {
                val wallet = walletRepository.getActiveWallet()
                    ?: throw Exception("No wallet")
                val privateKey = secureStorageManager.getPrivateKey(wallet.id)
                    ?: throw Exception("No private key")
                val credentials = Credentials.create(privateKey)

                // Extract message from params
                val message = extractSignMessage(paramsJson)
                val sig = org.web3j.crypto.Sign.signPrefixedMessage(
                    message.toByteArray(Charsets.UTF_8),
                    credentials.ecKeyPair,
                )
                val sigHex = Numeric.toHexStringNoPrefix(sig.r) +
                        Numeric.toHexStringNoPrefix(sig.s) +
                        String.format("%02x", sig.v[0].toInt())
                "\"0x$sigHex\""
            }

            "wallet_switchEthereumChain" -> {
                val chainIdHex = extractChainIdFromParams(paramsJson)
                val chainId = java.lang.Long.decode(chainIdHex)
                val target = Network.DEFAULT_NETWORKS.find { it.chainId == chainId }
                    ?: throw Exception("Unsupported chain: $chainIdHex")
                selectNetwork(target)
                "null"
            }

            "wallet_addEthereumChain" -> {
                // For now just try to switch to it if we support it
                val chainIdHex = extractChainIdFromParams(paramsJson)
                val chainId = java.lang.Long.decode(chainIdHex)
                val target = Network.DEFAULT_NETWORKS.find { it.chainId == chainId }
                    ?: throw Exception("Unsupported chain: $chainIdHex")
                selectNetwork(target)
                "null"
            }

            "eth_call" -> {
                val params = parseTransactionParams(paramsJson)
                val result = withContext(Dispatchers.IO) {
                    val web3 = blockchainService.getWeb3(network.rpcUrl)
                    val tx = org.web3j.protocol.core.methods.request.Transaction.createEthCallTransaction(
                        params["from"] ?: address,
                        params["to"] ?: "",
                        params["data"] ?: "0x",
                    )
                    web3.ethCall(tx, DefaultBlockParameterName.LATEST).send().value
                }
                "\"${result ?: "0x"}\""
            }

            "eth_getCode" -> {
                val targetAddress = extractFirstParam(paramsJson)
                val code = withContext(Dispatchers.IO) {
                    val web3 = blockchainService.getWeb3(network.rpcUrl)
                    web3.ethGetCode(targetAddress, DefaultBlockParameterName.LATEST).send().code
                }
                "\"${code ?: "0x"}\""
            }

            "eth_getTransactionCount" -> {
                val nonce = withContext(Dispatchers.IO) {
                    blockchainService.getNonce(address, network.rpcUrl)
                }
                "\"0x${nonce.toString(16)}\""
            }

            else -> {
                // Forward unknown methods to the RPC
                val result = withContext(Dispatchers.IO) {
                    val web3 = blockchainService.getWeb3(network.rpcUrl)
                    // Use raw JSON-RPC call
                    throw Exception("Unsupported method: $method")
                }
                result
            }
        }
    }

    private suspend fun signAndSend(
        params: Map<String, String>,
        network: Network,
    ): String {
        val wallet = walletRepository.getActiveWallet()
            ?: throw Exception("No wallet")
        val privateKey = secureStorageManager.getPrivateKey(wallet.id)
            ?: throw Exception("No private key")

        val credentials = Credentials.create(privateKey)
        val from = params["from"] ?: _state.value.walletAddress
        val to = params["to"] ?: throw Exception("Missing 'to' address")
        val value = parseBigInteger(params["value"])
        val data = params["data"] ?: ""

        val nonce = blockchainService.getNonce(from, network.rpcUrl)
        val gasPrice = if (params.containsKey("gasPrice")) {
            parseBigInteger(params["gasPrice"])
        } else {
            blockchainService.getGasPrice(network.rpcUrl)
        }
        val gasLimit = if (params.containsKey("gas")) {
            parseBigInteger(params["gas"])
        } else {
            blockchainService.estimateGas(from, to, value, network.rpcUrl)
                .multiply(BigInteger.valueOf(12))
                .divide(BigInteger.TEN) // 1.2x safety margin
        }

        val rawTx = if (data.isNotEmpty() && data != "0x") {
            RawTransaction.createTransaction(nonce, gasPrice, gasLimit, to, value, data)
        } else {
            RawTransaction.createEtherTransaction(nonce, gasPrice, gasLimit, to, value)
        }

        val signedMessage = TransactionEncoder.signMessage(rawTx, network.chainId, credentials)
        val hexValue = Numeric.toHexString(signedMessage)

        return blockchainService.sendRawTransaction(hexValue, network.rpcUrl)
    }

    // ─── JS bridge helpers ─────────────────────────────────────────────
    private fun sendResult(callbackId: String, result: String) {
        val js = """
            if (window.ethereum && window.ethereum._tranzoCallback) {
                window.ethereum._tranzoCallback('$callbackId', null, $result);
            }
        """.trimIndent()
        _events.tryEmit(BrowserEvent.ExecuteJs(js))
    }

    private fun sendError(callbackId: String, code: Int, message: String) {
        val escapedMsg = message.replace("'", "\\'").replace("\n", " ")
        val js = """
            if (window.ethereum && window.ethereum._tranzoCallback) {
                window.ethereum._tranzoCallback('$callbackId', { code: $code, message: '$escapedMsg' }, null);
            }
        """.trimIndent()
        _events.tryEmit(BrowserEvent.ExecuteJs(js))
    }

    // ─── Param parsing utilities ───────────────────────────────────────
    private fun parseTransactionParams(json: String): Map<String, String> {
        val result = mutableMapOf<String, String>()
        try {
            // Simple JSON parsing – extract key/value pairs from the first object in params array
            val cleaned = json.trim()
            // Find the first { ... }
            val objStart = cleaned.indexOf('{')
            val objEnd = cleaned.lastIndexOf('}')
            if (objStart >= 0 && objEnd > objStart) {
                val obj = cleaned.substring(objStart + 1, objEnd)
                // Split by commas, parse key:value
                val pairs = obj.split(",")
                for (pair in pairs) {
                    val colonIdx = pair.indexOf(':')
                    if (colonIdx > 0) {
                        val key = pair.substring(0, colonIdx).trim().removeSurrounding("\"")
                        val value = pair.substring(colonIdx + 1).trim().removeSurrounding("\"")
                        result[key] = value
                    }
                }
            }
        } catch (_: Exception) { }
        return result
    }

    private fun extractChainIdFromParams(json: String): String {
        // params: [{ chainId: "0x..." }]
        val regex = Regex("""chainId["\s:]*"?(0x[0-9a-fA-F]+)"?""")
        return regex.find(json)?.groupValues?.get(1) ?: throw Exception("Missing chainId")
    }

    private fun extractFirstParam(json: String): String {
        // params: ["0x..."]
        val regex = Regex(""""(0x[0-9a-fA-F]+)"""")
        return regex.find(json)?.groupValues?.get(1) ?: ""
    }

    private fun extractSignMessage(json: String): String {
        // params: ["message", "address"] or ["address", "message"]
        // For personal_sign the message is first, address is second
        val regex = Regex(""""([^"]+)"""")
        val matches = regex.findAll(json).toList()
        return if (matches.size >= 2) {
            val first = matches[0].groupValues[1]
            // If first starts with 0x and is 42 chars, it's an address → message is second
            if (first.startsWith("0x") && first.length == 42) {
                matches[1].groupValues[1]
            } else {
                first
            }
        } else if (matches.isNotEmpty()) {
            matches[0].groupValues[1]
        } else {
            ""
        }
    }

    private fun parseBigInteger(hex: String?): BigInteger {
        if (hex.isNullOrEmpty() || hex == "0x") return BigInteger.ZERO
        return if (hex.startsWith("0x")) {
            BigInteger(hex.removePrefix("0x"), 16)
        } else {
            try { BigInteger(hex) } catch (_: Exception) { BigInteger.ZERO }
        }
    }

    /**
     * Returns the JavaScript code to inject into every page.
     * This creates an EIP-1193 compatible `window.ethereum` provider.
     */
    fun getProviderInjectionJs(): String {
        val address = _state.value.walletAddress.lowercase()
        val network = _state.value.selectedNetwork
        val chainIdHex = "0x" + network.chainId.toString(16)

        return """
(function() {
    if (window.ethereum && window.ethereum.isTranzo) return;

    var _callbacks = {};
    var _listeners = {};
    var _callbackId = 0;

    function generateId() {
        return 'tranzo_' + (++_callbackId) + '_' + Date.now();
    }

    var provider = {
        isTranzo: true,
        isMetaMask: true,
        isConnected: function() { return true; },
        chainId: '$chainIdHex',
        networkVersion: '${network.chainId}',
        selectedAddress: ${if (address.isNotEmpty()) "'$address'" else "null"},

        request: function(args) {
            return new Promise(function(resolve, reject) {
                var id = generateId();
                _callbacks[id] = { resolve: resolve, reject: reject };
                var params = args.params || [];
                try {
                    window._TranzoProvider.postMessage(
                        JSON.stringify({
                            id: id,
                            method: args.method,
                            params: JSON.stringify(params)
                        })
                    );
                } catch (e) {
                    delete _callbacks[id];
                    reject(e);
                }
            });
        },

        send: function(methodOrPayload, paramsOrCallback) {
            // Legacy API support
            if (typeof methodOrPayload === 'string') {
                return provider.request({ method: methodOrPayload, params: paramsOrCallback || [] });
            }
            if (typeof paramsOrCallback === 'function') {
                provider.request({ method: methodOrPayload.method, params: methodOrPayload.params || [] })
                    .then(function(r) { paramsOrCallback(null, { id: methodOrPayload.id, jsonrpc: '2.0', result: r }); })
                    .catch(function(e) { paramsOrCallback(e, null); });
                return;
            }
            return provider.request({ method: methodOrPayload.method, params: methodOrPayload.params || [] });
        },

        sendAsync: function(payload, callback) {
            provider.request({ method: payload.method, params: payload.params || [] })
                .then(function(r) { callback(null, { id: payload.id, jsonrpc: '2.0', result: r }); })
                .catch(function(e) { callback(e, null); });
        },

        enable: function() {
            return provider.request({ method: 'eth_requestAccounts' });
        },

        on: function(event, handler) {
            if (!_listeners[event]) _listeners[event] = [];
            _listeners[event].push(handler);
        },

        removeListener: function(event, handler) {
            if (!_listeners[event]) return;
            _listeners[event] = _listeners[event].filter(function(h) { return h !== handler; });
        },

        removeAllListeners: function(event) {
            if (event) {
                delete _listeners[event];
            } else {
                _listeners = {};
            }
        },

        _tranzoEmit: function(event, data) {
            if (event === 'chainChanged') {
                provider.chainId = data;
                provider.networkVersion = parseInt(data, 16).toString();
            }
            if (event === 'accountsChanged' && Array.isArray(data) && data.length > 0) {
                provider.selectedAddress = data[0];
            }
            var handlers = _listeners[event] || [];
            for (var i = 0; i < handlers.length; i++) {
                try { handlers[i](data); } catch (e) { console.error(e); }
            }
        },

        _tranzoCallback: function(id, error, result) {
            var cb = _callbacks[id];
            if (!cb) return;
            delete _callbacks[id];
            if (error) {
                cb.reject(error);
            } else {
                cb.resolve(result);
            }
        }
    };

    // Make it non-writable to prevent dApps from overwriting
    Object.defineProperty(window, 'ethereum', {
        value: provider,
        writable: false,
        configurable: false
    });

    // Dispatch the EIP-6963 announcement event
    window.dispatchEvent(new Event('ethereum#initialized'));
})();
        """.trimIndent()
    }
}
