package com.hkbufyp.hrms.ui.screen.login

import androidx.biometric.BiometricPrompt.CryptoObject

sealed class LoginEvent {
    data object Login: LoginEvent()
    data class ShowBiometricPrompt(val isShow: Boolean): LoginEvent()
    data class UsernameChanged(val name: String): LoginEvent()
    data class PasswordChanged(val password: String): LoginEvent()
    data class LoginWithBiometric(val cryptoObject: CryptoObject): LoginEvent()
    data object DismissBiometricAlert: LoginEvent()
}