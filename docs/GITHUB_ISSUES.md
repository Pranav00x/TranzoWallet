# Tranzo Wallet -- GitHub Issues

Pre-defined issues for project tracking, grouped by milestone. Create these in the GitHub repository using `gh issue create` or the GitHub web UI.

---

## Milestone 1: Foundation

### Issue #1: Set up modular Android project structure
**Labels:** `setup`, `infrastructure`
**Milestone:** Foundation
Set up the multi-module Gradle project with `app`, `core` (crypto, keystore, security), `data` (network, localdb, repository), `domain` (model, usecase), `feature` (onboarding, wallet, tokens, nft, settings, payments), and `ui` (theme, frost-components) modules. Configure `settings.gradle.kts`, version catalog (`libs.versions.toml`), and shared build conventions.

### Issue #2: Implement BIP-39 mnemonic generation
**Labels:** `core`, `crypto`, `security`
**Milestone:** Foundation
Create `MnemonicGenerator` in `core/crypto` that generates 12-word and 24-word BIP-39 mnemonics using `SecureRandom`. Include the English word list, checksum validation, and unit tests with known test vectors from the BIP-39 specification.

### Issue #3: Implement BIP-44 HD key derivation
**Labels:** `core`, `crypto`
**Milestone:** Foundation
Create `KeyDerivation` in `core/crypto` that derives child keys from a BIP-39 seed using BIP-44 paths. Support coin types for Ethereum (60), Bitcoin (0), and Tron (195). Use the bitcoinj or web3j library for derivation. Include unit tests with known derivation vectors.

### Issue #4: Implement secure storage with Android Keystore
**Labels:** `core`, `security`
**Milestone:** Foundation
Create `SecureStorageManager` interface and `SecureStorageManagerImpl` in `core/keystore`. Use `EncryptedSharedPreferences` with AES-256-GCM value encryption and Keystore-backed key encryption. Provide methods for storing/retrieving mnemonic, PIN hash, and user preferences. Write integration tests.

### Issue #5: Define domain models
**Labels:** `domain`, `architecture`
**Milestone:** Foundation
Create data classes in `domain/model`: `Wallet` (address, name, network), `Token` (symbol, contractAddress, balance, price), `Transaction` (hash, from, to, value, status, timestamp), `Network` (chainId, name, rpcUrl, explorerUrl), `NFT` (tokenId, contractAddress, metadata), `SendRequest` (to, amount, gasLimit, gasPrice).

### Issue #6: Create Frost UI design system
**Labels:** `ui`, `design-system`
**Milestone:** Foundation
Implement the Frost glassmorphic design system: `FrostDesignTokens` (colors, typography, spacing, elevation, radii), `FrostCard` (translucent card with blur), `FrostButtonPrimary`/`FrostButtonSecondary`, `FrostAppBar`, `FrostModalSheet`, `TokenListItem`, `WalletBalanceCard`, `FloatingSendButton`, `SecureInputField`. Include Compose previews for each component.

### Issue #7: Set up CI/CD pipeline with GitHub Actions
**Labels:** `infrastructure`, `ci-cd`
**Milestone:** Foundation
Create `.github/workflows/build.yml` with jobs: build (lint, unit tests, debug APK), ui-test (connected tests on Android emulator), release (build AAB, create GitHub Release on tag push). Use `actions/checkout@v4`, `actions/setup-java@v4`, `gradle/actions/setup-gradle@v4`, `reactivecircus/android-emulator-runner@v2`.

### Issue #8: Set up network layer with Retrofit
**Labels:** `data`, `network`
**Milestone:** Foundation
Create Retrofit interfaces: `EthRpcApi` (JSON-RPC calls), `CoinGeckoApi` (price data), `ExplorerApi` (transaction history). Set up OkHttp client with logging interceptor (debug only), timeouts, and Moshi JSON parsing. Create `NetworkModule` for Hilt DI. Include `network_security_config.xml` to block cleartext traffic.

### Issue #9: Set up Room database
**Labels:** `data`, `database`
**Milestone:** Foundation
Create `TranzoDatabase` with entities: `WalletEntity`, `TokenEntity`, `TransactionEntity`. Implement DAOs with queries for CRUD operations, balance lookups, and paginated transaction history. Create `DatabaseModule` for Hilt DI. Include database migration strategy.

---

## Milestone 2: Onboarding

