package com.hkbufyp.hrms.domain.repository

import com.hkbufyp.hrms.data.remote.dto.leave.UpdateLeaveStatusPayload
import com.hkbufyp.hrms.data.remote.dto.leave.LeaveApplicationPayload
import com.hkbufyp.hrms.data.remote.dto.leave.LeaveApplyResponse
import com.hkbufyp.hrms.domain.model.LeaveApplication
import com.hkbufyp.hrms.domain.model.LeaveBalance
import com.hkbufyp.hrms.domain.model.LeaveType
import com.hkbufyp.hrms.util.NetworkResponse
import kotlinx.coroutines.flow.Flow

interface LeaveRepository {
    fun getLeaveTypes(): Flow<NetworkResponse<List<LeaveType>>>
    fun getUserLeaveBalance(id: String): Flow<NetworkResponse<List<LeaveBalance>>>
    fun createLeaveApplication(payload: LeaveApplicationPayload, employeeId: String): Flow<NetworkResponse<Int>>
    fun getApplications(): Flow<NetworkResponse<List<LeaveApplication>>>
    fun getApplicationsByEmployeeId(id: String): Flow<NetworkResponse<List<LeaveApplication>>>
    fun getApplicationById(id: Int): Flow<NetworkResponse<LeaveApplication>>
    fun updateApplicationStatus(id: Int, payload: UpdateLeaveStatusPayload): Flow<NetworkResponse<String>>
}