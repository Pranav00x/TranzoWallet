# Migration Guide: FlowStable to Tranzo Wallet

This guide explains how to reuse and adapt logic from the [FlowStable Android Test Wallet](https://github.com/tranzolabs/flowstable-android-test-wallet) repository when building Tranzo Wallet features.

---

## 1. Package Rename

All classes migrated from FlowStable must be re-packaged under the Tranzo namespace.

| FlowStable Package | Tranzo Wallet Package |
|---|---|
| `com.antigravity.cryptowallet` | `com.tranzo.wallet` |
| `com.antigravity.cryptowallet.wallet` | `com.tranzo.wallet.core.crypto` / `com.tranzo.wallet.core.keystore` |
| `com.antigravity.cryptowallet.blockchain` | `com.tranzo.wallet.data.network` |
| `com.antigravity.cryptowallet.ui` | `com.tranzo.wallet.feature.*` |
| `com.antigravity.cryptowallet.utils` | `com.tranzo.wallet.core.crypto` (crypto utils) / relevant modules |

When renaming, update all import statements, Hilt module bindings, and AndroidManifest references.

---

## 2. Wallet Logic

### Mnemonic Generation & Key Derivation

**FlowStable**: `WalletRepository.kt` contains both mnemonic generation and HD key derivation in a single class.

**Tranzo Wallet**: Split into dedicated classes in `core/crypto`:

| FlowStable | Tranzo Wallet | Notes |
|---|---|---|
| `WalletRepository.generateMnemonic()` | `MnemonicGenerator.generate()` | BIP-39 mnemonic generation. Reuse the word list handling and entropy logic. |
| `WalletRepository.deriveKey()` | `KeyDerivation.deriveKeyPair()` | BIP-44 HD derivation. Adapt derivation paths for multi-chain support. |
| `WalletRepository.signTransaction()` | `TransactionSigner.sign()` | ECDSA signing with secp256k1. Reuse the signing logic directly. |

**Migration steps:**
1. Copy the mnemonic word list and entropy generation logic from `WalletRepository`
2. Extract the BIP-44 derivation path logic into `KeyDerivation`
3. Update derivation paths to support multiple coin types (ETH: `m/44'/60'/0'/0/0`, BTC: `m/44'/0'/0'/0/0`, TRX: `m/44'/195'/0'/0/0`)

### Secure Storage

**FlowStable**: `SecureStorage.kt` uses raw SharedPreferences with AES encryption.

**Tranzo Wallet**: `SecureStorageManager` in `core/keystore` uses `EncryptedSharedPreferences` backed by Android Keystore.

| FlowStable | Tranzo Wallet | Notes |
|---|---|---|
| `SecureStorage.saveSeed()` | `SecureStorageManagerImpl.storeMnemonic()` | Replace manual AES with `EncryptedSharedPreferences`. |
| `SecureStorage.getSeed()` | `SecureStorageManagerImpl.retrieveMnemonic()` | Keystore-backed decryption. |
| `SecureStorage.savePin()` | `SecureStorageManagerImpl.storePin()` | Hash with SHA-256 before storing. |

**Migration steps:**
1. Do NOT copy the raw AES implementation. Use the existing `SecureStorageManagerImpl` which leverages AndroidX Security.
2. Reuse only the data flow logic (when to store, when to retrieve, error handling).

---

## 3. Blockchain Interaction

**FlowStable**: `BlockchainService.kt` handles Ethereum JSON-RPC calls.

**Tranzo Wallet**: Split across `data/network`:

| FlowStable | Tranzo Wallet | Notes |
|---|---|---|
| `BlockchainService.getBalance()` | `EthRpcApi.getBalance()` | Retrofit interface for `eth_getBalance`. Reuse the JSON-RPC request/response parsing. |
| `BlockchainService.sendTransaction()` | `EthRpcApi.sendRawTransaction()` | Reuse raw transaction encoding logic. |
| `BlockchainService.getGasPrice()` | `EthRpcApi.getGasPrice()` | Direct reuse. |
| `BlockchainService.estimateGas()` | `EthRpcApi.estimateGas()` | Direct reuse. |

**Migration steps:**
1. Extract the JSON-RPC request body construction (method, params, id) from FlowStable's service
2. Map to Retrofit `@POST` calls in `EthRpcApi`
3. Add proper error mapping from JSON-RPC error codes to domain exceptions

---

## 4. UI Migration: Brutalist to Frost

FlowStable uses a "Brutalist" design system with sharp corners, bold outlines, and monospace fonts. Tranzo Wallet uses the **Frost UI** glassmorphic design system.

### Component Mapping

| FlowStable (Brutalist) | Tranzo Wallet (Frost) | Key Differences |
|---|---|---|
| `BrutalistCard` | `FrostCard` | Rounded corners (20dp), semi-transparent background, blur effect |
| `BrutalistButton` | `FrostButtonPrimary` / `FrostButtonSecondary` | Gradient background (icyBlue -> frostCyan), 16dp corner radius |
| `BrutalistInput` | `SecureInputField` | Frosted surface background, rounded, visual transformation for sensitive fields |
| `BrutalistTopBar` | `FrostAppBar` | Transparent background, blur backdrop |
| `BrutalistBottomSheet` | `FrostModalSheet` | Glassmorphic modal with translucent background |
| N/A | `WalletBalanceCard` | New component for balance display |
| N/A | `FloatingSendButton` | New FAB-style send button |

### Theme Mapping

| Brutalist Token | Frost Token | Value |
|---|---|---|
| `brutalistBlack` | `frostBackground` | `#0A0E1A` |
| `brutalistWhite` | `frostWhite` | `#F9FAFB` |
| `brutalistAccent` | `icyBlue` | `#60A5FA` |
| `brutalistBorder` | `frostBorder` | `#33FFFFFF` (20% white) |

**Migration steps:**
1. Replace all Brutalist component imports with Frost equivalents
2. Update `MaterialTheme` color scheme references to use `FrostDesignTokens`
3. Replace monospace fonts with the system default (Inter/Roboto-style) defined in `TranzoTypography`
4. Test all screens against the dark Frost background (`#0A0E1A`)

---

## 5. Multi-Chain Support

**FlowStable**: Ethereum-only.

**Tranzo Wallet**: Multi-chain architecture supporting Ethereum, Bitcoin, and Tron.

### Address Derivation

Reuse FlowStable's `CryptoUtils` for Ethereum address derivation and extend for other chains:

```
CryptoUtils.deriveEthAddress(publicKey)   -> reuse directly
CryptoUtils.deriveBtcAddress(publicKey)   -> new: P2WPKH (Bech32) encoding
CryptoUtils.deriveTrxAddress(publicKey)   -> new: Base58Check with 0x41 prefix
```

### Derivation Paths (BIP-44)

| Chain | Coin Type | Path |
|---|---|---|
| Ethereum | 60 | `m/44'/60'/0'/0/0` |
| Bitcoin | 0 | `m/44'/0'/0'/0/0` |
| Tron | 195 | `m/44'/195'/0'/0/0` |

### Network Configuration

The `Network` domain model in Tranzo Wallet defines chain-specific parameters (RPC URL, chain ID, explorer URL). FlowStable's hardcoded Ethereum configuration should be replaced with dynamic network selection.

---

## 6. Transaction Handling

**FlowStable**: `AssetRepository.send()` handles the full send flow.

**Tranzo Wallet**: Split into use case and repository layers:

| FlowStable | Tranzo Wallet | Notes |
|---|---|---|
| `AssetRepository.send()` | `SendTokenUseCase` -> `TransactionRepositoryImpl` | Use case validates inputs; repository handles signing and broadcasting. |
| `AssetRepository.getHistory()` | `TransactionRepositoryImpl.getTransactions()` | Fetches from both local DB cache and remote explorer API. |

**Migration steps:**
1. Extract input validation (address format, sufficient balance, gas estimation) into `SendTokenUseCase`
2. Move raw transaction construction and RPC broadcasting into `TransactionRepositoryImpl`
3. Add `TransactionEntity` mapping for local caching of transaction history

---

## 7. Explorer APIs

**FlowStable**: `ExplorerApi` fetches transaction history from Etherscan.

**Tranzo Wallet**: `ExplorerApi` in `data/network/api` serves the same purpose but must support multiple explorers:

| Chain | Explorer | API Pattern |
|---|---|---|
| Ethereum | Etherscan | `api.etherscan.io/api?module=account&action=txlist` |
| Bitcoin | Blockstream | `blockstream.info/api/address/{addr}/txs` |
| Tron | TronGrid | `api.trongrid.io/v1/accounts/{addr}/transactions` |

**Migration steps:**
1. Reuse FlowStable's Etherscan response parsing and transaction mapping
2. Create chain-specific Retrofit interfaces for each explorer
3. Implement a `TransactionHistoryProvider` abstraction that delegates to the correct explorer based on the active network

---

## 8. Web3Bridge (dApp Browser)

**FlowStable**: `Web3Bridge.kt` injects a JavaScript provider into a WebView for dApp interaction.

**Tranzo Wallet**: This feature is planned for Milestone 5. When implementing:

1. Reuse the JavaScript injection script from FlowStable's `Web3Bridge`
2. Adapt the message handler to route signing requests through `TransactionSigner`
3. Add domain whitelisting and user consent dialogs before signing
4. Implement EIP-1193 provider interface for modern dApp compatibility

---

## 9. WalletConnect Integration

**FlowStable**: Uses WalletConnect v1 (deprecated).

**Tranzo Wallet**: Must use **WalletConnect v2** (via the `com.walletconnect:android-core` SDK).

**Migration steps:**
1. Do NOT reuse the WalletConnect v1 client code
2. Implement fresh using the WalletConnect v2 SDK
3. Reuse the session approval UI flow and signing request handling patterns
4. Support `eth_sendTransaction`, `personal_sign`, and `eth_signTypedData_v4` methods

---

## 10. What NOT to Migrate

The following items from FlowStable must **not** be carried over:

| Item | Reason |
|---|---|
| Debug keystore (`debug.keystore`) | Generate a new one for Tranzo Wallet |
| Hardcoded API keys in source | Use `BuildConfig` + `local.properties` pattern |
| Hardcoded RPC URLs | Use `Network` domain model with configurable endpoints |
| WalletConnect v1 dependency | Deprecated; use v2 |
| Brutalist UI components | Replaced by Frost UI design system |
| `minSdk 21` configuration | Tranzo targets `minSdk 26` for Keystore and BiometricPrompt support |
| Raw SharedPreferences for secrets | Replaced by `EncryptedSharedPreferences` |
| Test mnemonics in source code | Use test fixtures or environment variables |
| `allowBackup=true` in manifest | Security risk for wallet apps |
