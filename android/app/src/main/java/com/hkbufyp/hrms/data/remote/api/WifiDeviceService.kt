package com.hkbufyp.hrms.data.remote.api

import com.hkbufyp.hrms.data.remote.dto.wifi.AddWifiDevicePayload
import com.hkbufyp.hrms.data.remote.dto.wifi.RemoveWifiDevicePayload
import com.hkbufyp.hrms.util.Ktor
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url

class WifiDeviceService(private val ktor: Ktor) {
    suspend fun getWifiDevices() =
        ktor.client.get {
            url(ApiService.Wifi.root)
        }

    suspend fun addWifiDevice(payload: AddWifiDevicePayload) =
        ktor.client.post {
            url(ApiService.Wifi.root)
            setBody(payload)
        }

    suspend fun removeWifiDevice(payload: RemoveWifiDevicePayload) =
        ktor.client.delete {
            url(ApiService.Wifi.root)
            setBody(payload)
        }
}