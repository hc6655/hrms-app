package com.hkbufyp.hrms.ui.screen.management.employee.employees

sealed class EmployeesEvent {
    data class TriggerSearch(val isSearch: Boolean): EmployeesEvent()
    data class SearchTextChanged(val text: String): EmployeesEvent()
    data object PerformSearch: EmployeesEvent()
}
