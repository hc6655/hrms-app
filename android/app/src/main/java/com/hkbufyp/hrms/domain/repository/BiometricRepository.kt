package com.hkbufyp.hrms.domain.repository

import androidx.biometric.BiometricPrompt.CryptoObject
import com.hkbufyp.hrms.domain.model.BiometricStatus
import kotlinx.coroutines.flow.Flow

interface BiometricRepository {
    suspend fun getCryptoObject(isEncrypt: Boolean): CryptoObject
    fun getBiometricStatus(): Flow<BiometricStatus>
    suspend fun getEncryptedToken(id: String, cryptoObject: CryptoObject)
    fun decryptToken(cryptoObject: CryptoObject): Flow<String>
    suspend fun clean()
}