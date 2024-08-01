package com.hkbufyp.hrms.ui.screen.management.attendance.clockin_devices.ble_devices

import com.hkbufyp.hrms.domain.model.BleDevice

sealed class BleDeviceEvent {
    data object Enter: BleDeviceEvent()
    data object Refresh: BleDeviceEvent()
    data class RemoveDevice(val device: BleDevice): BleDeviceEvent()
    data object Confirm: BleDeviceEvent()
    data object Cancel: BleDeviceEvent()
}
