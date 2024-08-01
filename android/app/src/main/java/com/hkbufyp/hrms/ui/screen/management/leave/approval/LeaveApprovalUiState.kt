package com.hkbufyp.hrms.ui.screen.management.leave.approval

import com.hkbufyp.hrms.domain.model.LeaveApplication

data class LeaveApprovalUiState(
    val isLoadingData: Boolean = false,
    val isRefreshing: Boolean = false,
    val leaveApplication: LeaveApplication? = null,
    val message: String = "",
    val isApproving: Boolean = false,
    val isRejecting: Boolean = false,
    val isShowDialog: Boolean = false,
    val reason: String = ""
)
