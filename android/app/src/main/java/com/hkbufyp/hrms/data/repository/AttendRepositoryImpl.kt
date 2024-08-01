package com.hkbufyp.hrms.data.repository

import com.hkbufyp.hrms.data.remote.api.AttendService
import com.hkbufyp.hrms.data.remote.dto.attendance.AddCustomShiftPayload
import com.hkbufyp.hrms.data.remote.dto.attendance.AttendCustomShiftResponse
import com.hkbufyp.hrms.data.remote.dto.attendance.AttendMethodDto
import com.hkbufyp.hrms.data.remote.dto.attendance.AttendRecordDto
import com.hkbufyp.hrms.data.remote.dto.attendance.AttendTimeslotBriefResponse
import com.hkbufyp.hrms.data.remote.dto.attendance.AttendTimeslotDto
import com.hkbufyp.hrms.data.remote.dto.attendance.AttendTimeslotResponse
import com.hkbufyp.hrms.data.remote.dto.attendance.AttendTimeslotWeeklyPayload
import com.hkbufyp.hrms.data.remote.dto.attendance.GetAttendanceRecordPayload
import com.hkbufyp.hrms.data.remote.dto.attendance.TakeAttendancePayload
import com.hkbufyp.hrms.data.remote.dto.attendance.UpdateAttendanceMethodPayload
import com.hkbufyp.hrms.data.remote.dto.attendance.WeeklyAttendTimeslotResponse
import com.hkbufyp.hrms.data.remote.dto.attendance.toAttendCustomShift
import com.hkbufyp.hrms.data.remote.dto.attendance.toAttendMethod
import com.hkbufyp.hrms.data.remote.dto.attendance.toAttendRecord
import com.hkbufyp.hrms.data.remote.dto.attendance.toAttendTimeslot
import com.hkbufyp.hrms.data.remote.dto.attendance.toAttendTimeslotBrief
import com.hkbufyp.hrms.domain.model.AttendCustomShift
import com.hkbufyp.hrms.domain.model.AttendMethod
import com.hkbufyp.hrms.domain.model.AttendRecord
import com.hkbufyp.hrms.domain.model.AttendTimeslot
import com.hkbufyp.hrms.domain.model.AttendTimeslotBrief
import com.hkbufyp.hrms.domain.model.AttendTimeslotDate
import com.hkbufyp.hrms.domain.model.to12
import com.hkbufyp.hrms.domain.model.toDto
import com.hkbufyp.hrms.domain.repository.AttendRepository
import com.hkbufyp.hrms.util.NetworkResponse
import io.ktor.client.call.body
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AttendRepositoryImpl(
    private val attendService: AttendService
): AttendRepository {

    override fun createTimeslot(slot: AttendTimeslotDto) =
        flow {
            val response = attendService.createTimeslot(slot)

            when (response.status) {
                HttpStatusCode.OK -> {
                    emit(NetworkResponse.Success(response.body<String>()))
                }
                else -> {
                    emit(NetworkResponse.Failure(response.body()))
                }
            }
        }

    override fun updateTimeslot(slot: AttendTimeslotDto) =
        flow {
            val response = attendService.updateTimeslot(slot)

            when (response.status) {
                HttpStatusCode.OK -> {
                    emit(NetworkResponse.Success(response.body<String>()))
                }
                else -> {
                    emit(NetworkResponse.Failure(response.body()))
                }
            }
        }

    override fun getTimeslot(): Flow<NetworkResponse<List<AttendTimeslot>>> =
        flow {
            val response = attendService.getTimeSlot()

            when (response.status) {
                HttpStatusCode.OK -> {
                    val timeslotRes = response.body<AttendTimeslotResponse>()
                    val timeslots = timeslotRes.timeslots
                    emit(NetworkResponse.Success(timeslots.map { it.toAttendTimeslot() }))
                }
                else -> {
                    emit(NetworkResponse.Failure(response.body()))
                }
            }
        }

    override fun getTimeslotById(id: Int) =
        flow {
            val response = attendService.getTimeslotById(id)

            when (response.status) {
                HttpStatusCode.OK -> {
                    val res = response.body<AttendTimeslotDto>()
                    emit(NetworkResponse.Success(res.toAttendTimeslot()))
                }
                else -> {
                    emit(NetworkResponse.Failure(response.body()))
                }
            }
        }

    override fun getTimeslotBrief() =
        flow {
            val response = attendService.getTimeslotBrief()

            when (response.status) {
                HttpStatusCode.OK -> {
                    val timeslotRes = response.body<AttendTimeslotBriefResponse>()
                    val timeslots = timeslotRes.timeslots
                    emit(NetworkResponse.Success(timeslots.map { it.toAttendTimeslotBrief() }))
                }
                else -> {
                    emit(NetworkResponse.Failure(response.body()))
                }
            }
        }

    override fun getWeeklyTimeslotByEmployeeId(id: String, payload: AttendTimeslotWeeklyPayload) =
        flow {
            val response = attendService.getTimeslotWeeklyByEmployeeId(id, payload)

            when (response.status) {
                HttpStatusCode.OK -> {
                    val timeslotRes = response.body<WeeklyAttendTimeslotResponse>()
                    val timeslots = timeslotRes.slots
                    emit(NetworkResponse.Success(timeslots.map { it.to12() }))
                }
                else -> {
                    emit(NetworkResponse.Failure(response.body()))
                }
            }
        }

    override fun takeAttendance(payload: TakeAttendancePayload) =
        flow {
            val response = attendService.takeAttendance(payload)

            when (response.status) {
                HttpStatusCode.OK -> {
                    emit(NetworkResponse.Success(response.body<String>()))
                }
                else -> {
                    emit(NetworkResponse.Failure(response.body()))
                }
            }
        }

    override fun getMethod() =
        flow {
            val response = attendService.getMethod()

            when (response.status) {
                HttpStatusCode.OK -> {
                    val method = response.body<AttendMethodDto>()
                    emit(NetworkResponse.Success(method.toAttendMethod()))
                }
                else -> {
                    emit(NetworkResponse.Failure(response.body()))
                }
            }
        }

    override fun updateMethod(payload: UpdateAttendanceMethodPayload) =
        flow {
            val response = attendService.updateMethod(payload)

            when (response.status) {
                HttpStatusCode.OK -> {
                    emit(NetworkResponse.Success(response.body<String>()))
                }
                else -> {
                    emit(NetworkResponse.Failure(response.body()))
                }
            }
        }

    override fun getAttendanceRecord(
        employeeId: String,
        payload: GetAttendanceRecordPayload
    ) =
        flow {
            val response = attendService.getAttendanceRecord(employeeId, payload)

            when (response.status) {
                HttpStatusCode.OK -> {
                    val record = response.body<AttendRecordDto>()
                    emit(NetworkResponse.Success(record.toAttendRecord()))
                }
                else -> {
                    emit(NetworkResponse.Failure(response.body()))
                }
            }
        }

    override fun getCustomShift(employeeId: String) =
        flow {
            val response = attendService.getCustomShift(employeeId)

            when (response.status) {
                HttpStatusCode.OK -> {
                    val shiftResponse = response.body<AttendCustomShiftResponse>()
                    val shifts = shiftResponse.shifts
                    emit(NetworkResponse.Success(shifts.map { it.toAttendCustomShift() }))
                }
                else -> {
                    emit(NetworkResponse.Failure(response.body()))
                }
            }
        }

    override fun addCustomShift(
        employeeId: String,
        payload: AddCustomShiftPayload
    ) =
        flow {
            val response = attendService.addCustomShift(employeeId, payload)

            when (response.status) {
                HttpStatusCode.OK -> {
                    emit(NetworkResponse.Success(response.body<String>()))
                }
                else -> {
                    emit(NetworkResponse.Failure(response.body()))
                }
            }
        }

    override fun removeCustomShift(id: Int) =
        flow {
            val response = attendService.removeCustomShift(id)

            when (response.status) {
                HttpStatusCode.OK -> {
                    emit(NetworkResponse.Success(response.body<String>()))
                }
                else -> {
                    emit(NetworkResponse.Failure(response.body()))
                }
            }
        }
}