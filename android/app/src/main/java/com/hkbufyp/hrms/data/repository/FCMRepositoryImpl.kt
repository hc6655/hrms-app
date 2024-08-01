package com.hkbufyp.hrms.data.repository

import com.hkbufyp.hrms.data.remote.api.FCMService
import com.hkbufyp.hrms.data.remote.dto.fcm.SetFCMTokenPayload
import com.hkbufyp.hrms.domain.repository.FCMRepository
import com.hkbufyp.hrms.util.NetworkResponse
import io.ktor.client.call.body
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FCMRepositoryImpl(
    private val fcmService: FCMService
): FCMRepository {
    override fun setFCMToken(
        id: String,
        payload: SetFCMTokenPayload
    ) =
        flow {
            val response = fcmService.setFCMToken(id, payload)

            when (response.status) {
                HttpStatusCode.OK -> {
                    emit(NetworkResponse.Success(true))
                }
                else -> {
                    emit(NetworkResponse.Failure(response.body()))
                }
            }
        }
}