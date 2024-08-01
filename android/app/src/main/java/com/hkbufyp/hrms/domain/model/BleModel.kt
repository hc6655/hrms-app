package com.hkbufyp.hrms.domain.model

import android.bluetooth.le.ScanResult

data class BleDevice(
    val name: String,
    val mac: String
)

fun ScanResult.toBleDevice(name: String? = null) =
    BleDevice(
        name = name ?: "N/A",
        mac = this.device.address
    )