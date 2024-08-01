package com.hkbufyp.hrms.domain.repository

import com.hkbufyp.hrms.domain.model.LoginLog
import com.hkbufyp.hrms.domain.model.ManageEmployeeLog
import com.hkbufyp.hrms.domain.model.ManageLeaveLog
import com.hkbufyp.hrms.domain.model.TakeAttendLog
import com.hkbufyp.hrms.util.NetworkResponse
import kotlinx.coroutines.flow.Flow

interface LogRepository {
    fun getLoginLog(): Flow<NetworkResponse<List<LoginLog>>>
    fun getManageEmployeeLog(): Flow<NetworkResponse<List<ManageEmployeeLog>>>
    fun getManageLeaveLog(): Flow<NetworkResponse<List<ManageLeaveLog>>>
    fun getAttendLog(): Flow<NetworkResponse<List<TakeAttendLog>>>
}