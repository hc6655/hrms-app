package com.hkbufyp.hrms.ui.screen.management.employee.clock_in_timeslot

import com.hkbufyp.hrms.domain.model.AttendCustomShift
import com.hkbufyp.hrms.domain.model.AttendTimeslotBrief
import java.time.LocalDate

sealed class EmployeeTimeslotEvent {
    data class SelectDate(val date: LocalDate): EmployeeTimeslotEvent()
    data object AddShift: EmployeeTimeslotEvent()
    data class ConfirmAdd(val slot: AttendTimeslotBrief): EmployeeTimeslotEvent()
    data class RemoveShift(val shift: AttendCustomShift): EmployeeTimeslotEvent()
    data object ConfirmRemove: EmployeeTimeslotEvent()
    data object CancelAdd: EmployeeTimeslotEvent()
    data object CancelRemove: EmployeeTimeslotEvent()
}
