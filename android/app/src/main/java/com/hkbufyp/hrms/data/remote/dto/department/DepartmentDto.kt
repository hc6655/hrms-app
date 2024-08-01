package com.hkbufyp.hrms.data.remote.dto.department

import com.hkbufyp.hrms.domain.model.Department
import com.hkbufyp.hrms.domain.model.DepartmentDetail
import com.hkbufyp.hrms.domain.model.Position
import com.hkbufyp.hrms.domain.model.PositionBrief
import com.hkbufyp.hrms.util.toBoolean
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DepartmentDto(
    val id: String,
    val title: String,
)

fun DepartmentDto.toDepartment() =
    Department(id = id, title = title)

@Serializable
data class PositionDto(
    val id: Int,
    @SerialName("department_id")
    val departmentId: String,
    val name: String,
    @SerialName("access_level")
    val accessLevel: Int,
    @SerialName("management_feature")
    val managementFeature: Int,
    @SerialName("access_log")
    val accessLog: Int
)

fun PositionDto.toPosition() =
    Position(
        id = id,
        departmentId = departmentId,
        name = name,
        accessLevel = accessLevel,
        managementFeature = managementFeature.toBoolean(),
        accessLog = accessLog.toBoolean()
    )

@Serializable
data class DepartmentDetailDto(
    val id: String,
    val title: String,
    val positions: List<PositionDto>
)

fun DepartmentDetailDto.toDepartmentDetail() =
    DepartmentDetail(
        id = id,
        title = title,
        positions = positions.map { it.toPosition() }
    )

@Serializable
data class PositionBriefDto(
    val id: Int,
    val name: String
)

fun PositionBriefDto.toPositionBrief() =
    PositionBrief(
        id = id,
        name = name
    )