package com.hkbufyp.hrms.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class Department(
    val id: String,
    val title: String
)

@Serializable
data class Position(
    val id: Int,
    val departmentId: String,
    var name: String,
    var accessLevel: Int,
    var managementFeature: Boolean,
    var accessLog: Boolean
)

data class DepartmentDetail(
    val id: String,
    val title: String,
    val positions: List<Position>
)

data class PositionBrief(
    val id: Int,
    val name: String
)

fun Position.toBrief() =
    PositionBrief(
        id = id,
        name = name
    )