# Tranzo Wallet Roadmap

Development roadmap organized by milestones with estimated sprint timelines.

---

## Milestone 1: Foundation (Sprint 1-2)

Core project setup, cryptographic primitives, and design system.

- [x] Project scaffolding and modular build system (app, core, data, domain, feature, ui)
- [x] Core crypto module -- BIP-39 mnemonic generation and BIP-44 HD key derivation
- [x] Secure storage with Android Keystore and EncryptedSharedPreferences
- [x] Domain models (Wallet, Token, Transaction, Network, NFT, SendRequest)
- [x] Frost UI design system -- glassmorphic component library (FrostCard, FrostButton, FrostAppBar, FrostModalSheet)
- [x] Theme system with Frost color palette, typography scale, and design tokens
- [x] CI/CD pipeline with GitHub Actions (build, lint, test, release)
- [x] Network layer setup with Retrofit (EthRpcApi, CoinGeckoApi, ExplorerApi)
- [x] Room database with DAOs for wallets, tokens, and transactions

## Milestone 2: Onboarding (Sprint 3)

First-run experience: wallet creation, import, and security setup.

- [x] Splash screen with Frost branding and loading animation
- [x] Create wallet flow -- generate mnemonic, derive keys, save encrypted seed
- [x] Import wallet flow -- validate mnemonic input, restore wallet
- [x] Seed phrase backup screen with copy-and-verify pattern
- [x] PIN setup with SHA-256 hashing and secure storage
- [x] Biometric authentication setup (optional, Class 3 biometrics)
- [x] Intro/onboarding carousel explaining wallet features

## Milestone 3: Wallet Core (Sprint 4-5)

Primary wallet functionality: view balances, send, receive, and transaction history.

- [x] Wallet home dashboard with balance card and token list
- [x] Token balance display with real-time price data from CoinGecko
- [x] Send token flow with address input, amount entry, gas estimation, and confirmation
- [x] Receive screen with QR code generation for wallet address
- [x] Transaction history screen with pull-to-refresh and pagination
- [x] Bottom navigation with Home, Tokens, NFTs, Settings tabs
- [x] Repository layer connecting local database with remote APIs

## Milestone 4: Multi-Chain (Sprint 6)

Expand beyond Ethereum to support multiple blockchain networks.

- [ ] Network switching UI (Ethereum Mainnet, Goerli, Sepolia, BSC, Polygon, Arbitrum)
- [ ] ERC-20 token balance fetching and display
- [ ] Custom token addition by contract address
- [ ] Gas estimation improvements with slow/standard/fast options
- [ ] Chain-specific address validation
- [ ] Multi-network transaction history aggregation
- [ ] Bitcoin support with P2WPKH address derivation
- [ ] Tron support with TRC-20 token handling

## Milestone 5: Advanced Features (Sprint 7-8)

Power-user features: NFTs, dApp connectivity, and notifications.

- [ ] NFT gallery -- fetch and display ERC-721/ERC-1155 tokens with metadata
- [ ] NFT detail view with image, attributes, and transfer option
- [ ] WalletConnect v2 integration -- session proposal, approval, signing
- [ ] DApp browser with Web3 provider injection
- [ ] DApp domain whitelisting and transaction approval dialog
- [ ] Push notifications for incoming transactions (Firebase Cloud Messaging)
- [ ] Price alerts for tracked tokens
- [ ] Address book for frequently used recipients

## Milestone 6: Payments (Sprint 9-10)

Fiat on/off ramp and payment integrations for the Indian market.

- [ ] Card management module -- link and display virtual/physical cards
- [ ] UPI payment adapter -- initiate UPI payments from wallet balance
- [ ] KYC provider integration (DigiLocker / third-party KYC SDK)
- [ ] Fiat on-ramp via payment gateway (Razorpay / similar)
- [ ] Fiat off-ramp with bank withdrawal
- [ ] Transaction receipts and export (PDF/CSV)
- [ ] Merchant QR code scanning for in-store payments

## Milestone 7: Production Ready (Sprint 11-12)

Hardening, optimization, and launch preparation.

- [ ] Third-party security audit (penetration testing, code review)
- [ ] Performance optimization -- startup time, frame rate, memory usage
- [ ] Accessibility audit and remediation (TalkBack, content descriptions, contrast ratios)
- [ ] Play Store listing assets (screenshots, feature graphic, description)
- [ ] Privacy policy and terms of service
- [ ] Crash reporting integration (Firebase Crashlytics)
- [ ] Analytics integration (privacy-respecting, no PII)
- [ ] Rate limiting and abuse prevention for API calls
- [ ] App size optimization (APK splits, on-demand delivery)
- [ ] Localization -- Hindi, Tamil, Telugu, Kannada, Marathi (top Indian languages)

---

## Release Schedule (Estimated)

| Milestone | Target | Deliverable |
|---|---|---|
| M1: Foundation | Completed | Project skeleton, crypto core, design system |
| M2: Onboarding | Completed | Onboarding flow, wallet creation/import |
| M3: Wallet Core | Completed | Send, receive, transaction history |
| M4: Multi-Chain | Sprint 6 | Multi-network support |
| M5: Advanced | Sprint 7-8 | NFTs, WalletConnect, dApp browser |
| M6: Payments | Sprint 9-10 | Fiat ramp, UPI, card management |
| M7: Production | Sprint 11-12 | Security audit, Play Store launch |

## Success Metrics

- **Startup time**: Cold start under 2 seconds on mid-range devices
- **Transaction signing**: Under 500ms from confirmation tap to broadcast
- **Crash-free rate**: >99.5% on Play Store
- **Test coverage**: >80% line coverage on `core` and `domain` modules
- **Security**: Zero critical/high findings in third-party audit
