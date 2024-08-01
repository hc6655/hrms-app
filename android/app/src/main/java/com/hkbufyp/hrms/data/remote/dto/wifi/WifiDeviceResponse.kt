package com.hkbufyp.hrms.data.remote.dto.wifi

import kotlinx.serialization.Serializable

@Serializable
data class WifiDeviceResponse(
    val devices: List<WifiDeviceDto>
)