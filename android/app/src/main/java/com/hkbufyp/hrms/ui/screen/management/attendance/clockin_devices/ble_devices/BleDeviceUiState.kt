package com.hkbufyp.hrms.ui.screen.management.attendance.clockin_devices.ble_devices

import com.hkbufyp.hrms.domain.model.BleDevice

data class BleDeviceUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val devices: List<BleDevice>? = null,
    val message: String = "",
    val removeDevice: BleDevice? = null,
    val isShowConfirmationBox: Boolean = false,
    val isRemoving: Boolean = false
)
