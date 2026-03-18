package com.tranzo.wallet.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tranzo.wallet.feature.onboarding.SplashScreen
import com.tranzo.wallet.feature.onboarding.IntroScreen
import com.tranzo.wallet.feature.onboarding.CreateWalletScreen
import com.tranzo.wallet.feature.onboarding.ImportWalletScreen
import com.tranzo.wallet.feature.onboarding.BackupSeedScreen
import com.tranzo.wallet.feature.onboarding.PinSetupScreen
import com.tranzo.wallet.feature.wallet.MainScreen

object Routes {
    const val SPLASH = "splash"
    const val INTRO = "intro"
    const val CREATE_WALLET = "create_wallet"
    const val IMPORT_WALLET = "import_wallet"
    const val BACKUP_SEED = "backup_seed"
    const val PIN_SETUP = "pin_setup"
    const val MAIN = "main"
    const val SEND_TOKEN = "send_token"
    const val RECEIVE_TOKEN = "receive_token"
    const val TOKEN_DETAIL = "token_detail/{tokenId}"
    const val NFT_GALLERY = "nft_gallery"
    const val SETTINGS = "settings"
    const val TRANSACTION_HISTORY = "transaction_history"
}

@Composable
fun TranzoNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH
    ) {
        composable(Routes.SPLASH) {
            SplashScreen(
                onNavigateToIntro = {
                    navController.navigate(Routes.INTRO) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                },
                onNavigateToMain = {
                    navController.navigate(Routes.MAIN) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.INTRO) {
            IntroScreen(
                onCreateWallet = { navController.navigate(Routes.CREATE_WALLET) },
                onImportWallet = { navController.navigate(Routes.IMPORT_WALLET) }
            )
        }

        composable(
            route = "${Routes.BACKUP_SEED}?mnemonic={mnemonic}"
        ) { backStackEntry ->
            val mnemonic = backStackEntry.arguments?.getString("mnemonic") ?: ""
            BackupSeedScreen(
                mnemonic = mnemonic,
                onBackupComplete = {
                    navController.navigate(Routes.PIN_SETUP) {
                        popUpTo(Routes.INTRO) { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.CREATE_WALLET) {
            CreateWalletScreen(
                onWalletCreated = { _, mnemonic ->
                    navController.navigate("${Routes.BACKUP_SEED}?mnemonic=$mnemonic")
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.IMPORT_WALLET) {
            ImportWalletScreen(
                onWalletImported = {
                    navController.navigate(Routes.PIN_SETUP) {
                        popUpTo(Routes.INTRO) { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.PIN_SETUP) {
            PinSetupScreen(
                onPinSet = {
                    navController.navigate(Routes.MAIN) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.MAIN) {
            MainScreen(navController = navController)
        }
    }
}
