package com.hkbufyp.hrms.data.remote.dto.department

import kotlinx.serialization.Serializable

@Serializable
data class DepartmentResponse(
    val departments: List<DepartmentDto>
)