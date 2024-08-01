package com.hkbufyp.hrms.ui.screen.login

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LoginCard(content: @Composable ColumnScope.() -> Unit) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 30.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.53f),
        shape = RoundedCornerShape(35.dp),
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = 24.dp,
                vertical = 36.dp
            ),
            content = content
        )
    }
}

@Composable
fun LoginWelcomeText(name: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        Text(
            text = "Welcome Back",
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fontSize = 40.sp
        )

        Text (
            text = name,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fontSize = 35.sp,
            textAlign = TextAlign.Right,
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 24.dp),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginButtonGroup(
    password: String,
    onPasswordChanged: (String) -> Unit,
    onLoginClicked: () -> Unit,
    onBiometricClicked: () -> Unit,
    passwordEnabled: Boolean = true,
    isLoading: Boolean = false,
    isBiometricLoading: Boolean = false,
    isBiometricEnabled: Boolean = true
) {
    var isUsePassword by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(top = 45.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Button(
            onClick = { onBiometricClicked() },
            modifier = Modifier.fillMaxWidth(),
            shape = RectangleShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(255, 102, 102),
                contentColor = Color.White
            ),
            enabled = isBiometricEnabled
        ) {
            if (!isBiometricLoading) {
                Text(text = "Login via biometric authentication")
            } else {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(ButtonDefaults.IconSize),
                    strokeWidth = 2.dp
                )
            }
        }

        if (isUsePassword) {
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                BasicTextField(
                    value = password,
                    onValueChange = { onPasswordChanged(it) },
                    modifier = Modifier
                        .height(45.dp)
                        .fillMaxWidth(0.85f)
                        .padding(top = 5.dp),
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation()
                ) {
                    OutlinedTextFieldDefaults.DecorationBox(
                        value = password,
                        innerTextField = it,
                        enabled = passwordEnabled,
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
                    onClick = { onLoginClicked() },
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
            }
        } else {
            OutlinedButton(
                onClick = { isUsePassword = true },
                modifier = Modifier.fillMaxWidth(),
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Black
                )
            ) {
                Text(text = "Login via password")
            }
        }
    }
}

@Composable
fun LoginAbout() {
    Column(
        modifier = Modifier
            .padding(top = 40.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Text(
            text = "Department of Computer Science, Hong Kong Baptist University",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = Color.Gray,
            fontSize = 12.sp
        )

        Text(
            text = "香港浸會大學電腦科學系",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = Color.Gray,
            fontSize = 12.sp
        )
    }
}

@Composable
fun LoginFieldGroup(
    employeeId: String,
    password: String,
    onEmployeeIdChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onLoginClicked: () -> Unit,
    enabled: Boolean = true,
    isLoading: Boolean = false
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "Welcome",
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fontSize = 40.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        OutlinedTextField(
            value = employeeId,
            onValueChange = { onEmployeeIdChanged(it) },
            label = {
                Text(
                    text = "Employee ID",
                    textAlign = TextAlign.Center
                )
            },
            shape = RectangleShape,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "Employee ID"
                )
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            enabled = enabled
        )

        OutlinedTextField(
            value = password,
            onValueChange = { onPasswordChanged(it) },
            label = {
                Text(
                    text = "Password",
                    textAlign = TextAlign.Center
                )
            },
            shape = RectangleShape,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Lock,
                    contentDescription = "Password"
                )
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            enabled = enabled
        )

        Button(
            onClick = { onLoginClicked() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0, 102, 204)
            )
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(ButtonDefaults.IconSize),
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = "Login"
                )
            }
        }
    }
}