package com.tranzo.wallet.ui.frost

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * Frosted outlined text field with icyBlue focus border,
 * optional password visibility toggle, and error display.
 */
@Composable
fun SecureInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    isPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    errorMessage: String? = null
) {
    var passwordVisible by remember { mutableStateOf(false) }

    val visualTransformation = if (isPassword && !passwordVisible) {
        PasswordVisualTransformation()
    } else {
        VisualTransformation.None
    }

    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = {
                Text(
                    text = label,
                    style = FrostDesignTokens.caption,
                )
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = errorMessage != null,
            visualTransformation = visualTransformation,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            shape = RoundedCornerShape(FrostDesignTokens.radiusMd),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = FrostDesignTokens.frostWhite,
                unfocusedTextColor = FrostDesignTokens.frostWhite,
                cursorColor = FrostDesignTokens.icyBlue,
                focusedBorderColor = FrostDesignTokens.icyBlue,
                unfocusedBorderColor = FrostDesignTokens.frostSlate,
                errorBorderColor = FrostDesignTokens.frostError,
                focusedLabelColor = FrostDesignTokens.icyBlue,
                unfocusedLabelColor = FrostDesignTokens.frostGray,
                errorLabelColor = FrostDesignTokens.frostError,
                focusedContainerColor = FrostDesignTokens.frostWhite.copy(alpha = 0.06f),
                unfocusedContainerColor = FrostDesignTokens.frostWhite.copy(alpha = 0.04f),
                errorContainerColor = FrostDesignTokens.frostError.copy(alpha = 0.04f),
            ),
            trailingIcon = if (isPassword) {
                {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Filled.Visibility
                            else Icons.Filled.VisibilityOff,
                            contentDescription = if (passwordVisible) "Hide password"
                            else "Show password",
                            tint = FrostDesignTokens.frostGray,
                        )
                    }
                }
            } else null,
        )

        if (errorMessage != null) {
            Spacer(modifier = Modifier.height(FrostDesignTokens.spacingXs))
            Text(
                text = errorMessage,
                style = FrostDesignTokens.caption,
                color = FrostDesignTokens.frostError,
                modifier = Modifier.padding(start = FrostDesignTokens.spacingMd),
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0E1A)
@Composable
private fun SecureInputFieldPreview() {
    Column(modifier = Modifier.padding(16.dp)) {
        SecureInputField(
            value = "hello@tranzo.io",
            onValueChange = {},
            label = "Email",
        )
        Spacer(modifier = Modifier.height(16.dp))
        SecureInputField(
            value = "secret",
            onValueChange = {},
            label = "Password",
            isPassword = true,
        )
        Spacer(modifier = Modifier.height(16.dp))
        SecureInputField(
            value = "",
            onValueChange = {},
            label = "Seed Phrase",
            errorMessage = "Invalid mnemonic phrase",
        )
    }
}
