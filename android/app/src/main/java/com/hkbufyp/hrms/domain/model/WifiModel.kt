package com.hkbufyp.hrms.domain.model

import android.net.wifi.ScanResult

data class WifiDevice(
    val ssid: String,
    val bssid: String
)

fun ScanResult.toWifiDevice() =
    WifiDevice(
        ssid = this.SSID,
        bssid = this.BSSID
    )