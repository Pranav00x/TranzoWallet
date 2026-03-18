# Tranzo Wallet Security Checklist

This document defines the security requirements, implemented safeguards, and recommended hardening measures for the Tranzo Wallet Android application.

---

## 1. Non-Custodial Architecture

- [x] Private keys and seed phrases are generated on-device and **never transmitted** to any server
- [x] The wallet is fully non-custodial: users retain sole control of their funds
- [x] No server-side storage of mnemonics, private keys, or signing material
- [x] Key generation uses `java.security.SecureRandom` backed by the platform CSPRNG

## 2. Seed Phrase & Key Storage

- [x] Mnemonic seed phrase is encrypted at rest using **AES-256-GCM**
- [x] AES encryption key is generated and stored inside the **Android Keystore** (hardware-backed when available)
- [x] Keystore key is bound to the device and cannot be exported
- [x] Encrypted blob is stored in `EncryptedSharedPreferences` with `AES256_SIV` key encryption and `AES256_GCM` value encryption
- [x] Decrypted seed phrase is held in memory only during active signing operations and zeroed immediately after
- [ ] Consider using `StrongBox` Keystore when available (API 28+) for HSM-level protection

## 3. Authentication

### PIN Authentication
- [x] User PIN is **hashed with SHA-256** before storage (never stored in plaintext)
- [x] PIN is required on app launch and before sensitive operations (send, export seed)
- [ ] Implement rate limiting: lock out after 5 consecutive failed PIN attempts for 30 seconds, escalating on further failures
- [ ] Consider using Argon2id or PBKDF2 with a high iteration count instead of plain SHA-256 for PIN hashing

### Biometric Authentication
- [x] Biometric authentication via `BiometricPrompt` API (fingerprint, face)
- [x] Falls back to PIN when biometric hardware is unavailable
- [x] Biometric key is bound to `setUserAuthenticationRequired(true)` in Android Keystore
- [x] Only `BIOMETRIC_STRONG` (Class 3) biometrics accepted

## 4. UI Security

- [x] `FLAG_SECURE` set on all Activities in release builds to prevent screenshots and screen recording
- [x] Seed phrase display screen clears seed from ViewModel state on navigation away
- [x] Sensitive text fields use `visualTransformation` to mask input (PIN entry, private key)
- [ ] Implement clipboard auto-clear: when a wallet address is copied, schedule clipboard clearing after 60 seconds
- [ ] Add a screen-lock overlay that activates after 30 seconds of inactivity

## 5. API Key Management

- [x] No API keys hardcoded in source code
- [x] API keys injected via `BuildConfig` fields sourced from `local.properties`
- [x] `local.properties` is excluded from version control via `.gitignore`
- [x] CI/CD pipeline injects keys from GitHub Secrets at build time
- [ ] Consider server-side proxy for API calls to avoid embedding keys in the APK at all

## 6. Network Security

- [x] Network security configuration (`network_security_config.xml`) disables cleartext HTTP traffic
- [x] All API communication uses HTTPS/TLS 1.2+
- [x] Retrofit + OkHttp with appropriate timeouts to prevent resource exhaustion
- [ ] **Certificate pinning**: pin leaf or intermediate certificates for critical endpoints (RPC nodes, CoinGecko, Etherscan) using OkHttp `CertificatePinner`
- [ ] Implement certificate transparency (CT) log verification

## 7. Code Obfuscation & Tamper Protection

- [x] **ProGuard/R8** enabled for release builds with `isMinifyEnabled = true`
- [x] Resource shrinking enabled with `isShrinkResources = true`
- [x] Custom ProGuard rules preserve Retrofit interfaces, Room entities, and Hilt-injected classes
- [ ] Add integrity checks at startup to detect APK tampering (signature verification)
- [ ] Consider commercial obfuscation (DexGuard) for additional protection against reverse engineering

## 8. Dependency Security

