package com.hkbufyp.hrms.domain.enums

enum class LeaveApplicationStatus {
    PENDING,
    APPROVED,
    REJECTED;

    companion object {
        fun fromInt(v: Int) =
            values().firstOrNull { it.ordinal == v }
    }
}

fun LeaveApplicationStatus.toInt() = this.ordinal

fun LeaveApplicationStatus.toStr() =
    when (this) {
        LeaveApplicationStatus.PENDING -> "Pending"
        LeaveApplicationStatus.APPROVED -> "Approved"
        LeaveApplicationStatus.REJECTED -> "Rejected"
    }

fun LeaveApplicationStatus.isPending() = this == LeaveApplicationStatus.PENDING
fun LeaveApplicationStatus.isApproved() = this == LeaveApplicationStatus.APPROVED
fun LeaveApplicationStatus.isRejected() = this == LeaveApplicationStatus.REJECTED