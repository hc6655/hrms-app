package com.hkbufyp.hrms.ui.screen.management.employee.detail

import com.hkbufyp.hrms.domain.model.AttendTimeslot
import com.hkbufyp.hrms.domain.model.AttendTimeslotBrief
import com.hkbufyp.hrms.domain.model.Department
import com.hkbufyp.hrms.domain.model.Position
import com.hkbufyp.hrms.domain.model.PositionBrief
import com.hkbufyp.hrms.domain.model.Role
import com.hkbufyp.hrms.domain.model.User

data class EmployeeDetailUiState(
    val isLoadingData: Boolean = false,
    val isRefreshing: Boolean = false,
    val isEdit: Boolean = false,
    val isSubmitting: Boolean = false,
    val employeeData: User? = null,
    val editedEmployeeData: User? = null,
    val departments: List<Department> = emptyList(),
    val roles: List<Position> = emptyList(),
    val departmentMap: Map<String, Department> = emptyMap(),
    val roleMap: Map<String, Position> = emptyMap(),
    val isDepartmentExpand: Boolean = false,
    val isRoleExpand: Boolean = false,
    val message: String = "",
    val timeslots: List<AttendTimeslotBrief> = emptyList(),
    val timeslotMap: Map<String, AttendTimeslotBrief> = emptyMap(),
    val isTimeslotExpand: Boolean = false,
    val permissionDenied: Boolean = false,
    val workingTypeMap: Map<String, Int> = emptyMap(),
    val salaryTypeMap: Map<String, Int> = emptyMap(),
    val otAllowanceType: Map<String, Int> = emptyMap(),
    val isWorkingTypeExpand: Boolean = false,
    val isSalaryTypeExpand: Boolean = false,
    val isOtAllowanceTypeExpand: Boolean = false,
)
