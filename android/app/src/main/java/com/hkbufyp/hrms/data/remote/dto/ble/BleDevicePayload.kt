package com.hkbufyp.hrms.data.remote.dto.ble

import kotlinx.serialization.Serializable

@Serializable
data class AddBleDevicePayload(
    val mac: String,
    val name: String
)

@Serializable
data class RemoveBleDevicePayload(
    val mac: String
)