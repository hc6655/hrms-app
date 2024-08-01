package com.hkbufyp.hrms.ui.screen.user.clockin

import com.hkbufyp.hrms.domain.model.AttendTimeslotDate

sealed class ClockInEvent {
    data class ClockIn(val slot: AttendTimeslotDate): ClockInEvent()
    data class ClockOut(val slot: AttendTimeslotDate): ClockInEvent()
    data object Previous: ClockInEvent()
    data object Next: ClockInEvent()
}
