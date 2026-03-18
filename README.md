# Tranzo Wallet

A production-ready, non-custodial Android cryptocurrency wallet built with Kotlin, Jetpack Compose, and the Frost UI design system.

## Features

- **Non-Custodial**: Your keys, your crypto. Seed phrases never leave your device.
- **BIP39/BIP44**: Industry-standard wallet generation and key derivation.
- **Multi-Chain**: Support for Ethereum, Polygon, BSC, Base, Arbitrum, Optimism.
- **Frost UI**: Glassmorphic design system with frosted translucent cards and icy blue palette.
- **Secure Storage**: AES-GCM encryption via Android Keystore.
- **Biometric Auth**: PIN + fingerprint/face authentication.
- **Send & Receive**: Full transaction flow with QR code support.
- **Token Management**: ERC-20 token balances and transfers.
- **NFT Gallery**: View your NFT collection.
- **Transaction History**: Complete transaction log with status tracking.

## Architecture

```
/core          - crypto, keystore, security
/data          - network, repositories, localdb
/domain        - models, usecases
/features      - onboarding, wallet, tokens, nft, settings, payments
/ui            - frost-components, themes
/app           - main entry point
```

**Pattern**: MVVM with Clean Architecture
**DI**: Hilt
**UI**: Jetpack Compose with Material3
**Networking**: Retrofit + OkHttp + Web3j
**Storage**: Room DB + EncryptedSharedPreferences

## Getting Started

### Prerequisites

- Android Studio Ladybug or newer
- JDK 17
- Android SDK 35
- Min SDK 26 (Android 8.0)

### Setup

1. Clone the repository:
```bash
git clone https://github.com/Pranav00x/TranzoWallet.git
```

2. Copy the example properties file:
```bash
cp local.properties.example local.properties
```

3. Add your API keys to `local.properties`:
```properties
ETHERSCAN_API_KEY=your_key_here
COINGECKO_API_KEY=your_key_here
```

4. Open in Android Studio and sync Gradle.

5. Run on emulator or device.

## Build

```bash
# Debug APK
./gradlew assembleDebug

# Release AAB
./gradlew bundleRelease

# Run tests
./gradlew test

# Run lint
./gradlew lint
```

## Security

- Seed phrases encrypted with AES-256-GCM via Android Keystore
- PIN hashed with SHA-256
- FLAG_SECURE enabled in release builds
- No cleartext traffic allowed
- ProGuard/R8 obfuscation in release
- See [Security Checklist](docs/SECURITY_CHECKLIST.md) for full details

## Documentation

- [Roadmap](docs/ROADMAP.md)
- [Security Checklist](docs/SECURITY_CHECKLIST.md)
- [Migration Guide](docs/MIGRATION_GUIDE.md)
- [Frost UI Design Spec](docs/FIGMA_SPEC.md)
- [GitHub Issues](docs/GITHUB_ISSUES.md)

## License

MIT License
