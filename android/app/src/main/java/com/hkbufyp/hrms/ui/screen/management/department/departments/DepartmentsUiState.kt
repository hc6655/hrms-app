package com.hkbufyp.hrms.ui.screen.management.department.departments

import com.hkbufyp.hrms.domain.model.Department

data class DepartmentsUiState(
    val isLoadingData: Boolean = false,
    val isRefreshing: Boolean = false,
    val isDeleteClicked: Boolean = false,
    val departments: List<Department> = emptyList(),
    val currentDeleteDepartment: Department? = null,
    val message: String = ""
)
