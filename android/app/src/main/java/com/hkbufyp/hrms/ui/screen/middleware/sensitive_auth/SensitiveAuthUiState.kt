package com.hkbufyp.hrms.ui.screen.middleware.sensitive_auth

data class SensitiveAuthUiState(
    val isInitializing: Boolean = false,
    val isSubmitting: Boolean = false,
    val isUsePassword: Boolean = false,
    val password: String = "",
    val message: String = ""
)
