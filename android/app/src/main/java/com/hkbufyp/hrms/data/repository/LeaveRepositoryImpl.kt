package com.hkbufyp.hrms.data.repository

import com.hkbufyp.hrms.data.remote.api.LeaveService
import com.hkbufyp.hrms.data.remote.dto.leave.UpdateLeaveStatusPayload
import com.hkbufyp.hrms.data.remote.dto.leave.LeaveApplicationPayload
import com.hkbufyp.hrms.data.remote.dto.leave.LeaveApplicationDto
import com.hkbufyp.hrms.data.remote.dto.leave.LeaveApplicationResponse
import com.hkbufyp.hrms.data.remote.dto.leave.LeaveApplyResponse
import com.hkbufyp.hrms.data.remote.dto.leave.LeaveBalanceResponse
import com.hkbufyp.hrms.data.remote.dto.leave.LeaveTypeResponse
import com.hkbufyp.hrms.data.remote.dto.leave.toLeaveApplication
import com.hkbufyp.hrms.data.remote.dto.leave.toLeaveBalance
import com.hkbufyp.hrms.data.remote.dto.leave.toLeaveType
import com.hkbufyp.hrms.domain.repository.LeaveRepository
import com.hkbufyp.hrms.util.NetworkResponse
import io.ktor.client.call.body
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.flow.flow

class LeaveRepositoryImpl(
    private val leaveService: LeaveService
): LeaveRepository {
    override fun getLeaveTypes() =
        flow {
            val response = leaveService.getLeaveTypes()

            when (response.status) {
                HttpStatusCode.OK -> {
                    val typeResponse = response.body<LeaveTypeResponse>()
                    val leaveTypes = typeResponse.leaveTypes

                    emit(NetworkResponse.Success(leaveTypes.map { it.toLeaveType() }))
                }
                else -> {
                    emit(NetworkResponse.Failure(response.body()))
                }
            }
        }

    override fun getUserLeaveBalance(id: String) =
        flow {
            val response = leaveService.getUserLeaveBalance(id)

            when (response.status) {
                HttpStatusCode.OK -> {
                    val balanceResponse = response.body<LeaveBalanceResponse>()
                    val balances = balanceResponse.balances

                    emit(NetworkResponse.Success(balances.map { it.toLeaveBalance() }))
                }
                else -> {
                    emit(NetworkResponse.Failure(response.body()))
                }
            }
        }

    override fun createLeaveApplication(
        payload: LeaveApplicationPayload,
        employeeId: String) =
        flow {
            val response = leaveService.createLeaveApplication(payload, employeeId)

            when (response.status) {
                HttpStatusCode.OK -> {
                    val application = response.body<LeaveApplyResponse>()
                    emit(NetworkResponse.Success(application.applicationId.toInt()))
                }
                else -> {
                    emit(NetworkResponse.Failure(response.body()))
                }
            }
        }

    override fun getApplications() =
        flow {
            val response = leaveService.getLeaveApplications()

            when (response.status) {
                HttpStatusCode.OK -> {
                    val applicationsResponse = response.body<LeaveApplicationResponse>()
                    val applications = applicationsResponse.applications

                    emit(NetworkResponse.Success(applications.map { it.toLeaveApplication() }.reversed()))
                }
                else -> {
                    emit(NetworkResponse.Failure(response.body()))
                }
            }
        }

    override fun getApplicationsByEmployeeId(id: String) =
        flow {
            val response = leaveService.getLeaveApplicationsByEmployeeId(id)

            when (response.status) {
                HttpStatusCode.OK -> {
                    val applicationsResponse = response.body<LeaveApplicationResponse>()
                    val applications = applicationsResponse.applications

                    emit(NetworkResponse.Success(applications.map { it.toLeaveApplication() }.reversed()))
                }
                else -> {
                    emit(NetworkResponse.Failure(response.body()))
                }
            }
        }

    override fun getApplicationById(id: Int) =
        flow {
            val response = leaveService.getLeaveApplicationById(id)

            when (response.status) {
                HttpStatusCode.OK -> {
                    val applicationDto = response.body<LeaveApplicationDto>()
                    emit(NetworkResponse.Success(applicationDto.toLeaveApplication()))
                }
                else -> {
                    emit(NetworkResponse.Failure(response.body()))
                }
            }
        }

    override fun updateApplicationStatus(
        id: Int,
        payload: UpdateLeaveStatusPayload) =
        flow {
            val response = leaveService.updateLeaveStatus(id, payload)

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