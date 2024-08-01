package com.hkbufyp.hrms.ui.screen.management.attendance.timeslot.detail

import com.hkbufyp.hrms.domain.model.ValidClockInRange

sealed class AttendTimeslotDetailEvent {
    data object Submit: AttendTimeslotDetailEvent()
    data object AddSlot: AttendTimeslotDetailEvent()
    data class RemoveSlot(val index: Int): AttendTimeslotDetailEvent()
    data class SetName(val name: String): AttendTimeslotDetailEvent()
    data class SetStartTime(val index: Int, val time: String): AttendTimeslotDetailEvent()
    data class SetEndTime(val index: Int, val time: String): AttendTimeslotDetailEvent()
    data class SetRequireClockIn(val isCheck: Boolean): AttendTimeslotDetailEvent()
    data class SetRequireClockOut(val isCheck: Boolean): AttendTimeslotDetailEvent()
    data class SetCountAbsent(val isCheck: Boolean): AttendTimeslotDetailEvent()
    data class SetCountLate(val isCheck: Boolean): AttendTimeslotDetailEvent()
    data class SetCountLeaveEarly(val isCheck: Boolean): AttendTimeslotDetailEvent()
    data class SetCountOvertime(val isCheck: Boolean): AttendTimeslotDetailEvent()
    data class SetTotalWorkingHours(val v: String): AttendTimeslotDetailEvent()
    data class SetLatenessGrace(val v: String): AttendTimeslotDetailEvent()
    data class SetEarlyLeaveGrace(val v: String): AttendTimeslotDetailEvent()
    data class SetOvertimeStart(val v: String): AttendTimeslotDetailEvent()
    data class SetValidRangeMode(val mode: ValidClockInRange): AttendTimeslotDetailEvent()
    data class SetRangeOfBeforeWork(val v: String): AttendTimeslotDetailEvent()
    data class SetRangeOfAfterWork(val v: String): AttendTimeslotDetailEvent()
    data class SetRangeOfBeforeLeave(val v: String): AttendTimeslotDetailEvent()
    data class SetRangeOfAfterLeave(val v: String): AttendTimeslotDetailEvent()
    data class ShowSelectRangeDialog(val isShow: Boolean): AttendTimeslotDetailEvent()
}
