package com.tranzo.wallet.feature.onboarding

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tranzo.wallet.ui.theme.FrostBackground
import com.tranzo.wallet.ui.theme.IcyBlue
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onNavigateToIntro: () -> Unit,
    onNavigateToMain: () -> Unit,
    viewModel: SplashViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    var startAnimation by remember { mutableStateOf(false) }
    val alphaAnim by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1000),
        label = "splash_fade_in"
    )

    LaunchedEffect(Unit) {
        startAnimation = true
    }

    LaunchedEffect(state) {
        if (state != SplashState.Loading) {
            delay(2000L)
            when (state) {
                SplashState.NavigateToIntro -> onNavigateToIntro()
                SplashState.NavigateToMain -> onNavigateToMain()
                else -> Unit
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FrostBackground),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "TRANZO",
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            color = IcyBlue,
            modifier = Modifier.alpha(alphaAnim),
            letterSpacing = 8.sp
        )
    }
}
