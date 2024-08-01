package com.hkbufyp.hrms.data.remote.api

import com.hkbufyp.hrms.util.Ktor
import io.ktor.client.request.get
import io.ktor.client.request.url

class LogService(private val ktor: Ktor) {
    suspend fun getLoginLog() =
        ktor.client.get {
            url(ApiService.Log.login)
        }

    suspend fun getManageEmployeeLog() =
        ktor.client.get {
            url(ApiService.Log.employee)
        }

    suspend fun getManageLeaveLog() =
        ktor.client.get {
            url(ApiService.Log.leave)
        }

    suspend fun getAttendLog() =
        ktor.client.get {
            url(ApiService.Log.attend)
        }
}