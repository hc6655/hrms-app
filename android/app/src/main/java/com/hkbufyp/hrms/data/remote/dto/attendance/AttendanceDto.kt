package com.hkbufyp.hrms.data.remote.dto.attendance

import com.hkbufyp.hrms.domain.model.AttendBriefByDate
import com.hkbufyp.hrms.domain.model.AttendCustomShift
import com.hkbufyp.hrms.domain.model.AttendLog
import com.hkbufyp.hrms.domain.model.AttendMethod
import com.hkbufyp.hrms.domain.model.AttendRecord
import com.hkbufyp.hrms.domain.model.AttendTimeslot
import com.hkbufyp.hrms.domain.model.AttendTimeslotBrief
import com.hkbufyp.hrms.domain.model.AttendTimeslotDate
import com.hkbufyp.hrms.domain.model.AttendWorkingTimeslot
import com.hkbufyp.hrms.domain.model.ValidClockInRange
import com.hkbufyp.hrms.domain.model.to12
import com.hkbufyp.hrms.util.toBoolean
import com.hkbufyp.hrms.util.toCalendar24
import com.hkbufyp.hrms.util.toFormatStringByTime12
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AttendTimeslotDto(
    val id: Int,
    val name: String,
    val slots: List<AttendWorkingTimeslot>,
    @SerialName("require_clock_in")
    val requireClockIn: Int,
    @SerialName("require_clock_out")
    val requireClockOut: Int,
    @SerialName("count_absent")
    val countAbsent: Int,
    @SerialName("count_late")
    val countLate: Int,
    @SerialName("count_leave_early")
    val countLeaveEarly: Int,
    @SerialName("count_overtime")
    val countOvertime: Int,
    @SerialName("total_working_hours")
    val totalWorkingHours: Float,
    @SerialName("lateness_grace")
    val latenessGrace: Int,
    @SerialName("early_leave_grace")
    val earlyLeaveGrace: Int,
    @SerialName("overtime_start")
    val overtimeStart: Int,
    @SerialName("valid_clock_in_range")
    val validClockInRange: Int,
    @SerialName("range_before_work")
    val rangeOfBeforeWork: Int,
    @SerialName("range_after_work")
    val rangeOfAfterWork: Int,
    @SerialName("range_before_leave")
    val rangeOfBeforeLeave: Int,
    @SerialName("range_after_leave")
    val rangeOfAfterLeave: Int
)

fun AttendTimeslotDto.toAttendTimeslot() =
    AttendTimeslot(
        name = name,
        slots = slots.map { it.to12() },
        requireClockIn = requireClockIn.toBoolean(),
        requireClockOut = requireClockOut.toBoolean(),
        countAbsent = countAbsent.toBoolean(),
        countLate = countLate.toBoolean(),
        countLeaveEarly = countLeaveEarly.toBoolean(),
        countOvertime = countOvertime.toBoolean(),
        totalWorkingHours = totalWorkingHours,
        latenessGrace = latenessGrace,
        earlyLeaveGrace = earlyLeaveGrace,
        overtimeStart = overtimeStart,
        validClockInRange = ValidClockInRange.values().firstOrNull { it.ordinal == validClockInRange } ?: ValidClockInRange.Automatic,
        rangeOfBeforeWork = rangeOfBeforeWork,
        rangeOfAfterWork = rangeOfAfterWork,
        rangeOfBeforeLeave = rangeOfBeforeLeave,
        rangeOfAfterLeave = rangeOfAfterLeave,
        id = id
    )

@Serializable
data class AttendTimeslotBriefDto(
    val id: Int,
    val name: String,
    val slots: List<AttendWorkingTimeslot>
)

fun AttendTimeslotBriefDto.toAttendTimeslotBrief() =
    AttendTimeslotBrief(
        id = id,
        name = name,
        slots = slots.map { it.to12() }
    )

@Serializable
data class AttendMethodDto(
    @SerialName("wifi_enable")
    val wifiEnable: Int,
    @SerialName("ble_enable")
    val bleEnable: Int
)

fun AttendMethodDto.toAttendMethod() =
    AttendMethod(
        wifiEnable = wifiEnable.toBoolean(),
        bleEnable = bleEnable.toBoolean()
    )

@Serializable
data class AttendLogDto(
    val id: Int,
    @SerialName("employee_id")
    val employeeId: String,
    @SerialName("attend_date")
    val attendDate: String,
    @SerialName("date_time")
    val dateTime: String,
    val device: String,
    val ip: String,
    @SerialName("action_type")
    val actionType: Int,
    @SerialName("timeslot_id")
    val timeslotId: Int,
    @SerialName("success")
    val isSuccess: Int,
    val late: Int,
    @SerialName("early_leave")
    val earlyLeave: Int,
    val overtime: Int
)

fun AttendLogDto.toAttendLog() =
    AttendLog(
        id = id,
        employeeId = employeeId,
        attendDate = attendDate,
        dateTime = dateTime,
        device = device,
        ip = ip,
        actionType = actionType,
        timeslotId = timeslotId,
        isSuccess = isSuccess.toBoolean(),
        lateMin = late,
        earlyLeaveMin = earlyLeave,
        overtimeMin = overtime
    )

@Serializable
data class AttendBriefByDateDto(
    val date: String,
    val status: String,
    val inTime: String,
    val outTime: String,
    val lateHour: Float,
    val earlyLeaveHour: Float,
    @SerialName("otHour")
    val overtimeHour: Float,
    val leaveTypeId: Int,
    val timeslotName: String,
    val workingHours: Float,
)

fun AttendBriefByDateDto.toAttendBriefByDate() =
    AttendBriefByDate(
        date = date,
        status = status,
        inTime = inTime,
        outTime = outTime,
        lateHour = lateHour,
        earlyLeaveHour = earlyLeaveHour,
        overtimeHour = overtimeHour,
        leaveTypeId = leaveTypeId,
        timeslotName = timeslotName,
        workingHours = workingHours
    )

@Serializable
data class AttendRecordDto(
    val startDate: String,
    val endDate: String,
    val totalAttendDays: Int,
    val totalWorkingHours: Float,
    val totalLateHours: Float,
    val totalEarlyLeaveHours: Float,
    val totalOvertimeHours: Float,
    val logs: List<AttendLogDto>,
    val brief: List<AttendBriefByDateDto>
)

fun AttendRecordDto.toAttendRecord() =
    AttendRecord(
        startDate = startDate,
        endDate = endDate,
        totalAttendDays = totalAttendDays,
        totalWorkingHours = totalWorkingHours,
        totalLateHours = totalLateHours,
        totalEarlyLeaveHours = totalEarlyLeaveHours,
        totalOvertimeHours = totalOvertimeHours,
        logs = logs.map { it.toAttendLog() },
        brief = brief.map { it.toAttendBriefByDate() }
    )

@Serializable
data class AttendCustomShiftDto(
    val id: Int,
    @SerialName("employee_id")
    val employeeId: String,
    val date: String,
    @SerialName("timeslot_id")
    val timeslotId: Int,
    @SerialName("timeslot_name")
    val timeslotName: String,
    val start: String,
    val end: String
)

fun AttendCustomShiftDto.toAttendCustomShift() =
    AttendCustomShift(
        id = id,
        employeeId = employeeId,
        date = date,
        timeslotId = timeslotId,
        timeslotName = timeslotName,
        start = start.toCalendar24().toFormatStringByTime12(),
        end = end.toCalendar24().toFormatStringByTime12()
    )