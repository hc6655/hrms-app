package com.hkbufyp.hrms.data.remote.dto.log

import kotlinx.serialization.Serializable

@Serializable
data class LoginLogResponse(
    val logs: List<LoginLogDto>
)

@Serializable
data class ManageEmployeeLogResponse(
    val logs: List<ManageEmployeeLogDto>
)

@Serializable
data class ManageLeaveLogResponse(
    val logs: List<ManageLeaveLogDto>
)

@Serializable
data class TakeAttendLogResponse(
    val logs: List<TakeAttendLogDto>
)