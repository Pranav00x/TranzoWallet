@file:Suppress("SetJavaScriptEnabled")

package com.tranzo.wallet.feature.wallet

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.tranzo.wallet.domain.model.Network
import com.tranzo.wallet.ui.frost.FrostCard
import com.tranzo.wallet.ui.frost.FrostDesignTokens
import org.json.JSONObject

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun DAppBrowserScreen(
    viewModel: DAppBrowserViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var urlInput by remember(state.currentUrl) { mutableStateOf(state.currentUrl) }
    var webView by remember { mutableStateOf<WebView?>(null) }
    var showNetworkSheet by remember { mutableStateOf(false) }
    var pageProgress by remember { mutableIntStateOf(0) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    // Collect one-shot JS execution events
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is BrowserEvent.ExecuteJs -> {
                    webView?.evaluateJavascript(event.script, null)
                }
            }
        }
    }

    // React to URL changes from the ViewModel (e.g. user submitted a new URL)
    LaunchedEffect(state.url) {
        webView?.loadUrl(state.url)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FrostDesignTokens.frostBackground)
            .windowInsetsPadding(WindowInsets.statusBars)
    ) {
        // ── Top bar: Network chip + URL bar ──────────────────────────
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // Network chip
                Surface(
                    onClick = { showNetworkSheet = true },
                    shape = RoundedCornerShape(20.dp),
                    color = FrostDesignTokens.icyBlue.copy(alpha = 0.15f),
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(
                                    if (state.isConnected) FrostDesignTokens.frostSuccess
                                    else FrostDesignTokens.frostGray
                                )
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = state.selectedNetwork.name,
                            style = FrostDesignTokens.caption.copy(
                                color = FrostDesignTokens.icyBlue,
                                fontSize = 11.sp,
                            ),
                            maxLines = 1,
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // URL bar
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = FrostDesignTokens.frostSurface,
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            imageVector = if (state.currentUrl.startsWith("https"))
                                Icons.Default.Lock else Icons.Default.Language,
                            contentDescription = null,
                            tint = FrostDesignTokens.frostGray,
                            modifier = Modifier.size(14.dp),
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        BasicTextField(
                            value = urlInput,
                            onValueChange = { urlInput = it },
                            modifier = Modifier.weight(1f),
                            textStyle = TextStyle(
                                color = FrostDesignTokens.frostWhite,
                                fontSize = 13.sp,
                            ),
                            singleLine = true,
                            cursorBrush = SolidColor(FrostDesignTokens.icyBlue),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go),
                            keyboardActions = KeyboardActions(
                                onGo = {
                                    viewModel.onUrlSubmit(urlInput)
                                    keyboardController?.hide()
                                    focusManager.clearFocus()
                                }
                            ),
                        )
                        if (urlInput.isNotEmpty()) {
                            IconButton(
                                onClick = { urlInput = "" },
                                modifier = Modifier.size(20.dp),
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Clear",
                                    tint = FrostDesignTokens.frostGray,
                                    modifier = Modifier.size(14.dp),
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.width(4.dp))

                // Refresh
                IconButton(
                    onClick = { webView?.reload() },
                    modifier = Modifier.size(36.dp),
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh",
                        tint = FrostDesignTokens.frostGray,
                        modifier = Modifier.size(18.dp),
                    )
                }
            }

            // Progress bar
            AnimatedVisibility(
                visible = state.isLoading,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                LinearProgressIndicator(
                    progress = { pageProgress / 100f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                        .height(2.dp)
                        .clip(RoundedCornerShape(1.dp)),
                    color = FrostDesignTokens.icyBlue,
                    trackColor = Color.Transparent,
                )
            }
        }

        // ── WebView ──────────────────────────────────────────────────
        Box(modifier = Modifier.weight(1f)) {
            AndroidView(
                factory = { context ->
                    WebView(context).apply {
                        settings.javaScriptEnabled = true
                        settings.domStorageEnabled = true
                        settings.databaseEnabled = true
                        settings.setSupportMultipleWindows(false)
                        settings.javaScriptCanOpenWindowsAutomatically = true
                        settings.loadWithOverviewMode = true
                        settings.useWideViewPort = true
                        settings.builtInZoomControls = true
                        settings.displayZoomControls = false
                        settings.allowContentAccess = true
                        settings.mediaPlaybackRequiresUserGesture = false
                        settings.userAgentString = settings.userAgentString + " TranzoWallet"

                        // JS bridge
                        addJavascriptInterface(
                            TranzoJsBridge(viewModel),
                            "_TranzoProvider"
                        )

                        webViewClient = object : WebViewClient() {
                            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                                super.onPageStarted(view, url, favicon)
                                url?.let { viewModel.onPageStarted(it) }
                                // Inject the provider early
                                view?.evaluateJavascript(viewModel.getProviderInjectionJs(), null)
                            }

                            override fun onPageFinished(view: WebView?, url: String?) {
                                super.onPageFinished(view, url)
                                url?.let { viewModel.onPageFinished(it) }
                                // Re-inject to be safe
                                view?.evaluateJavascript(viewModel.getProviderInjectionJs(), null)
                                viewModel.updateNavState(
                                    canGoBack = view?.canGoBack() ?: false,
                                    canGoForward = view?.canGoForward() ?: false,
                                )
                            }

                            override fun shouldOverrideUrlLoading(
                                view: WebView?,
                                request: WebResourceRequest?
                            ): Boolean {
                                return false
                            }
                        }

                        webChromeClient = object : WebChromeClient() {
                            override fun onReceivedTitle(view: WebView?, title: String?) {
                                super.onReceivedTitle(view, title)
                                title?.let { viewModel.onTitleChanged(it) }
                            }

                            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                                super.onProgressChanged(view, newProgress)
                                pageProgress = newProgress
                            }
                        }

                        webView = this
                        loadUrl(state.url)
                    }
                },
                modifier = Modifier.fillMaxSize(),
            )
        }

        // ── Bottom toolbar ───────────────────────────────────────────
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = FrostDesignTokens.frostSurface,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .navigationBarsPadding(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(
                    onClick = { webView?.goBack() },
                    enabled = state.canGoBack,
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = if (state.canGoBack) FrostDesignTokens.frostWhite
                        else FrostDesignTokens.frostSlate,
                        modifier = Modifier.size(22.dp),
                    )
                }

                IconButton(
                    onClick = { webView?.goForward() },
                    enabled = state.canGoForward,
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Forward",
                        tint = if (state.canGoForward) FrostDesignTokens.frostWhite
                        else FrostDesignTokens.frostSlate,
                        modifier = Modifier.size(22.dp),
                    )
                }

                IconButton(
                    onClick = {
                        viewModel.onUrlSubmit("https://app.uniswap.org")
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "Home",
                        tint = FrostDesignTokens.frostWhite,
                        modifier = Modifier.size(22.dp),
                    )
                }

                IconButton(onClick = { showNetworkSheet = true }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Network",
                        tint = FrostDesignTokens.frostWhite,
                        modifier = Modifier.size(22.dp),
                    )
                }
            }
        }
    }

    // ── Network selector overlay ─────────────────────────────────────
    if (showNetworkSheet) {
        NetworkSelectorSheet(
            currentNetwork = state.selectedNetwork,
            onNetworkSelected = { network ->
                viewModel.selectNetwork(network)
                showNetworkSheet = false
            },
            onDismiss = { showNetworkSheet = false },
        )
    }
}

