package com.hkbufyp.hrms.ui.screen.management.department.departments

import com.hkbufyp.hrms.domain.model.Department

sealed class DepartmentsEvent {
    data object Enter: DepartmentsEvent()
    data object Refresh: DepartmentsEvent()
    data class DeleteClicked(val department: Department): DepartmentsEvent()
    data object DeleteConfirmed: DepartmentsEvent()
    data object DeleteCancelled: DepartmentsEvent()
}
