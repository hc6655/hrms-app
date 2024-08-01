package com.hkbufyp.hrms.data.remote.api

import com.hkbufyp.hrms.data.remote.dto.ble.AddBleDevicePayload
import com.hkbufyp.hrms.data.remote.dto.ble.RemoveBleDevicePayload
import com.hkbufyp.hrms.util.Ktor
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url

class BleDeviceService(private val ktor: Ktor) {
    suspend fun getBleDevices() =
        ktor.client.get {
            url(ApiService.Ble.root)
        }

    suspend fun addBleDevice(payload: AddBleDevicePayload) =
        ktor.client.post {
            url(ApiService.Ble.root)
            setBody(payload)
        }

    suspend fun removeBleDevice(payload: RemoveBleDevicePayload) =
        ktor.client.delete {
            url(ApiService.Ble.root)
            setBody(payload)
        }
}