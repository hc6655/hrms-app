package com.hkbufyp.hrms.ui.screen.management.employee.employees

import com.hkbufyp.hrms.domain.model.User

data class EmployeesUiState(
    val isLoadingData: Boolean = false,
    val employees: List<User> = emptyList(),
    val employeesGroup: Collection<List<User>> = emptyList(),
    val isShowAppbar: Boolean = true,
    val isSearching: Boolean = false,
    val searchText: String = ""
)
