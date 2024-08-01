package com.hkbufyp.hrms.domain.repository

import com.hkbufyp.hrms.data.remote.dto.role.RoleResponse
import com.hkbufyp.hrms.domain.model.Position
import com.hkbufyp.hrms.domain.model.Role
import com.hkbufyp.hrms.util.NetworkResponse
import kotlinx.coroutines.flow.Flow

interface RoleRepository {
    fun getRoles(): Flow<NetworkResponse<List<Position>>>
}