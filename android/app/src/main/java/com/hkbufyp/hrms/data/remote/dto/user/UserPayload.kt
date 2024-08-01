package com.hkbufyp.hrms.data.remote.dto.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginPayload(
    val id: String,
    val pwd: String
)

@Serializable
data class LoginBiometricPayload(
    val id: String,
    val token: String
)

@Serializable
data class CreateUserPayload(
    @SerialName("first_name")
    val firstName: String,
    @SerialName("last_name")
    val lastName: String,
    @SerialName("role_id")
    val roleId: Int,
    @SerialName("department_id")
    val departmentId: String,
    val phone:String,
    @SerialName("nick_name")
    val nickName: String,
    @SerialName("fixed_working_timeslot_id")
    val timeslotId: Int,
    @SerialName("working_type")
    val workingType: Int,
    @SerialName("salary_type")
    val salaryType: Int,
    val salary: Int,
    @SerialName("ot_allowance")
    val otAllowance: Int,
    @SerialName("ot_allowance_type")
    val otAllowanceType: Int
)

@Serializable
data class UpdateUserPayload(
    @SerialName("first_name")
    val firstName: String,
    @SerialName("last_name")
    val lastName: String,
    @SerialName("role_id")
    val roleId: Int,
    @SerialName("department_id")
    val departmentId: String,
    val phone:String,
    @SerialName("nick_name")
    val nickName: String,
    @SerialName("fixed_working_timeslot_id")
    val fixedTimeslotId: Int,
    @SerialName("working_type")
    val workingType: Int,
    @SerialName("salary_type")
    val salaryType: Int,
    val salary: Int,
    @SerialName("ot_allowance")
    val otAllowance: Int,
    @SerialName("ot_allowance_type")
    val otAllowanceType: Int
)