package com.hkbufyp.hrms.ui.screen.middleware.sensitive_auth

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Biotech
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.rounded.Biotech
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hkbufyp.hrms.shared.SharedViewModel

@Composable
fun SensitiveAuthScreen(
    snackbarHostState: SnackbarHostState,
    sharedViewModel: SharedViewModel,
    sensitiveAuthViewModel: SensitiveAuthViewModel,
    onAuthSucceed: () -> Unit
) {
    Column (
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = "Hello world")
        Button(onClick = { onAuthSucceed() }) {
            Text(text = "Auth Success")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun SensitiveAuthPreview() {

    var password by remember { mutableStateOf("") }
    val isLoading by remember { mutableStateOf(false) }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        ElevatedCard (
            modifier = Modifier
                .size(480.dp)
                .padding(24.dp),
            colors = CardDefaults.elevatedCardColors(
                containerColor = Color(240, 240, 240),
                contentColor = Color.Black
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            )
        ) {
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Icon(
                    imageVector = Icons.Filled.Warning,
                    contentDescription = "Warning",
                    tint = Color(255, 128, 0),
                    modifier = Modifier.size(100.dp)
                )

                Text(
                    text = "Authentication needed",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(top = 20.dp)
                )

                Text(
                    text = "You are accessing or requesting sensitive data, please input your password for verifying your identity.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 20.dp)
                )

                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp),
                    shape = RectangleShape
                ) {
                    Icon(
                        imageVector = Icons.Filled.Fingerprint,
                        contentDescription = "fingerprint"
                    )
                    Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                    Text(text = "Biometric authentication")
                }

                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RectangleShape
                ) {
                    Icon(
                        imageVector = Icons.Filled.Lock,
                        contentDescription = "Lock"
                    )
                    Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                    Text(text = "Password authentication")
                }

                /*Row(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    BasicTextField(
                        value = password,
                        onValueChange = { password = it },
                        modifier = Modifier
                            .height(45.dp)
                            .fillMaxWidth(0.75f)
                            .padding(top = 5.dp),
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation()
                    ) {
                        OutlinedTextFieldDefaults.DecorationBox(
                            value = password,
                            innerTextField = it,
                            enabled = true,
                            singleLine = true,
                            visualTransformation = PasswordVisualTransformation(),
                            interactionSource = remember { MutableInteractionSource() },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = "Lock"
                                )
                            },
                            contentPadding = OutlinedTextFieldDefaults.contentPadding(
                                top = 0.dp,
                                bottom = 0.dp
                            ),
                            label = {
                                Text(text = "Password",)
                            }
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    FilledIconButton(
                        onClick = {  },
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = Color(0, 102, 204))
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(ButtonDefaults.IconSize),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "Login",
                                tint = Color.White
                            )
                        }

                    }
                }*/

            }
        }
    }
}