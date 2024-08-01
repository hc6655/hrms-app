package com.hkbufyp.hrms.domain.repository

import com.hkbufyp.hrms.data.remote.dto.attendance.AddCustomShiftPayload
import com.hkbufyp.hrms.data.remote.dto.attendance.AttendCustomShiftDto
import com.hkbufyp.hrms.data.remote.dto.attendance.AttendTimeslotDto
import com.hkbufyp.hrms.data.remote.dto.attendance.AttendTimeslotWeeklyPayload
import com.hkbufyp.hrms.data.remote.dto.attendance.GetAttendanceRecordPayload
import com.hkbufyp.hrms.data.remote.dto.attendance.TakeAttendancePayload
import com.hkbufyp.hrms.data.remote.dto.attendance.UpdateAttendanceMethodPayload
import com.hkbufyp.hrms.domain.model.AttendCustomShift
import com.hkbufyp.hrms.domain.model.AttendMethod
import com.hkbufyp.hrms.domain.model.AttendRecord
import com.hkbufyp.hrms.domain.model.AttendTimeslot
import com.hkbufyp.hrms.domain.model.AttendTimeslotBrief
import com.hkbufyp.hrms.domain.model.AttendTimeslotDate
import com.hkbufyp.hrms.util.NetworkResponse
import kotlinx.coroutines.flow.Flow

interface AttendRepository {
    fun createTimeslot(slot: AttendTimeslotDto): Flow<NetworkResponse<String>>
    fun updateTimeslot(slot: AttendTimeslotDto): Flow<NetworkResponse<String>>
    fun getTimeslot(): Flow<NetworkResponse<List<AttendTimeslot>>>
    fun getTimeslotById(id: Int): Flow<NetworkResponse<AttendTimeslot>>
    fun getTimeslotBrief(): Flow<NetworkResponse<List<AttendTimeslotBrief>>>
    fun getWeeklyTimeslotByEmployeeId(id: String, payload: AttendTimeslotWeeklyPayload): Flow<NetworkResponse<List<AttendTimeslotDate>>>
    fun takeAttendance(payload: TakeAttendancePayload): Flow<NetworkResponse<String>>
    fun getMethod(): Flow<NetworkResponse<AttendMethod>>
    fun updateMethod(payload: UpdateAttendanceMethodPayload): Flow<NetworkResponse<String>>
    fun getAttendanceRecord(employeeId: String, payload: GetAttendanceRecordPayload): Flow<NetworkResponse<AttendRecord>>
    fun getCustomShift(employeeId: String): Flow<NetworkResponse<List<AttendCustomShift>>>
    fun addCustomShift(employeeId: String, payload: AddCustomShiftPayload): Flow<NetworkResponse<String>>
    fun removeCustomShift(id: Int): Flow<NetworkResponse<String>>
}