package com.hkbufyp.hrms.data.remote.dto.role

import com.hkbufyp.hrms.data.remote.dto.department.PositionDto
import kotlinx.serialization.Serializable

@Serializable
data class RoleResponse(
    val positions: List<PositionDto>
)