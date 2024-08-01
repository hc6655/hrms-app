package com.hkbufyp.hrms.data.remote.dto.log

import com.hkbufyp.hrms.domain.model.LoginLog
import com.hkbufyp.hrms.domain.model.ManageEmployeeLog
import com.hkbufyp.hrms.domain.model.ManageLeaveLog
import com.hkbufyp.hrms.domain.model.TakeAttendLog
import com.hkbufyp.hrms.util.toBoolean
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginLogDto(
    val id: Int,
    @SerialName("date_time")
    val dateTime: String,
    @SerialName("employee_id")
    val employeeId: String,
    val ip: String,
    val method: String,
    @SerialName("is_success")
    val isSuccess: Int,
    @SerialName("err_message")
    val errorMessage: String
)

fun LoginLogDto.toLoginLog() =
    LoginLog(
        id = id,
        dateTime = dateTime,
        employeeId = employeeId,
        ip = if (ip.contains("192.168") || ip.contains("127.0")) {
            "localhost"
        } else {
            ip
        },
        loginMethod = method,
        isSuccess = isSuccess.toBoolean(),
        errorMessage = errorMessage
    )

@Serializable
data class ManageEmployeeLogDto(
    val id: Int,
    @SerialName("date_time")
    val dateTime: String,
    @SerialName("operator_id")
    val operatorId: String,
    val ip: String,
    @SerialName("manage_type")
    val manageType: Int,
    @SerialName("operated_id")
    val operatedId: String,
    val message: String
)

fun ManageEmployeeLogDto.toManageEmployeeLog() =
    ManageEmployeeLog(
        id = id,
        dateTime = dateTime,
        operatedId = operatedId,
        ip = if (ip.contains("192.168") || ip.contains("127.0")) {
            "localhost"
        } else {
            ip
        },
        manageType = when (manageType) {
            0 -> "Access"
            1 -> "Create"
            2 -> "Update"
            else -> "Unknown"
        },
        operatorId = operatorId,
        message = message
    )

@Serializable
data class ManageLeaveLogDto(
    val id: Int,
    @SerialName("date_time")
    val dateTime: String,
    val ip: String,
    @SerialName("operator_id")
    val operatorId: String,
    @SerialName("application_id")
    val applicationId: Int,
    val status: Int,
    val message: String,
    val reason: String,
    val applicant: String
)

fun ManageLeaveLogDto.toManageLeaveLog() =
    ManageLeaveLog(
        id = id,
        dateTime = dateTime,
        ip = if (ip.contains("192.168") || ip.contains("127.0")) {
            "localhost"
        } else {
            ip
        },
        operatorId = operatorId,
        applicationId = applicationId,
        status = when (status) {
            0 -> "Create"
            1 -> "Approve"
            2 -> "Reject"
            else -> "Unknown"
        },
        message = message,
        reason = reason,
        applicant = applicant
    )

@Serializable
data class TakeAttendLogDto(
    val id: Int,
    @SerialName("employee_id")
    val employeeId: String,
    @SerialName("date_time")
    val dateTime: String,
    val ip: String,
    val success: Int
)

fun TakeAttendLogDto.toTakeAttendLog() =
    TakeAttendLog(
        id = id,
        employeeId = employeeId ,
        dateTime = dateTime,
        ip = if (ip.contains("192.168") || ip.contains("127.0")) {
            "localhost"
        } else {
            ip
        },
        success = success.toBoolean()
    )