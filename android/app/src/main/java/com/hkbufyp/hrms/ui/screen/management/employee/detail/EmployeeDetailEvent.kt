package com.hkbufyp.hrms.ui.screen.management.employee.detail

import com.hkbufyp.hrms.domain.model.AttendTimeslotBrief
import com.hkbufyp.hrms.domain.model.Department
import com.hkbufyp.hrms.domain.model.Position
import com.hkbufyp.hrms.domain.model.PositionBrief
import com.hkbufyp.hrms.domain.model.Role

sealed class EmployeeDetailEvent {
    data object Refresh: EmployeeDetailEvent()
    data object Edit: EmployeeDetailEvent()
    data object Save: EmployeeDetailEvent()
    data object CancelEdit: EmployeeDetailEvent()
    data object DepartmentExpand: EmployeeDetailEvent()
    data object RoleExpand: EmployeeDetailEvent()
    data object TimeslotExpand: EmployeeDetailEvent()
    data object WorkingTypeExpand: EmployeeDetailEvent()
    data object SalaryTypeExpand: EmployeeDetailEvent()
    data object OtAllowanceTypeExpand: EmployeeDetailEvent()
    data class SelectDepartment(val department: Department?): EmployeeDetailEvent()
    data class SelectRole(val role: Position?): EmployeeDetailEvent()
    data class FirstNameChanged(val firstName: String): EmployeeDetailEvent()
    data class LastNameChanged(val lastName: String): EmployeeDetailEvent()
    data class PhoneChanged(val phone: String): EmployeeDetailEvent()
    data class NicknameChanged(val nickName: String): EmployeeDetailEvent()
    data class SelectTimeslot(val slot: AttendTimeslotBrief?): EmployeeDetailEvent()
    data class SelectWorkingType(val t: Int): EmployeeDetailEvent()
    data class SelectSalaryType(val t: Int): EmployeeDetailEvent()
    data class SelectOtAllowanceType(val t: Int): EmployeeDetailEvent()
    data class SalaryChanged(val v: String): EmployeeDetailEvent()
    data class OtAllowanceChanged(val v: String): EmployeeDetailEvent()
}
