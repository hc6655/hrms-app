package com.hkbufyp.hrms.data.repository

import com.hkbufyp.hrms.data.remote.api.DepartmentService
import com.hkbufyp.hrms.data.remote.dto.department.CreateDepartmentPayload
import com.hkbufyp.hrms.data.remote.dto.department.DepartmentDetailDto
import com.hkbufyp.hrms.data.remote.dto.department.DepartmentDto
import com.hkbufyp.hrms.data.remote.dto.department.DepartmentResponse
import com.hkbufyp.hrms.data.remote.dto.department.UpdateDepartmentPayload
import com.hkbufyp.hrms.data.remote.dto.department.toDepartment
import com.hkbufyp.hrms.data.remote.dto.department.toDepartmentDetail
import com.hkbufyp.hrms.domain.model.Department
import com.hkbufyp.hrms.domain.model.DepartmentDetail
import com.hkbufyp.hrms.domain.repository.DepartmentRepository
import com.hkbufyp.hrms.util.NetworkResponse
import io.ktor.client.call.body
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DepartmentRepositoryImpl(
    private val departmentService: DepartmentService
): DepartmentRepository {
    override fun getDepartments() =
        flow {
            val response = departmentService.getDepartments()

            when (response.status) {
                HttpStatusCode.OK -> {
                    val departmentResponse = response.body<DepartmentResponse>()
                    val departments = departmentResponse.departments

                    emit(NetworkResponse.Success(departments.map { it.toDepartment() }))
                }
                else -> {
                    emit(NetworkResponse.Failure(response.body()))
                }
            }
        }

    override fun getDepartment(id: String) =
        flow {
            val response = departmentService.getDepartment(id)

            when (response.status) {
                HttpStatusCode.OK -> {
                    val departmentDto = response.body<DepartmentDetailDto>()
                    emit(NetworkResponse.Success(departmentDto.toDepartmentDetail()))
                }
                else -> {
                    emit(NetworkResponse.Failure(response.body()))
                }
            }
        }

    override fun createDepartment(department: DepartmentDetail) =
        flow {
            val response = departmentService.createDepartment(
                CreateDepartmentPayload(
                    id = department.id,
                    title = department.title,
                    positions = department.positions
                )
            )

            when (response.status) {
                HttpStatusCode.OK -> {
                    emit(NetworkResponse.Success("Success"))
                }
                else -> {
                    emit(NetworkResponse.Failure(response.body()))
                }
            }
        }

    override fun updateDepartment(department: DepartmentDetail, payload: UpdateDepartmentPayload) =
        flow {
            val response = departmentService.updateDepartment(
                id = department.id,
                payload = payload
            )

            when (response.status) {
                HttpStatusCode.OK -> {
                    emit(NetworkResponse.Success("Success"))
                }
                else -> {
                    emit(NetworkResponse.Failure(response.body()))
                }
            }
        }

    override fun deleteDepartment(id: String) =
        flow {
            val response = departmentService.deleteDepartment(id)

            when (response.status) {
                HttpStatusCode.OK -> {
                    emit(NetworkResponse.Success("Success"))
                }
                else -> {
                    emit(NetworkResponse.Failure(response.body()))
                }
            }
        }
}