package com.hkbufyp.hrms.ui.screen.home

import androidx.biometric.BiometricPrompt.AuthenticationResult

sealed class HomeEvent {
    data object Enter: HomeEvent()
    data class ShowBiometricPrompt(val isShow: Boolean): HomeEvent()
    data class BiometricSucceeded(val result: AuthenticationResult): HomeEvent()
    data class BiometricError(val errorCode: Int): HomeEvent()
}
