package com.hkbufyp.hrms.data.repository

import com.hkbufyp.hrms.data.remote.api.LogService
import com.hkbufyp.hrms.data.remote.dto.log.LoginLogResponse
import com.hkbufyp.hrms.data.remote.dto.log.ManageEmployeeLogResponse
import com.hkbufyp.hrms.data.remote.dto.log.ManageLeaveLogResponse
import com.hkbufyp.hrms.data.remote.dto.log.TakeAttendLogResponse
import com.hkbufyp.hrms.data.remote.dto.log.toLoginLog
import com.hkbufyp.hrms.data.remote.dto.log.toManageEmployeeLog
import com.hkbufyp.hrms.data.remote.dto.log.toManageLeaveLog
import com.hkbufyp.hrms.data.remote.dto.log.toTakeAttendLog
import com.hkbufyp.hrms.domain.model.LoginLog
import com.hkbufyp.hrms.domain.model.ManageEmployeeLog
import com.hkbufyp.hrms.domain.model.ManageLeaveLog
import com.hkbufyp.hrms.domain.model.TakeAttendLog
import com.hkbufyp.hrms.domain.repository.LogRepository
import com.hkbufyp.hrms.util.NetworkResponse
import io.ktor.client.call.body
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LogRepositoryImpl(
    private val logService: LogService
): LogRepository {

    override fun getLoginLog() =
        flow {
            val response = logService.getLoginLog()

            when (response.status) {
                HttpStatusCode.OK -> {
                    val loginResponse = response.body<LoginLogResponse>()
                    val logs = loginResponse.logs
                    emit(NetworkResponse.Success(logs.map { it.toLoginLog() }))
                }
                else -> {
                    emit(NetworkResponse.Failure(response.body()))
                }
            }
        }

    override fun getManageEmployeeLog() =
        flow {
            val response = logService.getManageEmployeeLog()

            when (response.status) {
                HttpStatusCode.OK -> {
                    val loginResponse = response.body<ManageEmployeeLogResponse>()
                    val logs = loginResponse.logs
                    emit(NetworkResponse.Success(logs.map { it.toManageEmployeeLog() }))
                }
                else -> {
                    emit(NetworkResponse.Failure(response.body()))
                }
            }
        }

    override fun getManageLeaveLog() =
        flow {
            val response = logService.getManageLeaveLog()

            when (response.status) {
                HttpStatusCode.OK -> {
                    val loginResponse = response.body<ManageLeaveLogResponse>()
                    val logs = loginResponse.logs
                    emit(NetworkResponse.Success(logs.map { it.toManageLeaveLog() }))
                }
                else -> {
                    emit(NetworkResponse.Failure(response.body()))
                }
            }
        }

    override fun getAttendLog() =
        flow {
            val response = logService.getAttendLog()

            when (response.status) {
                HttpStatusCode.OK -> {
                    val attendResponse = response.body<TakeAttendLogResponse>()
                    val logs = attendResponse.logs
                    emit(NetworkResponse.Success(logs.map { it.toTakeAttendLog() }))
                }
                else -> {
                    emit(NetworkResponse.Failure(response.body()))
                }
            }
        }
}