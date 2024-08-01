package com.hkbufyp.hrms.ui.screen.management.attendance.clockin_devices.wifi_devices_scan

import android.net.wifi.ScanResult
import com.hkbufyp.hrms.domain.model.WifiDevice

data class WifiScanningUiState(
    val isLoading: Boolean = false,
    val wifiList: List<ScanResult>? = null,
    val selectedWifi: WifiDevice? = null,
    val isShowConfirmationBox: Boolean = false,
    val isSubmitting: Boolean = false,
    val message: String = ""
)
