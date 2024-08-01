package com.hkbufyp.hrms.ui.screen.user.leave.history

import com.hkbufyp.hrms.domain.model.LeaveApplication

data class LeaveHistoryUiState(
    val isLoadingData: Boolean = false,
    val isRefreshing: Boolean = false,
    val leaveApplications: List<LeaveApplication?> = emptyList(),
    val message: String = "",
)
