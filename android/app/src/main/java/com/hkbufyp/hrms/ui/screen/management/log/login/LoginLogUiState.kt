package com.hkbufyp.hrms.ui.screen.management.log.login

import com.hkbufyp.hrms.domain.model.LoginLog

data class LoginLogUiState(
    val isLoadingData: Boolean = false,
    val logs: List<LoginLog> = emptyList(),
    val message: String = ""
)
