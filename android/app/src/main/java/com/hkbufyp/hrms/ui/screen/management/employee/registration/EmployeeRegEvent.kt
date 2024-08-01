package com.hkbufyp.hrms.ui.screen.management.employee.registration

import com.hkbufyp.hrms.domain.model.AttendTimeslotBrief
import com.hkbufyp.hrms.domain.model.Department
import com.hkbufyp.hrms.domain.model.Position
import com.hkbufyp.hrms.domain.model.Role

sealed class EmployeeRegEvent {
    data object Apply: EmployeeRegEvent()
    data object RoleExpand: EmployeeRegEvent()
    data object DepartmentExpand: EmployeeRegEvent()
    data object WorkingTypeExpand: EmployeeRegEvent()
    data object SalaryTypeExpand: EmployeeRegEvent()
    data object OtAllowanceTypeExpand: EmployeeRegEvent()
    data object TimeslotExpand: EmployeeRegEvent()
    data class FirstNameChanged(val name: String): EmployeeRegEvent()
    data class LastNameChanged(val name: String): EmployeeRegEvent()
    data class NickNameChanged(val name: String): EmployeeRegEvent()
    data class PhoneChanged(val phone: String): EmployeeRegEvent()
    data class SalaryChanged(val salary: String): EmployeeRegEvent()
    data class OtAllowanceChanged(val allowance: String): EmployeeRegEvent()
    data class SelectRole(val role: Position): EmployeeRegEvent()
    data class SelectDepartment(val department: Department): EmployeeRegEvent()
    data class SelectWorkingType(val v: Int): EmployeeRegEvent()
    data class SelectSalaryType(val v: Int): EmployeeRegEvent()
    data class SelectOtAllowanceType(val v: Int): EmployeeRegEvent()
    data class SelectTimeslot(val slot: AttendTimeslotBrief): EmployeeRegEvent()
}
