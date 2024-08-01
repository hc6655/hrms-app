package com.hkbufyp.hrms.domain.repository

import com.hkbufyp.hrms.data.remote.dto.user.CreateUserPayload
import com.hkbufyp.hrms.data.remote.dto.user.CreateUserResponse
import com.hkbufyp.hrms.data.remote.dto.user.LoginResponse
import com.hkbufyp.hrms.data.remote.dto.user.UpdateUserBiometricResponse
import com.hkbufyp.hrms.data.remote.dto.user.UpdateUserPayload
import com.hkbufyp.hrms.domain.model.Permission
import com.hkbufyp.hrms.domain.model.User
import com.hkbufyp.hrms.domain.model.UserJwt
import com.hkbufyp.hrms.util.NetworkResponse
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun login(id: String, password: String): Flow<NetworkResponse<UserJwt>>
    fun setAccessToken(token: String): Unit
    fun getUserInfo(id: String): Flow<NetworkResponse<User>>
    fun createUser(payload: CreateUserPayload): Flow<NetworkResponse<CreateUserResponse>>
    fun updateFCMToken(token: String)
    fun getEmployees(): Flow<NetworkResponse<List<User>>>
    fun updateUser(id: String, payload: UpdateUserPayload): Flow<NetworkResponse<String>>
    fun loginWithBiometric(id: String, token: String): Flow<NetworkResponse<UserJwt>>
    fun updateUserBiometric(id: String): Flow<NetworkResponse<String>>
}