package com.hkbufyp.hrms.data.remote.dto.leave

import kotlinx.serialization.Serializable

@Serializable
data class LeaveApplicationPayload(
    val startDate: String,
    val endDate: String,
    val days: Float,
    val leaveTypeId: Int,
    val reason: String
)

@Serializable
data class UpdateLeaveStatusPayload(
    val status: Int,
    val reason: String = ""
)