package com.hkbufyp.hrms.ui.screen.management.employee.attend_record

import com.hkbufyp.hrms.domain.model.AttendRecord
import com.hkbufyp.hrms.domain.model.User

data class EmployeeAttendRecordUiState(
    val isLoadingData: Boolean = false,
    val user: User? = null,
    val attendRecord: AttendRecord? = null,
    val message: String = "",
    val startDate: String = "",
    val endDate: String = "",
    val lateBalance: String = "",
    val earlyLeaveBalance: String = "",
    val overtimeBalance: String = "",
    val salary: String = ""
)
