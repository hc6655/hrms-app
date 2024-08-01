package com.hkbufyp.hrms.ui.screen.management.attendance.timeslot.detail

import com.hkbufyp.hrms.domain.model.AttendTimeslot

data class AttendTimeslotDetailUiState(
    val isSubmitting: Boolean = false,
    val message: String = "",
    val attendTimeslot: AttendTimeslot? = null,
    val showSelectRangeDialog: Boolean = false,
    val isCreate: Boolean = true,
    val isLoadingData: Boolean = false
)
