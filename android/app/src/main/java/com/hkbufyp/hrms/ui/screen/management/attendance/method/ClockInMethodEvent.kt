package com.hkbufyp.hrms.ui.screen.management.attendance.method

sealed class ClockInMethodEvent {
    data object Submit: ClockInMethodEvent()
    data class SwitchWifi(val on: Boolean): ClockInMethodEvent()
    data class SwitchBLE(val on: Boolean): ClockInMethodEvent()
}