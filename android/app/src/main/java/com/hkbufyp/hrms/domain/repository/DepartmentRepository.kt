package com.hkbufyp.hrms.domain.repository

import com.hkbufyp.hrms.data.remote.dto.department.DepartmentResponse
import com.hkbufyp.hrms.data.remote.dto.department.UpdateDepartmentPayload
import com.hkbufyp.hrms.domain.model.Department
import com.hkbufyp.hrms.domain.model.DepartmentDetail
import com.hkbufyp.hrms.util.NetworkResponse
import kotlinx.coroutines.flow.Flow

interface DepartmentRepository {
    fun getDepartments(): Flow<NetworkResponse<List<Department>>>
    fun getDepartment(id: String): Flow<NetworkResponse<DepartmentDetail>>
    fun createDepartment(department: DepartmentDetail): Flow<NetworkResponse<String>>
    fun updateDepartment(department: DepartmentDetail, payload: UpdateDepartmentPayload): Flow<NetworkResponse<String>>
    fun deleteDepartment(id: String): Flow<NetworkResponse<String>>
}