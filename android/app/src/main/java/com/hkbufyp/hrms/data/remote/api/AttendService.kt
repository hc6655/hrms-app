package com.hkbufyp.hrms.data.remote.api

import com.hkbufyp.hrms.data.remote.dto.attendance.AddCustomShiftPayload
import com.hkbufyp.hrms.data.remote.dto.attendance.AttendTimeslotDto
import com.hkbufyp.hrms.data.remote.dto.attendance.AttendTimeslotWeeklyPayload
import com.hkbufyp.hrms.data.remote.dto.attendance.GetAttendanceRecordPayload
import com.hkbufyp.hrms.data.remote.dto.attendance.TakeAttendancePayload
import com.hkbufyp.hrms.data.remote.dto.attendance.UpdateAttendanceMethodPayload
import com.hkbufyp.hrms.util.Ktor
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.request.url

class AttendService(private val ktor: Ktor) {
    suspend fun createTimeslot(payload: AttendTimeslotDto) =
        ktor.client.post {
            url(ApiService.Attend.timeslot)
            setBody(payload)
        }

    suspend fun updateTimeslot(payload: AttendTimeslotDto) =
        ktor.client.put {
            url(ApiService.Attend.timeslot)
            setBody(payload)
        }

    suspend fun getTimeSlot() =
        ktor.client.get {
            url(ApiService.Attend.timeslot)
        }

    suspend fun getTimeslotById(id: Int) =
        ktor.client.get {
            url("${ApiService.Attend.timeslot}/$id")
        }

    suspend fun getTimeslotBrief() =
        ktor.client.get {
            url("${ApiService.Attend.timeslot}/brief")
        }

    suspend fun getTimeslotWeeklyByEmployeeId(id: String, payload: AttendTimeslotWeeklyPayload) =
        ktor.client.post {
            url("${ApiService.Attend.time}/$id")
            setBody(payload)
        }

    suspend fun takeAttendance(payload: TakeAttendancePayload) =
        ktor.client.post {
            url(ApiService.Attend.root)
            setBody(payload)
        }

    suspend fun getMethod() =
        ktor.client.get {
            url(ApiService.Attend.method)
        }

    suspend fun updateMethod(payload: UpdateAttendanceMethodPayload) =
        ktor.client.put {
            url(ApiService.Attend.method)
            setBody(payload)
        }

    suspend fun getAttendanceRecord(employeeId: String, payload: GetAttendanceRecordPayload) =
        ktor.client.post {
            url("${ApiService.Attend.root}/$employeeId")
            setBody(payload)
        }

    suspend fun getCustomShift(employeeId: String) =
        ktor.client.get {
            url("${ApiService.Attend.custom}/$employeeId")
        }

    suspend fun addCustomShift(employeeId: String, payload: AddCustomShiftPayload) =
        ktor.client.post {
            url("${ApiService.Attend.custom}/$employeeId")
            setBody(payload)
        }

    suspend fun removeCustomShift(id: Int) =
        ktor.client.delete {
            url("${ApiService.Attend.custom}/$id")
        }
}