package com.tranzo.wallet.feature.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tranzo.wallet.ui.frost.FrostDesignTokens
import com.tranzo.wallet.ui.theme.FrostBackground
import com.tranzo.wallet.ui.theme.FrostBorder
import com.tranzo.wallet.ui.theme.FrostCard
import com.tranzo.wallet.ui.theme.FrostGray
import com.tranzo.wallet.ui.theme.FrostSurface
import com.tranzo.wallet.ui.theme.FrostWarning
import com.tranzo.wallet.ui.theme.FrostWhite
import com.tranzo.wallet.ui.theme.IcyBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackupSeedScreen(
    mnemonic: String,
    onBackupComplete: () -> Unit,
    onBack: () -> Unit
) {
    val words = remember(mnemonic) {
        mnemonic.trim().split("\\s+".toRegex()).filter { it.isNotEmpty() }
    }

    var isBackedUp by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Backup Seed Phrase",
                        color = FrostWhite,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = FrostWhite
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = FrostBackground
                )
            )
        },
        containerColor = FrostBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = FrostDesignTokens.spacingLg)
                .background(FrostBackground)
        ) {
            Spacer(modifier = Modifier.height(FrostDesignTokens.spacingMd))

            Text(
                text = "Write down these 12 words in order and store them in a safe place. This is the only way to recover your wallet.",
                style = FrostDesignTokens.body,
                color = FrostGray
            )

            Spacer(modifier = Modifier.height(FrostDesignTokens.spacingSm))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Never share your seed phrase with anyone!",
                    style = FrostDesignTokens.caption,
                    color = FrostWarning,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(FrostDesignTokens.spacingLg))

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(FrostDesignTokens.spacingSm),
                verticalArrangement = Arrangement.spacedBy(FrostDesignTokens.spacingSm),
                modifier = Modifier.fillMaxWidth()
            ) {
                itemsIndexed(words) { index, word ->
                    SeedWordCard(number = index + 1, word = word)
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    checked = isBackedUp,
                    onCheckedChange = { isBackedUp = it },
                    colors = CheckboxDefaults.colors(
                        checkedColor = IcyBlue,
                        uncheckedColor = FrostGray,
                        checkmarkColor = FrostBackground
                    )
                )
                Spacer(modifier = Modifier.width(FrostDesignTokens.spacingSm))
                Text(
                    text = "I've backed up my seed phrase",
                    style = FrostDesignTokens.body,
                    color = FrostWhite
                )
            }

            Spacer(modifier = Modifier.height(FrostDesignTokens.spacingMd))

            Button(
                onClick = onBackupComplete,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = isBackedUp,
                colors = ButtonDefaults.buttonColors(
                    containerColor = IcyBlue,
                    contentColor = FrostBackground,
                    disabledContainerColor = IcyBlue.copy(alpha = 0.3f),
                    disabledContentColor = FrostBackground.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(FrostDesignTokens.radiusMd)
            ) {
                Text(
                    text = "Continue",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(FrostDesignTokens.spacingXxl))
        }
    }
}

@Composable
private fun SeedWordCard(number: Int, word: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = FrostSurface,
                shape = RoundedCornerShape(FrostDesignTokens.radiusSm)
            )
            .border(
                width = 1.dp,
                color = FrostBorder,
                shape = RoundedCornerShape(FrostDesignTokens.radiusSm)
            )
            .padding(
                horizontal = FrostDesignTokens.spacingSm,
                vertical = FrostDesignTokens.spacingSm
            ),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "$number.",
                style = FrostDesignTokens.caption,
                color = FrostGray,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = word,
                style = FrostDesignTokens.label,
                color = FrostWhite,
                textAlign = TextAlign.Center
            )
        }
    }
}
