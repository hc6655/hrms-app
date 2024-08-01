package com.hkbufyp.hrms.domain.model

data class LoginLog(
    val id: Int,
    val dateTime: String,
    val employeeId: String,
    val ip: String,
    val loginMethod: String,
    val isSuccess: Boolean,
    val errorMessage: String
)

data class ManageEmployeeLog(
    val id: Int,
    val dateTime: String,
    val operatorId: String,
    val ip: String,
    val manageType: String,
    val operatedId: String,
    val message: String
)

data class ManageLeaveLog(
    val id: Int,
    val dateTime: String,
    val ip: String,
    val operatorId: String,
    val applicationId: Int,
    val status: String,
    val message: String,
    val reason: String,
    val applicant: String
)

data class TakeAttendLog(
    val id: Int,
    val employeeId: String,
    val dateTime: String,
    val ip: String,
    val success: Boolean
)