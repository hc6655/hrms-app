package com.hkbufyp.hrms.data.remote.dto.attendance

import com.hkbufyp.hrms.domain.model.AttendTimeslotDate
import kotlinx.serialization.Serializable


@Serializable
data class AttendTimeslotResponse(
    val timeslots: List<AttendTimeslotDto>
)

@Serializable
data class AttendTimeslotBriefResponse(
    val timeslots: List<AttendTimeslotBriefDto>
)

@Serializable
data class WeeklyAttendTimeslotResponse(
    val slots: List<AttendTimeslotDate>
)

@Serializable
data class AttendCustomShiftResponse(
    val shifts: List<AttendCustomShiftDto>
)