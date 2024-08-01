package com.hkbufyp.hrms.ui.screen.management.leave.applications

sealed class LeaveApplicationsEvent {
    data object Refresh: LeaveApplicationsEvent()
    data object Enter: LeaveApplicationsEvent()
}
