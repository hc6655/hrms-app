package com.hkbufyp.hrms.data.remote.dto.department

import com.hkbufyp.hrms.domain.model.Position
import kotlinx.serialization.Serializable

@Serializable
data class CreateDepartmentPayload(
    val id: String,
    val title: String,
    val positions: List<Position>
)

@Serializable
data class UpdateDepartmentPayload(
    val title: String,
    val deletedPositions: List<Int>,
    val updatedPositions: List<Position>,
    val addedPositions: List<Position>
)