- [ ] Enable Gradle dependency verification (`gradle/verification-metadata.xml`) to pin artifact checksums
- [ ] Run `./gradlew dependencyCheckAnalyze` (OWASP Dependency Check) in CI to scan for known CVEs
- [ ] Subscribe to security advisories for critical dependencies (web3j, BouncyCastle, OkHttp)
- [ ] Pin dependency versions in `libs.versions.toml` (already done) and review updates before merging

## 9. Data Persistence Security

- [x] Room database does not store private keys or seed phrases
- [x] Transaction history and token metadata are non-sensitive and stored in plaintext Room DB
- [x] Wallet entity stores only the public address and display name
- [ ] Consider encrypting the Room database with SQLCipher for defense-in-depth
- [ ] Implement data wipe on uninstall using `android:allowBackup="false"` and `android:fullBackupContent="false"`

## 10. Logging & Debugging

- [x] `isDebuggable = false` in release build type
- [x] Debug build uses `.debug` application ID suffix to prevent co-installation with release
- [ ] Strip all `Log.d` / `Log.v` calls from release builds via ProGuard rules or Timber with a no-op release tree
- [ ] Disable WebView debugging in release: `WebView.setWebContentsDebuggingEnabled(false)`

---

## Threat Model

### T1: Supply Chain Attacks
- **Threat**: Malicious code injected via compromised dependencies
- **Mitigations**: Pin dependency versions, enable Gradle verification, run OWASP Dependency Check in CI, review changelogs before version bumps
- **Residual risk**: Medium -- requires ongoing vigilance

### T2: Physical Device Access
- **Threat**: Attacker with physical access to an unlocked device
- **Mitigations**: PIN/biometric gate on app launch and sensitive operations, FLAG_SECURE prevents screenshots, seed encrypted at rest with Keystore-bound key
- **Residual risk**: Low if PIN is strong; medium if attacker has extended physical access and device is rooted

### T3: Network Man-in-the-Middle (MITM)
- **Threat**: Attacker intercepts API calls to RPC nodes or price feeds
- **Mitigations**: TLS 1.2+ enforced, cleartext disabled, certificate pinning (recommended)
- **Residual risk**: Low with certificate pinning; medium without

### T4: Clipboard Sniffing
- **Threat**: Malicious apps read wallet addresses or seed phrases from the clipboard
- **Mitigations**: Auto-clear clipboard after copy, warn users before copying seed phrase, use in-app display instead of clipboard where possible
- **Residual risk**: Medium on Android < 12 (no clipboard access notifications)

### T5: Screenshot & Screen Recording
- **Threat**: Malicious overlay or screen recorder captures seed phrase or private key display
- **Mitigations**: `FLAG_SECURE` on all sensitive screens in release builds
- **Residual risk**: Low

### T6: Root Detection
- **Threat**: Rooted device allows other apps to access Tranzo Wallet's private storage
- **Mitigations**: Android Keystore keys are hardware-backed and non-exportable even on rooted devices; encrypted storage adds another layer
- **Recommendations**: Add root/Magisk detection at startup and warn users; do not block usage entirely (non-custodial philosophy)
- **Residual risk**: Medium on rooted devices

### T7: Reverse Engineering
- **Threat**: Attacker decompiles the APK to extract API keys or understand signing logic
- **Mitigations**: R8 obfuscation, API keys injected at build time (not in source), signing logic uses standard cryptographic libraries
- **Residual risk**: Medium -- determined attackers can still reverse-engineer; server-side proxy for API keys is the long-term fix

### T8: Transaction Manipulation
- **Threat**: Malicious app or overlay modifies the recipient address during a send operation
- **Mitigations**: Show a confirmation screen with the full recipient address before signing, require PIN/biometric to confirm
- **Recommendations**: Add address checksum validation, address book with trusted contacts
- **Residual risk**: Low

---

## Audit Schedule

| Audit Type | Frequency | Owner |
|---|---|---|
| Dependency CVE scan | Every CI build | Automated |
| Manual code review (security focus) | Every PR touching `core/crypto`, `core/keystore`, `core/security` | Security lead |
| Third-party penetration test | Before each major release | External vendor |
| Threat model review | Quarterly | Engineering + Security |
