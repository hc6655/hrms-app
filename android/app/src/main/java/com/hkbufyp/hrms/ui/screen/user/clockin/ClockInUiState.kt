package com.hkbufyp.hrms.ui.screen.user.clockin

import androidx.biometric.BiometricPrompt
import com.hkbufyp.hrms.domain.model.AttendRecord
import com.hkbufyp.hrms.domain.model.AttendTimeslotDate
import com.hkbufyp.hrms.domain.model.BiometricStatus

data class ClockInUiState(
    val isLoading: Boolean = false,
    val isValidForClockIn: Boolean = false,
    val message: String = "",
    val startOfWeek: String = "",
    val endOfWeek: String = "",
    val slots: List<AttendTimeslotDate> = emptyList(),
    val showBiometricPrompt: Boolean = false,
    val biometricStatus: BiometricStatus? = null,
    val shouldShowBiometricAlert: Boolean = false,
    val cryptoObject: BiometricPrompt.CryptoObject? = null,
    val isSubmitting: Boolean = false,
    val attendRecord: AttendRecord? = null
)
