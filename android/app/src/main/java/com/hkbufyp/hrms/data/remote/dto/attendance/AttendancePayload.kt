package com.hkbufyp.hrms.data.remote.dto.attendance

import kotlinx.serialization.Serializable

@Serializable
data class AttendTimeslotWeeklyPayload(
    val startDate: String,
    val endDate: String
)

@Serializable
data class TakeAttendancePayload(
    val attendDate: String,
    val device: String,
    val actionType: Int,
    val timeslotId: Int
)

@Serializable
data class UpdateAttendanceMethodPayload(
    val wifiEnable: Boolean,
    val bleEnable: Boolean
)

@Serializable
data class GetAttendanceRecordPayload(
    val startDate: String,
    val endDate: String
)

@Serializable
data class AddCustomShiftPayload(
    val date: String,
    val timeslotId: Int
)