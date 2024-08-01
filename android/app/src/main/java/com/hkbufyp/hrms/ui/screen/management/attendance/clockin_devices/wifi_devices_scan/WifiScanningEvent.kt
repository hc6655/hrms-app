package com.hkbufyp.hrms.ui.screen.management.attendance.clockin_devices.wifi_devices_scan

import android.net.wifi.ScanResult

sealed class WifiScanningEvent {
    data object Refresh: WifiScanningEvent()
    data class SelectDevice(val device: ScanResult): WifiScanningEvent()
    data object ConfirmSelect: WifiScanningEvent()
    data object CancelSelect: WifiScanningEvent()
}
