package com.hkbufyp.hrms.domain.repository

import android.net.wifi.ScanResult
import android.net.wifi.WifiInfo
import com.hkbufyp.hrms.domain.model.WifiDevice
import com.hkbufyp.hrms.util.NetworkResponse
import kotlinx.coroutines.flow.Flow

interface WifiRepository {
    fun scanWifi(): Flow<List<ScanResult>>
    fun getWifiDevices(): Flow<NetworkResponse<List<WifiDevice>>>
    fun addWifiDevice(device: WifiDevice): Flow<NetworkResponse<String>>
    fun removeWifiDevice(device: WifiDevice): Flow<NetworkResponse<String>>
    fun getCurrentConnectedWifi(): WifiInfo?
}