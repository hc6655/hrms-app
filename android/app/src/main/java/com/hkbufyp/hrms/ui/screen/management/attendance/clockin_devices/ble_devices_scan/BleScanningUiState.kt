package com.hkbufyp.hrms.ui.screen.management.attendance.clockin_devices.ble_devices_scan

import android.bluetooth.le.ScanResult
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import com.hkbufyp.hrms.domain.model.BleDevice

data class BleScanningUiState(
    val isScanning: Boolean = false,
    val bleList: SnapshotStateMap<String,ScanResult> = mutableStateMapOf(),
    val selectedBle: BleDevice? = null,
    val isShowConfirmationBox: Boolean = false,
    val isSubmitting: Boolean = false,
    val message: String = "",
)
