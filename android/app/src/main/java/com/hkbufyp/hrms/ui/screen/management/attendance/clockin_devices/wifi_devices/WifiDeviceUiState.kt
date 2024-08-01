package com.hkbufyp.hrms.ui.screen.management.attendance.clockin_devices.wifi_devices

import com.hkbufyp.hrms.domain.model.WifiDevice

data class WifiDeviceUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val devices: List<WifiDevice>? = null,
    val message: String = "",
    val removeDevice: WifiDevice? = null,
    val isShowConfirmationBox: Boolean = false,
    val isRemoving: Boolean = false
)
