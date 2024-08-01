package com.hkbufyp.hrms.data.remote.dto.wifi

import com.hkbufyp.hrms.domain.model.WifiDevice
import kotlinx.serialization.Serializable

@Serializable
data class WifiDeviceDto(
    val bssid: String,
    val ssid: String
)

fun WifiDeviceDto.toWifiDevice() =
    WifiDevice(ssid = ssid, bssid = bssid)