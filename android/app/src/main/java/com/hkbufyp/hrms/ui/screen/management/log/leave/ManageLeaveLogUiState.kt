package com.hkbufyp.hrms.ui.screen.management.log.leave

import com.hkbufyp.hrms.domain.model.ManageLeaveLog


data class ManageLeaveLogUiState(
    val isLoadingData: Boolean = false,
    val logs: List<ManageLeaveLog> = emptyList(),
    val message: String = ""
)
