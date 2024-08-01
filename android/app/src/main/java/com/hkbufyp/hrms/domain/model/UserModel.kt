package com.hkbufyp.hrms.domain.model

import com.hkbufyp.hrms.data.remote.dto.user.UpdateUserPayload
import kotlinx.serialization.SerialName

data class UserJwt(
    val id: String,
    val nickName: String,
    val accessLevel: Int,
    val managementFeature: Boolean,
    val accessLog: Boolean
)

data class User(
    val id: String,
    val firstName: String,
    val lastName: String,
    val nickName: String,
    val phone: String,
    val position: PositionBrief,
    val department: Department,
    val fixedTimeslotId: Int,
    val timeslotName: String,
    val workingType: Int,
    val salaryType: Int,
    val salary: Int,
    val otAllowance: Int,
    val otAllowanceType: Int
)

fun User.toUpdatePayload(): UpdateUserPayload =
    UpdateUserPayload(
        firstName = firstName,
        lastName = lastName,
        nickName = nickName,
        phone = phone,
        roleId = position.id,
        departmentId = department.id,
        fixedTimeslotId = fixedTimeslotId,
        workingType = workingType,
        salaryType = salaryType,
        salary = salary,
        otAllowance = otAllowance,
        otAllowanceType = otAllowanceType
    )