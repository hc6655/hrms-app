package com.hkbufyp.hrms.data.remote.dto.user

import com.hkbufyp.hrms.domain.model.Department
import com.hkbufyp.hrms.domain.model.Permission
import com.hkbufyp.hrms.domain.model.PositionBrief
import com.hkbufyp.hrms.domain.model.Role
import com.hkbufyp.hrms.domain.model.User
import com.hkbufyp.hrms.domain.model.UserJwt
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserInfoDto(
    @SerialName("id")
    val employeeId: String,
    @SerialName("first_name")
    val firstName: String,
    @SerialName("last_name")
    val lastName: String,
    @SerialName("role_id")
    val roleId: Int,
    @SerialName("department_id")
    val departmentId: String,
    @SerialName("phone")
    val phone: String,
    @SerialName("nick_name")
    val nickName: String,
    @SerialName("department_title")
    val departmentTitle: String,
    @SerialName("position_name")
    val positionName: String,
    @SerialName("fixed_working_timeslot_id")
    val fixedTimeslotId: Int,
    @SerialName("timeslot_name")
    val timeslotName: String,
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

fun UserInfoDto.toUser() =
    User(
        id = employeeId,
        firstName = firstName,
        lastName = lastName,
        nickName = nickName,
        phone = phone,
        position = PositionBrief(
            id = roleId,
            name = positionName
        ),
        department = Department(
            id = departmentId,
            title = departmentTitle
        ),
        fixedTimeslotId = fixedTimeslotId,
        timeslotName = timeslotName,
        workingType = workingType,
        salaryType = salaryType,
        salary = salary,
        otAllowance = otAllowance,
        otAllowanceType = otAllowanceType
    )
