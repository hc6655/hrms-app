package com.hkbufyp.hrms.data.remote.dto.user

import com.auth0.android.jwt.JWT
import com.hkbufyp.hrms.domain.model.Permission
import com.hkbufyp.hrms.domain.model.UserJwt
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val token: String
)

fun LoginResponse.toUserJwt(): UserJwt {
    val jwt = JWT(token)
    println("token: $token")
    return UserJwt(
        id = jwt.getClaim("employeeId").asString() ?: "0",
        nickName = jwt.getClaim("nickName").asString() ?: "Unknown user",
        accessLevel = jwt.getClaim("accessLevel").asInt() ?: 0,
        managementFeature = jwt.getClaim("managementFeature").asInt() == 1,
        accessLog = jwt.getClaim("accessLog").asInt() == 1
    )
}

@Serializable
data class CreateUserResponse(
    @SerialName("employee_id")
    val employeeId: String
)

@Serializable
data class GetUsersResponse(
    val employees: List<UserInfoDto>
)

@Serializable
data class UpdateUserBiometricResponse(
    val token: String
)