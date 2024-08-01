package com.hkbufyp.hrms.ui.screen.user.leave.detail

import com.hkbufyp.hrms.domain.model.LeaveApplication

data class LeaveDetailUiState(
    val isLoadingData: Boolean = false,
    val isRefreshing: Boolean = false,
    val leaveApplication: LeaveApplication? = null,
    val message: String = ""
)
