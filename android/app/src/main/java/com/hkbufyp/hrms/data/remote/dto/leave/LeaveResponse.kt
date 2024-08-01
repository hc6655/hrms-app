package com.hkbufyp.hrms.data.remote.dto.leave

import kotlinx.serialization.Serializable

@Serializable
data class LeaveTypeResponse(
    val leaveTypes: List<LeaveTypeDto>
)

@Serializable
data class LeaveBalanceResponse(
    val balances: List<LeaveBalanceDto>
)

@Serializable
data class LeaveApplicationResponse(
    val applications: List<LeaveApplicationDto>
)

@Serializable
data class LeaveApplyResponse(
    val applicationId: String
)