package com.hkbufyp.hrms.ui.screen.management.employee.registration

import com.hkbufyp.hrms.domain.model.AttendTimeslotBrief
import com.hkbufyp.hrms.domain.model.Department
import com.hkbufyp.hrms.domain.model.Position
import com.hkbufyp.hrms.domain.model.Role

data class EmployeeRegUiState(
    val isLoadingData: Boolean = false,
    val roles: List<Position> = emptyList(),
    val departments: List<Department> = emptyList(),
    val firstName: String = "",
    val lastName: String = "",
    val nickName: String = "",
    val phone: String = "",
    val isRoleExpand: Boolean = false,
    val isDepartmentExpand: Boolean = false,
    val selectedRole: Position? = null,
    val selectedDepartment: Department? = null,
    val isRegistering: Boolean = false,
    val message: String = "",
    val workingTypeList: List<String> = emptyList(),
    val selectedWorkingType: Int = 0,
    val salaryTypeList: List<String> = emptyList(),
    val selectedSalaryType: Int = 0,
    val salary: Int = 0,
    val otAllowance: Int = 0,
    val otAllowanceTypeList: List<String> = emptyList(),
    val selectedOtAllowanceType: Int = 0,
    val timeslots: List<AttendTimeslotBrief> = emptyList(),
    val selectedTimeslot: AttendTimeslotBrief? = null,
    val isWorkingTypeExpand: Boolean = false,
    val isSalaryTypeExpand: Boolean = false,
    val isOtAllowanceTypeExpand: Boolean = false,
    val isTimeslotExpand: Boolean = false,
)