### Issue #10: Implement splash screen
**Labels:** `feature`, `onboarding`, `ui`
**Milestone:** Onboarding
Create `SplashScreen` with Frost-themed branding animation, loading indicator, and auto-navigation. If wallet exists, navigate to PIN entry. If no wallet, navigate to intro/create flow. Use `SplashViewModel` to check wallet state via `SecureStorageManager`.

### Issue #11: Implement create wallet flow
**Labels:** `feature`, `onboarding`, `crypto`
**Milestone:** Onboarding
Create `CreateWalletScreen` and `CreateWalletViewModel`. Generate a 12-word mnemonic via `MnemonicGenerator`, derive keys via `KeyDerivation`, store encrypted seed via `SecureStorageManager`, save wallet entity to Room. Show loading state during key generation. Navigate to seed backup screen on success.

### Issue #12: Implement import wallet flow
**Labels:** `feature`, `onboarding`, `crypto`
**Milestone:** Onboarding
Create `ImportWalletScreen` and `ImportWalletViewModel`. Accept 12 or 24 word mnemonic input with word-by-word validation. Validate checksum, derive keys, store encrypted seed, and save wallet entity. Handle invalid mnemonic errors with clear user feedback.

### Issue #13: Implement seed phrase backup screen
**Labels:** `feature`, `onboarding`, `security`
**Milestone:** Onboarding
Create `BackupSeedScreen` that displays the mnemonic as a numbered word grid. Require the user to verify by selecting words in order. Warn about the importance of backup. Apply `FLAG_SECURE` to prevent screenshots. Clear mnemonic from memory after verification.

### Issue #14: Implement PIN setup
**Labels:** `feature`, `onboarding`, `security`
**Milestone:** Onboarding
Create `PinSetupScreen` and `PinSetupViewModel`. Accept a 6-digit PIN, require confirmation (enter twice), hash with SHA-256, store hash via `SecureStorageManager`. Use `SecureInputField` with dot masking. Show error on mismatch. Navigate to home on success.

---

## Milestone 3: Wallet Core

### Issue #15: Implement wallet home dashboard
**Labels:** `feature`, `wallet`, `ui`
**Milestone:** Wallet Core
Create `WalletHomeScreen` and `WalletHomeViewModel`. Display `WalletBalanceCard` with total portfolio value, network name, and truncated address. Show token list using `TokenListItem` components. Include pull-to-refresh for balance updates. Integrate bottom navigation bar.

### Issue #16: Implement send token flow
**Labels:** `feature`, `wallet`, `transactions`
**Milestone:** Wallet Core
Create `SendTokenScreen` and `SendTokenViewModel`. Steps: select token, enter recipient address (with QR scan option), enter amount (with max button), review gas estimation, confirm with PIN/biometric, broadcast transaction, show result. Implement `SendTokenUseCase` for validation and `TransactionRepositoryImpl` for broadcasting.

### Issue #17: Implement receive screen with QR code
**Labels:** `feature`, `wallet`, `ui`
**Milestone:** Wallet Core
Create `ReceiveTokenScreen` that displays the wallet address as a QR code (generated by `QrCodeGenerator`) and as copiable text. Allow sharing the address via Android share sheet. Show network name to prevent cross-chain mistakes.

### Issue #18: Implement transaction history
**Labels:** `feature`, `wallet`, `data`
**Milestone:** Wallet Core
Create `TransactionHistoryScreen` and `TransactionHistoryViewModel`. Fetch transactions from `ExplorerApi`, cache in Room via `TransactionRepositoryImpl`. Display as a scrollable list with type icon (send/receive), amount, address, timestamp, and status. Support pagination and pull-to-refresh.

---

## Milestone 4: Multi-Chain

### Issue #19: Implement network switching
**Labels:** `feature`, `multi-chain`, `architecture`
**Milestone:** Multi-Chain
Add network selector UI (modal sheet with supported networks). Persist selected network. Update all API calls, address derivation, and explorer URLs based on active network. Support: Ethereum Mainnet, Goerli, Sepolia, BSC, Polygon, Arbitrum. Refresh balances on network change.

### Issue #20: Implement ERC-20 token support
**Labels:** `feature`, `multi-chain`, `tokens`
**Milestone:** Multi-Chain
Fetch ERC-20 token balances by calling `eth_call` on token contracts (`balanceOf`). Display token list with symbol, icon (from CoinGecko), balance, and USD value. Support custom token addition by contract address with metadata auto-fetch.

