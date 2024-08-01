package com.hkbufyp.hrms.domain.repository

import com.hkbufyp.hrms.data.remote.dto.fcm.SetFCMTokenPayload
import com.hkbufyp.hrms.util.NetworkResponse
import kotlinx.coroutines.flow.Flow

interface FCMRepository {
    fun setFCMToken(id: String, payload: SetFCMTokenPayload): Flow<NetworkResponse<Boolean>>
}