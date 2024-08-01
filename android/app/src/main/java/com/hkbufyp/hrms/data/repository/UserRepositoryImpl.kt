package com.hkbufyp.hrms.data.repository

import com.hkbufyp.hrms.data.remote.api.UserService
import com.hkbufyp.hrms.data.remote.dto.user.CreateUserPayload
import com.hkbufyp.hrms.data.remote.dto.user.CreateUserResponse
import com.hkbufyp.hrms.data.remote.dto.user.GetUsersResponse
import com.hkbufyp.hrms.data.remote.dto.user.LoginResponse
import com.hkbufyp.hrms.data.remote.dto.user.UpdateUserBiometricResponse
import com.hkbufyp.hrms.data.remote.dto.user.UpdateUserPayload
import com.hkbufyp.hrms.data.remote.dto.user.UserInfoDto
import com.hkbufyp.hrms.data.remote.dto.user.toUser
import com.hkbufyp.hrms.data.remote.dto.user.toUserJwt
import com.hkbufyp.hrms.domain.model.Permission
import com.hkbufyp.hrms.domain.model.User
import com.hkbufyp.hrms.domain.model.UserJwt
import com.hkbufyp.hrms.domain.repository.UserRepository
import com.hkbufyp.hrms.util.NetworkResponse
import io.ktor.client.call.body
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UserRepositoryImpl(private val userService: UserService): UserRepository {
    override fun login(id: String, password: String) =
        flow {
            val response = userService.login(id, password)

            when (response.status) {
                HttpStatusCode.OK -> {
                    val loginResponse = response.body<LoginResponse>()
                    userService.setAccessToken(loginResponse.token)

                    emit(NetworkResponse.Success(loginResponse.toUserJwt()))
                }
                else -> {
                    emit(NetworkResponse.Failure(response.body()))
                }
            }
        }

    override fun setAccessToken(token: String) {
        userService.setAccessToken(token)
    }

    override fun getUserInfo(id: String) =
        flow {
            val response = userService.getUserInfo(id)

            when (response.status) {
                HttpStatusCode.OK -> {
                    val res = response.body<UserInfoDto>()
                    emit(NetworkResponse.Success(res.toUser()))
                }
                else -> {
                    emit(NetworkResponse.Failure(response.body()))
                }
            }
        }

    override fun createUser(payload: CreateUserPayload) =
        flow {
            val response = userService.createUser(payload)

            when (response.status) {
                HttpStatusCode.OK -> {
                    emit(NetworkResponse.Success(response.body<CreateUserResponse>()))
                }

                else -> {
                    emit(NetworkResponse.Failure(response.body()))
                }
            }
        }


    override fun updateFCMToken(token: String) {

    }

    override fun getEmployees() =
        flow {
            val response = userService.getUsers()

            when (response.status) {
                HttpStatusCode.OK -> {
                    val usersResponse = response.body<GetUsersResponse>()
                    val users = usersResponse.employees

                    emit(NetworkResponse.Success(users.map { it.toUser() }))
                }
                else -> {
                    emit(NetworkResponse.Failure(response.body()))
                }
            }
        }

    override fun updateUser(id:String, payload: UpdateUserPayload) =
        flow {
            val response = userService.updateUser(id, payload)

            when (response.status) {
                HttpStatusCode.OK -> {
                    emit(NetworkResponse.Success(response.body<String>()))
                }
                else -> {
                    emit(NetworkResponse.Failure(response.body()))
                }
            }
        }

    override fun loginWithBiometric(id: String, token: String) =
        flow {
            val response = userService.loginWithBiometric(id, token)

            when (response.status) {
                HttpStatusCode.OK -> {
                    val loginResponse = response.body<LoginResponse>()
                    userService.setAccessToken(loginResponse.token)

                    emit(NetworkResponse.Success(loginResponse.toUserJwt()))
                }
                else -> {
                    emit(NetworkResponse.Failure(response.body()))
                }
            }
        }

    override fun updateUserBiometric(id: String) =
        flow {
            val response = userService.updateUserBiometric(id)

            when (response.status) {
                HttpStatusCode.OK -> {
                    val res = response.body<UpdateUserBiometricResponse>()
                    emit(NetworkResponse.Success(res.token))
                }
                else -> {
                    emit(NetworkResponse.Failure(response.body()))
                }
            }
        }
}