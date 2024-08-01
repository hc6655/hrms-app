package com.hkbufyp.hrms.data.remote.api

import com.hkbufyp.hrms.util.Ktor
import io.ktor.client.request.get
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse

class RoleService(private val ktor: Ktor) {
    suspend fun getRoles() =
        ktor.client.get {
            url(ApiService.Role.root)
        }
}