package com.tranzo.wallet.feature.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Brightness6
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tranzo.wallet.domain.model.Network
import com.tranzo.wallet.ui.frost.FrostAppBar
import com.tranzo.wallet.ui.frost.FrostCard
import com.tranzo.wallet.ui.frost.FrostDesignTokens

@Composable
fun SettingsScreen(
    onBackClick: (() -> Unit)? = null,
    onChangePinClick: () -> Unit = {},
    onShowSeedPhraseClick: () -> Unit = {},
    onShowPrivateKeyClick: () -> Unit = {},
    onCustomRpcClick: () -> Unit = {},
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FrostDesignTokens.frostBackground)
    ) {
        FrostAppBar(
            title = "Settings",
            onBackClick = onBackClick,
        )

        LazyColumn(
            contentPadding = PaddingValues(
                horizontal = FrostDesignTokens.spacingMd,
                vertical = FrostDesignTokens.spacingSm,
            ),
            verticalArrangement = Arrangement.spacedBy(FrostDesignTokens.spacingMd),
        ) {
            // ── Security Section ─────────────────────────────────────────
            item {
                SectionHeader(title = "Security")
            }

            item {
                FrostCard(modifier = Modifier.fillMaxWidth()) {
                    SettingsRow(
                        icon = Icons.Default.Lock,
                        label = "Change PIN",
                        onClick = onChangePinClick,
                    )

                    Spacer(modifier = Modifier.height(FrostDesignTokens.spacingSm))

                    SettingsToggleRow(
                        icon = Icons.Default.Fingerprint,
                        label = "Biometric Authentication",
                        checked = state.biometricEnabled,
                        onCheckedChange = { viewModel.toggleBiometric(it) },
                    )

                    Spacer(modifier = Modifier.height(FrostDesignTokens.spacingSm))

                    SettingsRow(
                        icon = Icons.Default.Visibility,
                        label = "Show Seed Phrase",
                        onClick = onShowSeedPhraseClick,
                    )

                    Spacer(modifier = Modifier.height(FrostDesignTokens.spacingSm))

                    SettingsRow(
                        icon = Icons.Default.Key,
                        label = "Show Private Key",
                        onClick = onShowPrivateKeyClick,
                    )
                }
            }

            // ── Network Section ──────────────────────────────────────────
            item {
                SectionHeader(title = "Network")
            }

            item {
                FrostCard(modifier = Modifier.fillMaxWidth()) {
                    SettingsRow(
                        icon = Icons.Default.Language,
                        label = "Network",
                        subtitle = state.selectedNetwork.name,
                        onClick = { /* Open network selector */ },
                    )

                    Spacer(modifier = Modifier.height(FrostDesignTokens.spacingSm))

                    SettingsRow(
                        icon = Icons.Default.Wifi,
                        label = "Custom RPC",
                        onClick = onCustomRpcClick,
                    )
                }
            }

            // ── Preferences Section ──────────────────────────────────────
            item {
                SectionHeader(title = "Preferences")
            }

            item {
                FrostCard(modifier = Modifier.fillMaxWidth()) {
                    SettingsRow(
                        icon = Icons.Default.CurrencyExchange,
                        label = "Currency",
                        subtitle = state.selectedCurrency,
                        onClick = { /* Open currency selector */ },
                    )

                    Spacer(modifier = Modifier.height(FrostDesignTokens.spacingSm))

                    SettingsRow(
                        icon = Icons.Default.Brightness6,
                        label = "Theme",
                        subtitle = "Dark",
                        onClick = { /* Open theme selector */ },
                    )
                }
            }

            // ── Developer Section ────────────────────────────────────────
            item {
                SectionHeader(title = "Developer")
            }

            item {
                FrostCard(modifier = Modifier.fillMaxWidth()) {
                    SettingsToggleRow(
                        icon = Icons.Default.Code,
                        label = "Developer Mode",
                        checked = state.developerModeEnabled,
                        onCheckedChange = { viewModel.toggleDeveloperMode(it) },
                    )

                    Spacer(modifier = Modifier.height(FrostDesignTokens.spacingSm))

                    SettingsToggleRow(
                        icon = Icons.Default.BugReport,
                        label = "Testnet",
                        checked = state.testnetEnabled,
                        onCheckedChange = { viewModel.toggleTestnet(it) },
                    )
                }
            }

            // ── About Section ────────────────────────────────────────────
            item {
                SectionHeader(title = "About")
            }

            item {
                FrostCard(modifier = Modifier.fillMaxWidth()) {
                    SettingsRow(
                        icon = Icons.Default.Info,
                        label = "Version",
                        subtitle = state.appVersion,
                        onClick = null,
                    )

                    Spacer(modifier = Modifier.height(FrostDesignTokens.spacingSm))

                    SettingsRow(
                        icon = Icons.Default.Link,
                        label = "GitHub",
                        subtitle = "github.com/tranzo-wallet",
                        onClick = { /* Open browser */ },
                    )
                }
            }

            // Bottom spacing
            item {
                Spacer(modifier = Modifier.height(FrostDesignTokens.spacingXl))
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        style = FrostDesignTokens.label.copy(
            color = FrostDesignTokens.icyBlue,
        ),
        modifier = Modifier.padding(
            top = FrostDesignTokens.spacingSm,
            bottom = FrostDesignTokens.spacingXs,
        ),
    )
}

@Composable
private fun SettingsRow(
    icon: ImageVector,
    label: String,
    subtitle: String? = null,
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (onClick != null) Modifier.clickable(onClick = onClick)
                else Modifier
            )
            .padding(vertical = FrostDesignTokens.spacingSm),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            modifier = Modifier.size(24.dp),
            tint = FrostDesignTokens.frostGray,
        )

        Spacer(modifier = Modifier.width(FrostDesignTokens.spacingMd))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = FrostDesignTokens.label,
            )

            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = FrostDesignTokens.caption,
                )
            }
        }

        if (onClick != null) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Navigate",
                modifier = Modifier.size(20.dp),
                tint = FrostDesignTokens.frostSlate,
            )
        }
    }
}

@Composable
private fun SettingsToggleRow(
    icon: ImageVector,
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = FrostDesignTokens.spacingSm),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            modifier = Modifier.size(24.dp),
            tint = FrostDesignTokens.frostGray,
        )

        Spacer(modifier = Modifier.width(FrostDesignTokens.spacingMd))

        Text(
            text = label,
            style = FrostDesignTokens.label,
            modifier = Modifier.weight(1f),
        )

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = FrostDesignTokens.frostWhite,
                checkedTrackColor = FrostDesignTokens.icyBlue,
                uncheckedThumbColor = FrostDesignTokens.frostGray,
                uncheckedTrackColor = FrostDesignTokens.frostSlate,
                uncheckedBorderColor = FrostDesignTokens.frostSlate,
            ),
        )
    }
}
