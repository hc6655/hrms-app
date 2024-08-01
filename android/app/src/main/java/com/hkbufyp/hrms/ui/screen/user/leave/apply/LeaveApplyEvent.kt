package com.hkbufyp.hrms.ui.screen.user.leave.apply

import com.hkbufyp.hrms.domain.model.LeaveType

sealed class LeaveApplyEvent {
    data object Apply: LeaveApplyEvent()
    data object ShowDatePickerForStartDate: LeaveApplyEvent()
    data object ShowDatePickerForEndDate: LeaveApplyEvent()

    data object DismissDatePickerForStartDate: LeaveApplyEvent()
    data object DismissDatePickerForEndDate: LeaveApplyEvent()
    data object LeaveTypeExpandChanged: LeaveApplyEvent()
    data class SelectedStartDate(val date: String): LeaveApplyEvent()
    data class SelectedEndDate(val date: String): LeaveApplyEvent()
    data class ApplyDaysChanged(val days: String): LeaveApplyEvent()
    data class ReasonChanged(val reason: String): LeaveApplyEvent()
    data class SelectedLeaveType(val leaveType: LeaveType): LeaveApplyEvent()
}
