package com.hkbufyp.hrms.data.remote.api

import com.hkbufyp.hrms.data.remote.dto.fcm.SetFCMTokenPayload
import com.hkbufyp.hrms.util.Ktor
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.request.url

class FCMService(private val ktor: Ktor) {
    suspend fun setFCMToken(id: String, payload: SetFCMTokenPayload) =
        ktor.client.put {
            url(ApiService.FCM.root)
            setBody(payload)
        }
}