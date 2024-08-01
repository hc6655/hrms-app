package com.hkbufyp.hrms.ui.screen.user.leave.apply

import com.hkbufyp.hrms.domain.model.LeaveType

data class LeaveApplyUiState(
    val startDate: String = "",
    val endDate: String = "",
    val applyDays: String = "",
    val reason: String = "",
    val isLeaveTypeExpand: Boolean = false,
    val isShowDatePickerForStartDate: Boolean = false,
    val isShowDatePickerForEndDate: Boolean = false,
    val isLoadingData: Boolean = false,
    val leaveTypes: List<LeaveType> = emptyList(),
    val selectedLeaveType: LeaveType? = null,
    val message: String = "",
    val isApplying: Boolean = false
)
