package com.hkbufyp.hrms.ui.screen.management.log.employee

import com.hkbufyp.hrms.domain.model.ManageEmployeeLog


data class ManageEmployeeLogUiState(
    val isLoadingData: Boolean = false,
    val logs: List<ManageEmployeeLog> = emptyList(),
    val message: String = ""
)
