package com.tranzo.wallet.feature.payments

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.tranzo.wallet.ui.frost.FrostAppBar
import com.tranzo.wallet.ui.frost.FrostButtonPrimary
import com.tranzo.wallet.ui.frost.FrostCard
import com.tranzo.wallet.ui.frost.FrostDesignTokens

@Composable
fun KycScreen(
    onBackClick: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FrostDesignTokens.frostBackground)
    ) {
        FrostAppBar(
            title = "KYC Verification",
            onBackClick = onBackClick,
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = FrostDesignTokens.spacingMd),
        ) {
            Spacer(modifier = Modifier.height(FrostDesignTokens.spacingLg))

            // Stepper
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                StepIndicator(
                    stepNumber = 1,
                    label = "Personal Info",
                    icon = Icons.Default.Person,
                    isActive = true,
                    isCompleted = false,
                )

                StepConnector()

                StepIndicator(
                    stepNumber = 2,
                    label = "Documents",
                    icon = Icons.Default.CloudUpload,
                    isActive = false,
                    isCompleted = false,
                )

                StepConnector()

                StepIndicator(
                    stepNumber = 3,
                    label = "Verification",
                    icon = Icons.Default.VerifiedUser,
                    isActive = false,
                    isCompleted = false,
                )
            }

            Spacer(modifier = Modifier.height(FrostDesignTokens.spacingXl))

            // Step 1: Personal Info (disabled/coming soon)
            FrostCard(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Step 1: Personal Information",
                    style = FrostDesignTokens.title,
                )

                Spacer(modifier = Modifier.height(FrostDesignTokens.spacingSm))

                Text(
                    text = "Coming Soon",
                    style = FrostDesignTokens.caption.copy(
                        color = FrostDesignTokens.frostWarning,
                    ),
                )

                Spacer(modifier = Modifier.height(FrostDesignTokens.spacingMd))

                // Placeholder form fields
                DisabledFormField(label = "Full Name", placeholder = "Enter your full name")

                Spacer(modifier = Modifier.height(FrostDesignTokens.spacingSm))

                DisabledFormField(label = "Email", placeholder = "Enter your email address")

                Spacer(modifier = Modifier.height(FrostDesignTokens.spacingSm))

                DisabledFormField(label = "Date of Birth", placeholder = "DD/MM/YYYY")

                Spacer(modifier = Modifier.height(FrostDesignTokens.spacingSm))

                DisabledFormField(label = "Country", placeholder = "Select your country")
            }

            Spacer(modifier = Modifier.height(FrostDesignTokens.spacingMd))

            // Step 2: Document Upload
            FrostCard(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Step 2: Document Upload",
                    style = FrostDesignTokens.title.copy(
                        color = FrostDesignTokens.frostSlate,
                    ),
                )

                Spacer(modifier = Modifier.height(FrostDesignTokens.spacingSm))

                Text(
                    text = "Coming Soon",
                    style = FrostDesignTokens.caption.copy(
                        color = FrostDesignTokens.frostWarning,
                    ),
                )

                Spacer(modifier = Modifier.height(FrostDesignTokens.spacingMd))

                // Upload placeholders
                UploadPlaceholder(label = "Government ID (Front)")

                Spacer(modifier = Modifier.height(FrostDesignTokens.spacingSm))

                UploadPlaceholder(label = "Government ID (Back)")

                Spacer(modifier = Modifier.height(FrostDesignTokens.spacingSm))

                UploadPlaceholder(label = "Selfie with ID")
            }

            Spacer(modifier = Modifier.height(FrostDesignTokens.spacingMd))

            // Step 3: Verification
            FrostCard(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Step 3: Verification",
                    style = FrostDesignTokens.title.copy(
                        color = FrostDesignTokens.frostSlate,
                    ),
                )

                Spacer(modifier = Modifier.height(FrostDesignTokens.spacingSm))

                Text(
                    text = "Coming Soon",
                    style = FrostDesignTokens.caption.copy(
                        color = FrostDesignTokens.frostWarning,
                    ),
                )

                Spacer(modifier = Modifier.height(FrostDesignTokens.spacingMd))

                Text(
                    text = "Your documents will be verified by our KYC partner. This process typically takes 1-3 business days.",
                    style = FrostDesignTokens.body.copy(
                        color = FrostDesignTokens.frostGray,
                    ),
                )
            }

            Spacer(modifier = Modifier.height(FrostDesignTokens.spacingLg))

            // Submit button (disabled)
            FrostButtonPrimary(
                text = "Submit for Verification",
                onClick = { /* Coming soon */ },
                enabled = false,
            )

            Spacer(modifier = Modifier.height(FrostDesignTokens.spacingXl))
        }
    }
}

