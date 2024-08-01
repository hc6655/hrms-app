package com.hkbufyp.hrms.ui.screen.management.attendance.method

import com.hkbufyp.hrms.domain.model.AttendMethod

data class ClockInMethodUiState(
    val isLoadingData: Boolean = false,
    val method: AttendMethod? = null,
    val isSubmitting: Boolean = false,
    val message: String = ""
)
