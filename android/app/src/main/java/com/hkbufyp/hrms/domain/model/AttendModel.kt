package com.hkbufyp.hrms.domain.model

import com.hkbufyp.hrms.data.remote.dto.attendance.AttendTimeslotDto
import com.hkbufyp.hrms.data.remote.dto.attendance.UpdateAttendanceMethodPayload
import com.hkbufyp.hrms.util.toCalendar12
import com.hkbufyp.hrms.util.toCalendar24
import com.hkbufyp.hrms.util.toFormatStringByTime12
import com.hkbufyp.hrms.util.toFormatStringByTime24
import com.hkbufyp.hrms.util.toInt
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

enum class ValidClockInRange {
    Automatic,
    Manual
}

@Serializable
data class AttendWorkingTimeslot(
    val id: Int,
    var start: String,
    var end: String
)

@Serializable
data class AttendTimeslotDate(
    val id: Int,
    val name: String,
    val date: String,
    val start: String,
    val end: String
)

fun AttendWorkingTimeslot.to24() =
    AttendWorkingTimeslot(
        id = id,
        start = start.toCalendar12().toFormatStringByTime24(),
        end = end.toCalendar12().toFormatStringByTime24()
    )

fun AttendWorkingTimeslot.to12() =
    AttendWorkingTimeslot(
        id = id,
        start = start.toCalendar24().toFormatStringByTime12(),
        end = end.toCalendar24().toFormatStringByTime12()
    )

fun AttendTimeslotDate.to24() =
    AttendTimeslotDate(
        id = id,
        name = name,
        date = date,
        start = start.toCalendar12().toFormatStringByTime24(),
        end = end.toCalendar12().toFormatStringByTime24()
    )

fun AttendTimeslotDate.to12() =
    AttendTimeslotDate(
        id = id,
        name = name,
        date = date,
        start = start.toCalendar24().toFormatStringByTime12(),
        end = end.toCalendar24().toFormatStringByTime12()
    )

data class AttendTimeslot(
    val id: Int = 0,
    val name: String,
    val slots: List<AttendWorkingTimeslot>,
    val requireClockIn: Boolean,
    val requireClockOut: Boolean,
    val countAbsent: Boolean,
    val countLate: Boolean,
    val countLeaveEarly: Boolean,
    val countOvertime: Boolean,
    val totalWorkingHours: Float,
    val latenessGrace: Int,
    val earlyLeaveGrace: Int,
    val overtimeStart: Int,
    val validClockInRange: ValidClockInRange,
    val rangeOfBeforeWork: Int,
    val rangeOfAfterWork: Int,
    val rangeOfBeforeLeave: Int,
    val rangeOfAfterLeave: Int
)

fun AttendTimeslot.toDto() =
    AttendTimeslotDto(
        name = name,
        slots = slots.map { it.to24() },
        requireClockOut = requireClockIn.toInt(),
        requireClockIn = requireClockOut.toInt(),
        countAbsent = countAbsent.toInt(),
        countLate = countLate.toInt(),
        countLeaveEarly = countLeaveEarly.toInt(),
        countOvertime = countOvertime.toInt(),
        totalWorkingHours = totalWorkingHours,
        latenessGrace = latenessGrace,
        earlyLeaveGrace = earlyLeaveGrace,
        overtimeStart = overtimeStart,
        validClockInRange = validClockInRange.ordinal,
        rangeOfBeforeWork = rangeOfBeforeWork,
        rangeOfAfterWork = rangeOfAfterWork,
        rangeOfBeforeLeave = rangeOfBeforeLeave,
        rangeOfAfterLeave = rangeOfAfterLeave,
        id = id
    )

data class AttendTimeslotBrief(
    val id: Int,
    val name: String,
    val slots: List<AttendWorkingTimeslot>
)

data class AttendMethod(
    val wifiEnable: Boolean,
    val bleEnable: Boolean
)

fun AttendMethod.toUpdatePayload() =
    UpdateAttendanceMethodPayload(
        wifiEnable = wifiEnable,
        bleEnable = bleEnable
    )

data class AttendLog(
    val id: Int,
    val employeeId: String,
    val attendDate: String,
    val dateTime: String,
    val device: String,
    val ip: String,
    val actionType: Int,
    val timeslotId: Int,
    val isSuccess: Boolean,
    val lateMin: Int,
    val earlyLeaveMin: Int,
    val overtimeMin: Int
)

data class AttendBriefByDate(
    val date: String,
    val status: String,
    val inTime: String,
    val outTime: String,
    val lateHour: Float,
    val earlyLeaveHour: Float,
    val overtimeHour: Float,
    val leaveTypeId: Int,
    val timeslotName: String,
    val workingHours: Float
)

data class AttendRecord(
    val startDate: String,
    val endDate: String,
    val totalAttendDays: Int,
    val totalWorkingHours: Float,
    val totalLateHours: Float,
    val totalEarlyLeaveHours: Float,
    val totalOvertimeHours: Float,
    val logs: List<AttendLog>,
    val brief: List<AttendBriefByDate>
)

fun AttendBriefByDate.isLate() = lateHour > 0.0
fun AttendBriefByDate.isEarlyLeave() = earlyLeaveHour > 0.0
fun AttendBriefByDate.isOvertime() = overtimeHour > 0.0
fun AttendBriefByDate.isClockedIn() = inTime.isNotEmpty() && inTime.isNotBlank()
fun AttendBriefByDate.isClockedOut() = outTime.isNotEmpty() && outTime.isNotBlank()

data class AttendCustomShift(
    val id: Int,
    val employeeId: String,
    val date: String,
    val timeslotId: Int,
    val timeslotName: String,
    val start: String,
    val end: String
)