/**
 * Overlay sheet for selecting the active blockchain network.
 */
@Composable
private fun NetworkSelectorSheet(
    currentNetwork: Network,
    onNetworkSelected: (Network) -> Unit,
    onDismiss: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.55f))
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onDismiss,
            ),
        contentAlignment = Alignment.BottomCenter,
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = { /* consume */ },
                ),
            shape = RoundedCornerShape(
                topStart = FrostDesignTokens.radiusXl,
                topEnd = FrostDesignTokens.radiusXl,
            ),
            color = FrostDesignTokens.frostSurface.copy(alpha = 0.97f),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(FrostDesignTokens.spacingLg),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // Drag handle
                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(100.dp))
                        .background(FrostDesignTokens.frostGray.copy(alpha = 0.4f))
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Select Network",
                    style = FrostDesignTokens.headline,
                    color = FrostDesignTokens.frostWhite,
                )

                Spacer(modifier = Modifier.height(16.dp))

                Network.DEFAULT_NETWORKS.filter { !it.isTestnet }.forEach { network ->
                    val isSelected = network.chainId == currentNetwork.chainId
                    FrostCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        onClick = { onNetworkSelected(network) },
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(
                                            if (isSelected) FrostDesignTokens.icyBlue.copy(alpha = 0.2f)
                                            else FrostDesignTokens.frostSlate
                                        ),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Text(
                                        text = network.symbol.take(2),
                                        style = FrostDesignTokens.caption.copy(
                                            color = if (isSelected) FrostDesignTokens.icyBlue
                                            else FrostDesignTokens.frostGray,
                                            fontSize = 11.sp,
                                        ),
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = network.name,
                                        style = FrostDesignTokens.label.copy(
                                            color = if (isSelected) FrostDesignTokens.icyBlue
                                            else FrostDesignTokens.frostWhite,
                                        ),
                                    )
                                    Text(
                                        text = network.symbol,
                                        style = FrostDesignTokens.caption,
                                    )
                                }
                            }

                            if (isSelected) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(FrostDesignTokens.frostSuccess)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

/**
 * Android JS interface that bridges dApp JS calls to our ViewModel.
 */
private class TranzoJsBridge(private val viewModel: DAppBrowserViewModel) {

    @JavascriptInterface
    fun postMessage(messageJson: String) {
        try {
            val json = JSONObject(messageJson)
            val id = json.getString("id")
            val method = json.getString("method")
            val params = json.optString("params", "[]")
            viewModel.handleProviderRequest(method, params, id)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
