package com.hkbufyp.hrms.ui.screen.login

import androidx.biometric.BiometricPrompt.CryptoObject
import com.hkbufyp.hrms.domain.model.BiometricStatus
import com.hkbufyp.hrms.domain.model.UserJwt

data class LoginUiState(
    val isLoggingIn: Boolean = false,
    val isLoggingInWithBiometric: Boolean = false,
    val userName: String = "",
    val password: String = "",
    val message: String = "",
    val userJwt: UserJwt? = null,
    val showBiometricPrompt: Boolean = false,
    val biometricStatus: BiometricStatus? = null,
    val shouldShowBiometricAlert: Boolean = false,
    val cryptoObject: CryptoObject? = null,
    /* ---- TEST ONLY ---- */
    val isTesting: Boolean = true,
    val isAutoLoggedIn: Boolean = false
)
