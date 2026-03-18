package com.tranzo.wallet.feature.wallet

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Token
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Token
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.tranzo.wallet.ui.frost.FrostDesignTokens

private enum class BottomTab(
    val route: String,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    Wallet("tab_wallet", "Wallet", Icons.Filled.AccountBalanceWallet, Icons.Outlined.AccountBalanceWallet),
    Tokens("tab_tokens", "Tokens", Icons.Filled.Token, Icons.Outlined.Token),
    NFTs("tab_nfts", "NFTs", Icons.Filled.PhotoLibrary, Icons.Outlined.PhotoLibrary),
    History("tab_history", "History", Icons.Filled.History, Icons.Outlined.History),
    Settings("tab_settings", "Settings", Icons.Filled.Settings, Icons.Outlined.Settings)
}

@Composable
fun MainScreen(navController: NavController) {
    val bottomNavController = rememberNavController()
    val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        containerColor = FrostDesignTokens.frostBackground,
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp),
                containerColor = FrostDesignTokens.frostSurface,
                tonalElevation = FrostDesignTokens.elevationNone,
            ) {
                BottomTab.entries.forEach { tab ->
                    val selected = currentDestination?.hierarchy?.any { it.route == tab.route } == true

                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            bottomNavController.navigate(tab.route) {
                                popUpTo(bottomNavController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = if (selected) tab.selectedIcon else tab.unselectedIcon,
                                contentDescription = tab.label,
                                modifier = Modifier.size(24.dp),
                            )
                        },
                        label = {
                            Text(
                                text = tab.label,
                                style = FrostDesignTokens.caption,
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = FrostDesignTokens.icyBlue,
                            selectedTextColor = FrostDesignTokens.icyBlue,
                            unselectedIconColor = FrostDesignTokens.frostGray,
                            unselectedTextColor = FrostDesignTokens.frostGray,
                            indicatorColor = FrostDesignTokens.icyBlue.copy(alpha = 0.12f),
                        ),
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = bottomNavController,
            startDestination = BottomTab.Wallet.route,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            enterTransition = {
                fadeIn(animationSpec = tween(200))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(200))
            },
        ) {
            composable(BottomTab.Wallet.route) {
                WalletHomeScreen(
                    onSendClick = { navController.navigate("send_token") },
                    onReceiveClick = { navController.navigate("receive_token") },
                )
            }

            composable(BottomTab.Tokens.route) {
                TokensPlaceholder()
            }

            composable(BottomTab.NFTs.route) {
                NftsPlaceholder()
            }

            composable(BottomTab.History.route) {
                TransactionHistoryScreen()
            }

            composable(BottomTab.Settings.route) {
                SettingsPlaceholder()
            }
        }
    }
}

@Composable
private fun TokensPlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FrostDesignTokens.frostBackground),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "Tokens",
            style = FrostDesignTokens.headline,
        )
    }
}

@Composable
private fun NftsPlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FrostDesignTokens.frostBackground),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "NFTs",
            style = FrostDesignTokens.headline,
        )
    }
}

@Composable
private fun SettingsPlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FrostDesignTokens.frostBackground),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "Settings",
            style = FrostDesignTokens.headline,
        )
    }
}
