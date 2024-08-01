package com.hkbufyp.hrms.data.remote.dto.wifi

import kotlinx.serialization.Serializable

@Serializable
data class AddWifiDevicePayload(
    val bssid: String,
    val ssid: String
)

@Serializable
data class RemoveWifiDevicePayload(
    val bssid: String
)