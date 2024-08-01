package com.hkbufyp.hrms.data.remote.dto.leave

import com.hkbufyp.hrms.data.remote.dto.log.ManageLeaveLogDto
import com.hkbufyp.hrms.data.remote.dto.log.toManageLeaveLog
import com.hkbufyp.hrms.domain.enums.LeaveApplicationStatus
import com.hkbufyp.hrms.domain.model.LeaveApplication
import com.hkbufyp.hrms.domain.model.LeaveBalance
import com.hkbufyp.hrms.domain.model.LeaveType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LeaveTypeDto(
    val id: Int,
    val name: String,
    @SerialName("default_days")
    val defaultDays: Int,
    @SerialName("is_default_days_per_year")
    val isDefaultDaysPerYear: Int,
    @SerialName("grow_rate_per_year")
    val growRatePerYear: Int,
    @SerialName("grow_rate_per_month")
    val growRatePerMonth: Int,
    @SerialName("is_need_reason_for_apply")
    val isNeedReasonForApply: Int
)

fun LeaveTypeDto.toLeaveType() =
    LeaveType(
        id = id,
        name = name,
        defaultDays = defaultDays,
        isDefaultDaysPerYear = isDefaultDaysPerYear == 1,
        growRatePerYear = growRatePerYear,
        growRatePerMonth = growRatePerMonth,
        isNeedReasonForApply = isNeedReasonForApply == 1
    )

@Serializable
data class LeaveBalanceDto(
    @SerialName("employee_id")
    val employeeId: String,
    @SerialName("leave_type_id")
    val leaveTypeId: Int,
    @SerialName("name")
    val leaveTypeName: String,
    @SerialName("total_days")
    val totalDays: Int,
    @SerialName("days_left")
    val daysLeft: Int,
)

fun LeaveBalanceDto.toLeaveBalance() =
    LeaveBalance(
        employeeId = employeeId,
        leaveType = LeaveType(leaveTypeId, leaveTypeName),
        totalDays = totalDays,
        daysLeft = daysLeft
    )

@Serializable
data class LeaveApplicationDto(
    val id: Int,
    @SerialName("apply_date")
    val applyDate: String,
    @SerialName("start_date")
    val startDate: String,
    @SerialName("end_date")
    val endDate: String,
    val days: Float,
    @SerialName("leave_type_id")
    val leaveTypeId: Int,
    val reason: String,
    val status: Int,
    @SerialName("employee_id")
    val employeeId: String,
    val name: String,
    val logs: List<ManageLeaveLogDto>? = null
)

fun LeaveApplicationDto.toLeaveApplication() =
    LeaveApplication(
        id = id,
        applyDate = applyDate,
        startDate = startDate,
        endDate = endDate,
        days = days,
        leaveType = LeaveType(leaveTypeId, name),
        reason = reason,
        status = LeaveApplicationStatus.fromInt(status) ?: LeaveApplicationStatus.PENDING,
        employeeId = employeeId,
        logs = logs?.map { it.toManageLeaveLog() }
    )