@Composable
private fun StepIndicator(
    stepNumber: Int,
    label: String,
    icon: ImageVector,
    isActive: Boolean,
    isCompleted: Boolean
) {
    val circleColor = when {
        isCompleted -> FrostDesignTokens.frostSuccess
        isActive -> FrostDesignTokens.icyBlue
        else -> FrostDesignTokens.frostSlate
    }

    val textColor = when {
        isCompleted -> FrostDesignTokens.frostSuccess
        isActive -> FrostDesignTokens.frostWhite
        else -> FrostDesignTokens.frostSlate
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Surface(
            modifier = Modifier.size(40.dp),
            shape = CircleShape,
            color = circleColor.copy(alpha = 0.2f),
            border = androidx.compose.foundation.BorderStroke(
                width = 2.dp,
                color = circleColor,
            ),
        ) {
            Box(contentAlignment = Alignment.Center) {
                if (isCompleted) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Completed",
                        tint = circleColor,
                        modifier = Modifier.size(20.dp),
                    )
                } else {
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        tint = circleColor,
                        modifier = Modifier.size(20.dp),
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(FrostDesignTokens.spacingXs))

        Text(
            text = label,
            style = FrostDesignTokens.caption.copy(color = textColor),
        )
    }
}

@Composable
private fun StepConnector() {
    Box(
        modifier = Modifier
            .width(32.dp)
            .height(2.dp)
            .background(FrostDesignTokens.frostSlate),
    )
}

@Composable
private fun DisabledFormField(
    label: String,
    placeholder: String
) {
    Column {
        Text(
            text = label,
            style = FrostDesignTokens.caption,
        )

        Spacer(modifier = Modifier.height(FrostDesignTokens.spacingXs))

        OutlinedTextField(
            value = "",
            onValueChange = { /* Disabled */ },
            modifier = Modifier.fillMaxWidth(),
            enabled = false,
            placeholder = {
                Text(
                    text = placeholder,
                    style = FrostDesignTokens.body.copy(
                        color = FrostDesignTokens.frostSlate,
                    ),
                )
            },
            shape = RoundedCornerShape(FrostDesignTokens.radiusSm),
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = FrostDesignTokens.frostSlate,
                disabledBorderColor = FrostDesignTokens.frostSlate.copy(alpha = 0.5f),
                disabledContainerColor = FrostDesignTokens.frostSurface.copy(alpha = 0.5f),
            ),
        )
    }
}

@Composable
private fun UploadPlaceholder(label: String) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .border(
                width = 1.dp,
                color = FrostDesignTokens.frostSlate.copy(alpha = 0.5f),
                shape = RoundedCornerShape(FrostDesignTokens.radiusSm),
            ),
        shape = RoundedCornerShape(FrostDesignTokens.radiusSm),
        color = FrostDesignTokens.frostSurface.copy(alpha = 0.3f),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(FrostDesignTokens.spacingMd),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Icon(
                imageVector = Icons.Default.CloudUpload,
                contentDescription = "Upload",
                tint = FrostDesignTokens.frostSlate,
                modifier = Modifier.size(24.dp),
            )

            Spacer(modifier = Modifier.height(FrostDesignTokens.spacingXs))

            Text(
                text = label,
                style = FrostDesignTokens.caption.copy(
                    color = FrostDesignTokens.frostSlate,
                ),
            )
        }
    }
}
