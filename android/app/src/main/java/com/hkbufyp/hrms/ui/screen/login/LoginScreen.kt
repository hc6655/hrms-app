package com.hkbufyp.hrms.ui.screen.login

import android.app.Activity
import android.inputmethodservice.Keyboard
import android.security.keystore.KeyProperties
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowCircleRight
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
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
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.hkbufyp.hrms.R
import com.hkbufyp.hrms.domain.model.UserJwt
import com.hkbufyp.hrms.shared.SharedViewModel
import com.hkbufyp.hrms.ui.components.LaunchBiometricPrompt
import com.hkbufyp.hrms.util.isEmptyOrBlank
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.crypto.Cipher
import kotlin.reflect.jvm.internal.impl.descriptors.Visibilities.Local

@Composable
fun LoginScreen(
    snackbarHostState: SnackbarHostState,
    sharedViewModel: SharedViewModel,
    loginViewModel: LoginViewModel,
    onNavToHomeScreen: (userJwt: UserJwt?) -> Unit) {

    val loginUiState by loginViewModel.uiState.collectAsState()
    val sharedUiState by sharedViewModel.sharedUiState.collectAsState()
    val focusManager = LocalFocusManager.current


    LaunchedEffect(key1 = true) {
        sharedViewModel.isShowAppBar(false)
        sharedViewModel.isEnableSwipeSideBar(false)
        /* ---- TEST ONLY ---- */
        /*if (!sharedViewModel.isAutoLoggedIn) {
            sharedViewModel.isAutoLoggedIn = true
            //loginViewModel.onEvent(LoginEvent.UsernameChanged("23000"))
            loginViewModel.onEvent(LoginEvent.PasswordChanged("hrmsadmin"))
            //loginViewModel.onEvent(LoginEvent.Login)
        }*/
        /* ------------------- */

        if (sharedUiState.employeeInfo?.employeeId?.isNotEmpty() == true) {
            loginViewModel.onEvent(LoginEvent.UsernameChanged(sharedUiState.employeeInfo?.employeeId ?: ""))
        }

        launch {
            loginViewModel.isShowSnackbar.collectLatest {
                if (it) {
                    snackbarHostState.showSnackbar(loginUiState.message)
                }
            }
        }

        launch {
            loginViewModel.isLoggedIn.collectLatest {
                if (it) {
                    onNavToHomeScreen(loginUiState.userJwt)
                }
            }
        }
    }

    if (loginUiState.showBiometricPrompt) {
        LaunchBiometricPrompt(
            cryptoObject = loginUiState.cryptoObject!!,
            onSucceeded = {
                loginViewModel.onEvent(LoginEvent.ShowBiometricPrompt(false))
                loginViewModel.onEvent(LoginEvent.LoginWithBiometric(it.cryptoObject!!))

            },
            onError = { errorCode, errString ->
                loginViewModel.onEvent(LoginEvent.ShowBiometricPrompt(false))
                println("Biometric authentication error with code: $errorCode, reason: $errString")
            },
            onFailed = {
                loginViewModel.onEvent(LoginEvent.ShowBiometricPrompt(false))
                println("Biometric authentication failed")
            },
        )
    }

    if (loginUiState.shouldShowBiometricAlert) {
        AlertDialog(
            onDismissRequest = {
                loginViewModel.onEvent(LoginEvent.DismissBiometricAlert)
            },
            confirmButton = {
                TextButton(onClick = {
                    loginViewModel.onEvent(LoginEvent.DismissBiometricAlert)
                }
                ) {
                    Text(text = "OK")
                }
            },
            icon = {
                Icon(imageVector = Icons.Filled.Fingerprint, contentDescription = "Fingerprint")
            },
            title = {
                Text(text = "Biometric Authentication Expired")
            },
            text = {
                Text(text = "You seems updated your fingerprint recently, please re-register your fingerprint")
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight(0.5f)
                .fillMaxWidth()
                .padding(top = 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.login_bg_new),
                contentDescription = "")
        }

        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp, alignment = Alignment.Top),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "HRMS",
                color = Color.Black,
                fontSize = 50.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LoginCard {
                if (sharedUiState.employeeInfo?.employeeId.isNullOrEmpty()) {
                    LoginFieldGroup(
                        employeeId = loginUiState.userName,
                        password = loginUiState.password,
                        onEmployeeIdChanged = {
                            loginViewModel.onEvent(LoginEvent.UsernameChanged(it))
                        },
                        onPasswordChanged = {
                            loginViewModel.onEvent(LoginEvent.PasswordChanged(it))
                        },
                        onLoginClicked = {
                            focusManager.clearFocus()
                            if (!loginUiState.isLoggingIn) {
                                loginViewModel.onEvent(LoginEvent.Login)
                            }
                        },
                        enabled = !loginUiState.isLoggingIn,
                        isLoading = loginUiState.isLoggingIn,
                    )
                } else {
                    LoginWelcomeText(name = sharedUiState.employeeInfo?.employeeName ?: "")
                    LoginButtonGroup(
                        password = loginUiState.password,
                        onPasswordChanged = {
                            loginViewModel.onEvent(LoginEvent.PasswordChanged(it))
                        },
                        onLoginClicked = {
                            focusManager.clearFocus()
                            if (!loginUiState.isLoggingIn && !loginUiState.isLoggingInWithBiometric) {
                                loginViewModel.onEvent(LoginEvent.Login)
                            }
                        },
                        onBiometricClicked = {
                            if (!loginUiState.isLoggingIn && !loginUiState.isLoggingInWithBiometric) {
                                loginViewModel.onEvent(LoginEvent.ShowBiometricPrompt(true))
                            }
                        },
                        isLoading = loginUiState.isLoggingIn,
                        passwordEnabled = !loginUiState.isLoggingIn,
                        isBiometricLoading = loginUiState.isLoggingInWithBiometric,
                        isBiometricEnabled = loginUiState.biometricStatus?.canAuthByBiometric() ?: false
                    )
                }

                LoginAbout()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun DefaultPreview() {
}