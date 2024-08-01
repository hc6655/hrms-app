package com.hkbufyp.hrms.data.remote.dto.ble

import kotlinx.serialization.Serializable

@Serializable
data class BleDeviceResponse(
    val devices: List<BleDeviceDto>
)