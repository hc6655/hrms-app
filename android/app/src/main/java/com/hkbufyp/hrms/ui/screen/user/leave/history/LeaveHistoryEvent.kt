package com.hkbufyp.hrms.ui.screen.user.leave.history

sealed class LeaveHistoryEvent {
    data object Refresh: LeaveHistoryEvent()
    data object Enter: LeaveHistoryEvent()
}
