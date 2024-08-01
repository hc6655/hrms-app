package com.hkbufyp.hrms.data.remote.dto.fcm

import kotlinx.serialization.Serializable

@Serializable
data class SetFCMTokenPayload(
    val token: String
)