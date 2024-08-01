package com.hkbufyp.hrms.ui.screen.management.attendance.clockin_devices.ble_devices_scan

import android.bluetooth.le.ScanResult

sealed class BleScanningEvent {
    data object Enter: BleScanningEvent()
    data object Refresh: BleScanningEvent()
    data object Confirm: BleScanningEvent()
    data class SelectDevice(val v: ScanResult): BleScanningEvent()
    data object Cancel: BleScanningEvent()
}
