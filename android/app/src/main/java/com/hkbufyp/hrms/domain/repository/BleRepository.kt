package com.hkbufyp.hrms.domain.repository

import android.bluetooth.le.ScanResult
import com.hkbufyp.hrms.data.remote.dto.ble.AddBleDevicePayload
import com.hkbufyp.hrms.data.remote.dto.ble.RemoveBleDevicePayload
import com.hkbufyp.hrms.domain.model.BleDevice
import com.hkbufyp.hrms.util.NetworkResponse
import kotlinx.coroutines.flow.Flow

interface BleRepository {
    fun scanBLE(): Flow<ScanResult?>
    fun getBleDevices(): Flow<NetworkResponse<List<BleDevice>>>
    fun addBleDevice(payload: AddBleDevicePayload): Flow<NetworkResponse<String>>
    fun removeBleDevice(payload: RemoveBleDevicePayload): Flow<NetworkResponse<String>>
}