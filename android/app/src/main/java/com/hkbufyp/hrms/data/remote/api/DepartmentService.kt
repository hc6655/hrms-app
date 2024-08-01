package com.hkbufyp.hrms.data.remote.api

import com.hkbufyp.hrms.data.remote.dto.department.CreateDepartmentPayload
import com.hkbufyp.hrms.data.remote.dto.department.DepartmentDto
import com.hkbufyp.hrms.data.remote.dto.department.UpdateDepartmentPayload
import com.hkbufyp.hrms.domain.model.Department
import com.hkbufyp.hrms.util.Ktor
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.request.url

class DepartmentService(private val ktor: Ktor) {
    suspend fun getDepartments() =
        ktor.client.get {
            url(ApiService.Department.root)
        }

    suspend fun getDepartment(id: String) =
        ktor.client.get {
            url("${ApiService.Department.root}/$id")
        }

    suspend fun createDepartment(payload: CreateDepartmentPayload) =
        ktor.client.post {
            url(ApiService.Department.root)
            setBody(payload)
        }

    suspend fun updateDepartment(id: String, payload: UpdateDepartmentPayload) =
        ktor.client.put {
            url("${ApiService.Department.root}/$id")
            setBody(payload)
        }

    suspend fun deleteDepartment(id: String) =
        ktor.client.delete {
            url("${ApiService.Department.root}/$id")
        }
}