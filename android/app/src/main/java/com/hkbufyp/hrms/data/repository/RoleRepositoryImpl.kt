package com.hkbufyp.hrms.data.repository

import com.hkbufyp.hrms.data.remote.api.RoleService
import com.hkbufyp.hrms.data.remote.dto.department.toPosition
import com.hkbufyp.hrms.data.remote.dto.role.RoleResponse
import com.hkbufyp.hrms.domain.repository.RoleRepository
import com.hkbufyp.hrms.util.NetworkResponse
import io.ktor.client.call.body
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.flow.flow

class RoleRepositoryImpl(private val roleService: RoleService): RoleRepository {
    override fun getRoles() =
        flow {
            val response = roleService.getRoles()

            when (response.status) {
                HttpStatusCode.OK -> {
                    val roleResponse = response.body<RoleResponse>()
                    val roles = roleResponse.positions

                    emit(NetworkResponse.Success(roles.map { it.toPosition() }))
                }
                else -> {
                    emit(NetworkResponse.Failure(response.body()))
                }
            }
        }
}