### Issue #21: Implement gas estimation UI
**Labels:** `feature`, `wallet`, `ux`
**Milestone:** Multi-Chain
Add slow/standard/fast gas options on the send confirmation screen. Fetch current gas prices from the RPC node. Show estimated transaction fee in both native token and USD. Allow manual gas limit override for advanced users.

---

## Milestone 5: Advanced Features

### Issue #22: Implement NFT gallery
**Labels:** `feature`, `nft`, `ui`
**Milestone:** Advanced Features
Fetch ERC-721 and ERC-1155 tokens owned by the wallet address. Display as a grid gallery with thumbnail images. NFT detail view shows full image, name, description, attributes, collection info, and transfer option. Use Coil for image loading.

### Issue #23: Integrate WalletConnect v2
**Labels:** `feature`, `walletconnect`, `dapp`
**Milestone:** Advanced Features
Integrate the WalletConnect v2 Android SDK. Support session proposal scanning (QR code), session approval with chain/account selection, and signing requests (`eth_sendTransaction`, `personal_sign`, `eth_signTypedData_v4`). Show approval dialogs with dApp metadata.

### Issue #24: Implement dApp browser
**Labels:** `feature`, `dapp`, `webview`
**Milestone:** Advanced Features
Create an in-app WebView-based dApp browser. Inject EIP-1193 JavaScript provider via `Web3Bridge`. Route signing requests through `TransactionSigner` with user confirmation. Add URL bar, navigation controls, and domain whitelisting. Disable `WebView.setWebContentsDebuggingEnabled` in release builds.

### Issue #25: Implement push notifications
**Labels:** `feature`, `notifications`
**Milestone:** Advanced Features
Integrate Firebase Cloud Messaging for incoming transaction notifications. Monitor wallet addresses on a backend service (or use a third-party webhook provider). Show notification with transaction amount, sender, and status. Deep-link to transaction detail screen.

---

## Milestone 6: Payments

### Issue #26: Implement card management module
**Labels:** `feature`, `payments`, `cards`
**Milestone:** Payments
Create card management UI for linking virtual or physical cards. Display card details in a Frost-styled card component. Support card activation, PIN management, and spending limits. Integrate with card issuer API (provider TBD).

### Issue #27: Implement UPI payment adapter
**Labels:** `feature`, `payments`, `upi`
**Milestone:** Payments
Build UPI payment flow: enter UPI ID or scan QR, enter amount, confirm payment. Integrate with UPI SDK/API for payment initiation. Map wallet balance to UPI payment source. Show payment status and receipt.

### Issue #28: Integrate KYC provider
**Labels:** `feature`, `payments`, `compliance`
**Milestone:** Payments
Integrate a KYC verification SDK (DigiLocker or third-party provider). Collect user identity documents, selfie verification, and PAN/Aadhaar validation. Gate payment features behind KYC completion. Store KYC status locally and on backend.

---

## Milestone 7: Production Ready

### Issue #29: Security audit
**Labels:** `security`, `audit`
**Milestone:** Production Ready
Engage a third-party security firm for penetration testing and code review. Focus areas: key storage, transaction signing, network communication, WebView security. Remediate all critical and high findings before release.

### Issue #30: Performance optimization
**Labels:** `performance`, `optimization`
**Milestone:** Production Ready
Profile and optimize: cold start time (<2s target), Compose recomposition (minimize unnecessary recompositions), memory usage (no leaks in ViewModel/Fragment lifecycle), network calls (batch where possible, implement caching). Use Android Profiler and Macrobenchmark.

### Issue #31: Accessibility compliance
**Labels:** `accessibility`, `ux`
**Milestone:** Production Ready
Audit all screens with TalkBack. Add content descriptions to all interactive elements and images. Ensure minimum contrast ratios (4.5:1 for text). Support dynamic font scaling. Test with Switch Access. Fix any focus order issues in Compose.

### Issue #32: Play Store listing preparation
**Labels:** `release`, `marketing`
**Milestone:** Production Ready
Prepare Play Store listing: app icon (Frost-themed), feature graphic, 6-8 screenshots across phone and tablet, short/long descriptions, privacy policy URL, content rating questionnaire, target audience declaration, data safety section. Create an internal testing track for initial rollout.
