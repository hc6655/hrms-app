package com.hkbufyp.hrms.domain.repository

import com.hkbufyp.hrms.domain.model.preference.EmployeeInfo
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    fun getEmployeeInfo(): Flow<EmployeeInfo>
    suspend fun saveEmployeeInfo(employeeInfo: EmployeeInfo)
    fun isNewFCMTokenUploaded(): Flow<Boolean>
    suspend fun setNewFCMTokenUploaded(isUploaded: Boolean)
    fun getBiometricToken(): Flow<String>
    suspend fun saveBiometricToken(token: String)
    fun getBiometricIV(): Flow<String>
    suspend fun saveBiometricIV(iv: String)
}