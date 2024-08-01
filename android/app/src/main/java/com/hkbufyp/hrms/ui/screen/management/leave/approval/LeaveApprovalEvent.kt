package com.hkbufyp.hrms.ui.screen.management.leave.approval

sealed class LeaveApprovalEvent {
    data object Refresh: LeaveApprovalEvent()
    data object Approve: LeaveApprovalEvent()
    data object Reject: LeaveApprovalEvent()
    data object ShowDialog: LeaveApprovalEvent()
    data object HideDialog: LeaveApprovalEvent()
    data class ReasonChanged(val v: String): LeaveApprovalEvent()
}
