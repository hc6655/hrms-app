package com.hkbufyp.hrms.domain.model

enum class AndroidKeyStatus {
    VALID,
    INVALIDATED,
    ERROR
}

data class BiometricStatus (
    val isBiometricTokenExist: Boolean = false,
    val isBiometricAvailable: Boolean = false,
    val keyStatus: AndroidKeyStatus = AndroidKeyStatus.INVALIDATED
) {
    fun canAuthByBiometric() = isBiometricTokenExist && isBiometricAvailable && keyStatus == AndroidKeyStatus.VALID
    fun isKeyInvalidated() = keyStatus == AndroidKeyStatus.INVALIDATED
}