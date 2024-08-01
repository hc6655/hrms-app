package com.hkbufyp.hrms.ui.screen.user.leave.detail

sealed class LeaveDetailEvent {
    data object Refresh: LeaveDetailEvent()
}
