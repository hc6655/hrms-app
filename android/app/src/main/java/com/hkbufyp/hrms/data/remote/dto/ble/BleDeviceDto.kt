package com.hkbufyp.hrms.data.remote.dto.ble

import com.hkbufyp.hrms.domain.model.BleDevice
import kotlinx.serialization.Serializable

@Serializable
data class BleDeviceDto(
    val mac: String,
    val name: String
)

fun BleDeviceDto.toBleDevice() =
    BleDevice(
        mac = mac,
        name = name
    )