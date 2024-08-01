package com.hkbufyp.hrms.data.repository

import android.Manifest
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.pm.PackageManager
import android.os.Handler
import androidx.core.app.ActivityCompat
import com.hkbufyp.hrms.data.remote.api.BleDeviceService
import com.hkbufyp.hrms.data.remote.dto.ble.AddBleDevicePayload
import com.hkbufyp.hrms.data.remote.dto.ble.BleDeviceResponse
import com.hkbufyp.hrms.data.remote.dto.ble.RemoveBleDevicePayload
import com.hkbufyp.hrms.data.remote.dto.ble.toBleDevice
import com.hkbufyp.hrms.domain.model.BleDevice
import com.hkbufyp.hrms.domain.repository.BleRepository
import com.hkbufyp.hrms.util.NetworkResponse
import io.ktor.client.call.body
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow

class BleRepositoryImpl(
    private val context: Context,
    private val bleDeviceService: BleDeviceService
): BleRepository {

    override fun scanBLE() =
        callbackFlow {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                trySend(null)
            } else {
                val bleManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
                val bleScanner = bleManager.adapter.bluetoothLeScanner
                val handler = Handler()
                val period: Long = 10000
                val leScanCallback: ScanCallback = object: ScanCallback() {
                    override fun onScanResult(callbackType: Int, result: ScanResult) {
                        super.onScanResult(callbackType, result)
                        trySend(result)
                    }
                }

                handler.postDelayed({
                    bleScanner.stopScan(leScanCallback)
                    trySend(null)
                }, period)

                bleScanner.startScan(leScanCallback)

                awaitClose { bleScanner.stopScan(leScanCallback) }
            }
        }

    override fun getBleDevices() =
        flow {
            val response = bleDeviceService.getBleDevices()

            when (response.status) {
                HttpStatusCode.OK -> {
                    val bleResponse = response.body<BleDeviceResponse>()
                    val devices = bleResponse.devices
                    emit(NetworkResponse.Success(devices.map { it.toBleDevice() }))
                }
                else -> {
                    emit(NetworkResponse.Failure(response.body()))
                }
            }
        }

    override fun addBleDevice(payload: AddBleDevicePayload) =
        flow {
            val response = bleDeviceService.addBleDevice(payload)

            when (response.status) {
                HttpStatusCode.OK -> {
                    emit(NetworkResponse.Success(response.body<String>()))
                }
                else -> {
                    emit(NetworkResponse.Failure(response.body()))
                }
            }
        }

    override fun removeBleDevice(payload: RemoveBleDevicePayload) =
        flow {
            val response = bleDeviceService.removeBleDevice(payload)

            when (response.status) {
                HttpStatusCode.OK -> {
                    emit(NetworkResponse.Success(response.body<String>()))
                }
                else -> {
                    emit(NetworkResponse.Failure(response.body()))
                }
            }
        }
}