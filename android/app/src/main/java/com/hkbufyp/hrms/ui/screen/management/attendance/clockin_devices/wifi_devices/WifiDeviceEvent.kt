package com.hkbufyp.hrms.ui.screen.management.attendance.clockin_devices.wifi_devices

import com.hkbufyp.hrms.domain.model.WifiDevice

sealed class WifiDeviceEvent {
    data object Refresh: WifiDeviceEvent()
    data object Enter: WifiDeviceEvent()
    data class RemoveDevice(val device: WifiDevice): WifiDeviceEvent()
    data object ConfirmRemove: WifiDeviceEvent()
    data object CancelRemove: WifiDeviceEvent()
}
