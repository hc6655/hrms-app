package com.hkbufyp.hrms.ui.screen.management.employee.clock_in_timeslot

import com.hkbufyp.hrms.domain.model.AttendCustomShift
import com.hkbufyp.hrms.domain.model.AttendTimeslotBrief
import java.time.LocalDate

data class EmployeeTimeslotUiState(
    val isLoadingData: Boolean = false,
    val isSubmitting: Boolean = false,
    val selectedDate: LocalDate? = null,
    val message: String = "",
    val isShowAddDialog: Boolean = false,
    val isShowRemoveDialog: Boolean = false,
    val timeslots: List<AttendTimeslotBrief> = emptyList(),
    val shifts: List<AttendCustomShift> = emptyList(),
    val selectedSlot: AttendTimeslotBrief? = null,
    val allShifts: List<AttendCustomShift> = emptyList(),
    val removingShift: AttendCustomShift? = null
)
