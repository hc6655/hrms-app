package com.hkbufyp.hrms.ui.screen.middleware.sensitive_auth

sealed class SensitiveAuthEvent {
    data object Submit: SensitiveAuthEvent()
    data object UsePassword: SensitiveAuthEvent()
    data class PasswordChanged(val password: String): SensitiveAuthEvent()
}
