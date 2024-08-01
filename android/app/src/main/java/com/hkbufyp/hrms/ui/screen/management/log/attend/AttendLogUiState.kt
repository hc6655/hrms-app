package com.hkbufyp.hrms.ui.screen.management.log.attend

import com.hkbufyp.hrms.domain.model.TakeAttendLog


data class AttendLogUiState(
    val isLoadingData: Boolean = false,
    val logs: List<TakeAttendLog> = emptyList(),
    val message: String = ""
)
