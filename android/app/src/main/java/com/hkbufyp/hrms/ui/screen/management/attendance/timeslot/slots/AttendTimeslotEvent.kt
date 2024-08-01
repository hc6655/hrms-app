package com.hkbufyp.hrms.ui.screen.management.attendance.timeslot.slots

sealed class AttendTimeslotEvent {
    data object Enter: AttendTimeslotEvent()
}
