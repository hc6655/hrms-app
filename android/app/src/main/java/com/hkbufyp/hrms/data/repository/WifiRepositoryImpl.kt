package com.hkbufyp.hrms.data.repository

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.wifi.ScanResult
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import androidx.core.app.ActivityCompat
import com.hkbufyp.hrms.data.remote.api.WifiDeviceService
import com.hkbufyp.hrms.data.remote.dto.wifi.AddWifiDevicePayload
import com.hkbufyp.hrms.data.remote.dto.wifi.RemoveWifiDevicePayload
import com.hkbufyp.hrms.data.remote.dto.wifi.WifiDeviceResponse
import com.hkbufyp.hrms.data.remote.dto.wifi.toWifiDevice
import com.hkbufyp.hrms.domain.model.WifiDevice
import com.hkbufyp.hrms.domain.repository.WifiRepository
import com.hkbufyp.hrms.util.NetworkResponse
import io.ktor.client.call.body
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow

class WifiRepositoryImpl(
    private val context: Context,
    private val wifiDeviceService: WifiDeviceService
): WifiRepository {
    override fun scanWifi() =
        callbackFlow {
            val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val wifiScanReceiver = object: BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    val success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false)
                    if (success) {
                        if (ActivityCompat.checkSelfPermission(
                                context,
                                Manifest.permission.ACCESS_FINE_LOCATION
                            ) == PackageManager.PERMISSION_GRANTED
                        ) {
                            trySend(wifiManager.scanResults)
                        } else {
                            trySend(emptyList())
                        }
                    } else {
                        trySend(emptyList())
                    }
                }
            }

            val intentFilter = IntentFilter()
            intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
            context.registerReceiver(wifiScanReceiver, intentFilter)

            wifiManager.startScan()

            awaitClose { context.unregisterReceiver(wifiScanReceiver) }
        }

    override fun getWifiDevices() =
        flow {
            val response = wifiDeviceService.getWifiDevices()

            when (response.status) {
                HttpStatusCode.OK -> {
                    val deviceResponse = response.body<WifiDeviceResponse>()
                    val devices = deviceResponse.devices

                    emit(NetworkResponse.Success(devices.map { it.toWifiDevice() }))
                }
                else -> {
                    emit(NetworkResponse.Failure(response.body()))
                }
            }
        }

    override fun addWifiDevice(device: WifiDevice) =
        flow {
            val response = wifiDeviceService.addWifiDevice(
                AddWifiDevicePayload(
                    bssid = device.bssid,
                    ssid = device.ssid
                )
            )

            when (response.status) {
                HttpStatusCode.OK -> {
                    emit(NetworkResponse.Success("Success"))
                }
                else -> {
                    emit(NetworkResponse.Failure(response.body()))
                }
            }
        }

    override fun removeWifiDevice(device: WifiDevice) =
        flow {
            val response = wifiDeviceService.removeWifiDevice(
                RemoveWifiDevicePayload(bssid = device.bssid)
            )

            when (response.status) {
                HttpStatusCode.OK -> {
                    emit(NetworkResponse.Success("Success"))
                }
                else -> {
                    emit(NetworkResponse.Failure(response.body()))
                }
            }
        }

    override fun getCurrentConnectedWifi(): WifiInfo? {
        val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        return wifiManager.connectionInfo
    }
}