package com.hkbufyp.hrms.ui.screen.management.employee.attend_record

sealed class EmployeeAttendRecordEvent {
    data object Previous: EmployeeAttendRecordEvent()
    data object Next: EmployeeAttendRecordEvent()
}