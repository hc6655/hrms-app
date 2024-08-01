package com.hkbufyp.hrms.data.remote.api

import com.hkbufyp.hrms.data.remote.dto.leave.UpdateLeaveStatusPayload
import com.hkbufyp.hrms.data.remote.dto.leave.LeaveApplicationPayload
import com.hkbufyp.hrms.util.Ktor
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.request.url

class LeaveService(private val ktor: Ktor) {
    suspend fun getLeaveTypes() =
        ktor.client.get {
            url(ApiService.Leave.root)
        }

    suspend fun getUserLeaveBalance(id: String) =
        ktor.client.get {
            url("${ApiService.Leave.userBalance}/$id")
        }

    suspend fun createLeaveApplication(
        payload: LeaveApplicationPayload,
        employeeId: String) =
        ktor.client.post {
            url("${ApiService.Leave.userBalance}/$employeeId")
            setBody(payload)
        }

    suspend fun getLeaveApplications() =
        ktor.client.get {
            url(ApiService.Leave.applications)
        }

    suspend fun getLeaveApplicationById(id: Int) =
        ktor.client.get {
            url("${ApiService.Leave.applications}/$id")
        }

    suspend fun updateLeaveStatus(id: Int, payload: UpdateLeaveStatusPayload) =
        ktor.client.put {
            url("${ApiService.Leave.applications}/$id")
            setBody(payload)
        }

    suspend fun getLeaveApplicationsByEmployeeId(id: String) =
        ktor.client.get {
            url("${ApiService.Leave.applicationsByUser}/$id")
        }
}