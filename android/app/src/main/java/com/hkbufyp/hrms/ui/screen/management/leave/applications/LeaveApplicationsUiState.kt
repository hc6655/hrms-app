package com.hkbufyp.hrms.ui.screen.management.leave.applications

import com.hkbufyp.hrms.domain.model.LeaveApplication

data class LeaveApplicationsUiState(
    val isLoadingData: Boolean = false,
    val isRefreshing: Boolean = false,
    val leaveApplications: List<LeaveApplication> = emptyList(),
    val message: String = "",
)
