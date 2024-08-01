package com.hkbufyp.hrms.data.remote.api

import com.hkbufyp.hrms.data.remote.dto.user.CreateUserPayload
import com.hkbufyp.hrms.data.remote.dto.user.LoginBiometricPayload
import com.hkbufyp.hrms.data.remote.dto.user.LoginPayload
import com.hkbufyp.hrms.data.remote.dto.user.UpdateUserPayload
import com.hkbufyp.hrms.util.Ktor
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse

class UserService(private val ktor: Ktor) {
    suspend fun login(id: String, password: String): HttpResponse {
        val payload = LoginPayload(id, password)
        return ktor.client.post {
            url(ApiService.User.login)
            setBody(payload)
        }
    }

    fun setAccessToken(token: String) {
        ktor.setToken(token)
    }

    suspend fun getUserInfo(id: String): HttpResponse {
        return ktor.client.get {
            url("${ApiService.User.root}/${id}")
        }
    }

    suspend fun createUser(payload: CreateUserPayload): HttpResponse {
        return ktor.client.post {
            url(ApiService.User.root)
            setBody(payload)
        }
    }

    suspend fun getUserPermission(id: String): HttpResponse {
        return ktor.client.get {
            url("${ApiService.User.getPermission}/$id")
        }
    }

    suspend fun getUsers(): HttpResponse {
        return ktor.client.get {
            url(ApiService.User.root)
        }
    }

    suspend fun updateUser(id: String, payload: UpdateUserPayload): HttpResponse {
        return ktor.client.put {
            url("${ApiService.User.root}/$id")
            setBody(payload)
        }
    }

    suspend fun loginWithBiometric(id: String, token: String): HttpResponse {
        val payload = LoginBiometricPayload(id, token)
        return ktor.client.post {
            url(ApiService.User.auth)
            setBody(payload)
        }
    }

    suspend fun updateUserBiometric(id: String): HttpResponse {
        return ktor.client.put {
            url("${ApiService.User.auth}/$id")
        }
    }
}