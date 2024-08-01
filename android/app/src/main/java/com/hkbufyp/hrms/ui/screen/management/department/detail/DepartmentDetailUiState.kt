package com.hkbufyp.hrms.ui.screen.management.department.detail

import com.hkbufyp.hrms.domain.model.Department
import com.hkbufyp.hrms.domain.model.DepartmentDetail
import com.hkbufyp.hrms.domain.model.Position

data class DepartmentDetailUiState(
    val isLoadingData: Boolean = false,
    val isSubmitting: Boolean = false,
    val isCreation: Boolean = true,
    val department: DepartmentDetail? = null,
    val message: String = "",
    val accessLevelList: List<String> = emptyList(),
    val isAccessLevelExpanded: List<Boolean> = emptyList()
)
