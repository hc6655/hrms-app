package com.hkbufyp.hrms.domain.model

import com.hkbufyp.hrms.domain.enums.LeaveApplicationStatus


data class LeaveType(
    val id: Int,
    val name: String,
    val defaultDays: Int? = null,
    val isDefaultDaysPerYear: Boolean? = null,
    val growRatePerYear: Int? = null,
    val growRatePerMonth: Int? = null,
    val isNeedReasonForApply: Boolean? = null
)

data class LeaveBalance(
    val employeeId: String,
    val leaveType: LeaveType,
    val totalDays: Int,
    val daysLeft: Int,
)

data class LeaveApplication(
    val id: Int,
    val applyDate: String,
    val startDate: String,
    val endDate: String,
    val days: Float,
    val leaveType: LeaveType,
    val reason: String,
    val status: LeaveApplicationStatus,
    val employeeId: String,
    val logs: List<ManageLeaveLog>? = null
)