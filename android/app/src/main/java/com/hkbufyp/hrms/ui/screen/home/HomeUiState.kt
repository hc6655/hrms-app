package com.hkbufyp.hrms.ui.screen.home

import androidx.biometric.BiometricPrompt.CryptoObject
import com.hkbufyp.hrms.domain.model.Announcement


data class HomeUiState(
    val isLoadingData: Boolean = false,
    val message: String = "",
    val announcements: List<Announcement> = emptyList(),
    val shouldShowBiometricAlert: Boolean = false,
    val showedBiometricAlert: Boolean = false,
    val showBiometricPrompt: Boolean = false,
    val isLoadingToken: Boolean = false,
    val cryptoObject: CryptoObject? = null,
)