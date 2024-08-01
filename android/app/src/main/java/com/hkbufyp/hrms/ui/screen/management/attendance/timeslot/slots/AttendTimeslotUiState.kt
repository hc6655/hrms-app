package com.hkbufyp.hrms.ui.screen.management.attendance.timeslot.slots

import com.hkbufyp.hrms.domain.model.AttendTimeslot

data class AttendTimeslotUiState(
    val isLoadingData: Boolean = false,
    val message: String = "",
    val timeslots: List<AttendTimeslot>? = null
)
