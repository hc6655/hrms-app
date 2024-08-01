package com.hkbufyp.hrms.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.hkbufyp.hrms.domain.model.preference.EmployeeInfo
import com.hkbufyp.hrms.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class UserPreferencesRepositoryImpl(
    private val context: Context
): UserPreferencesRepository {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("hrms_preferences")

    private object PreferencesKeys {
        val EMPLOYEE_ID = stringPreferencesKey("employee_id")
        val EMPLOYEE_NAME = stringPreferencesKey("employee_name")
        val FCM_TOKEN_UPLOADED = booleanPreferencesKey("fcm_token_uploaded")
        val BIOMETRIC_TOKEN = stringPreferencesKey("biometric_token")
        val BIOMETRIC_IV = stringPreferencesKey("biometric_iv")
    }

    override fun getEmployeeInfo(): Flow<EmployeeInfo> =
        context.dataStore.data.catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map {
            EmployeeInfo(
                employeeId = it[PreferencesKeys.EMPLOYEE_ID] ?: "",
                employeeName = it[PreferencesKeys.EMPLOYEE_NAME] ?: ""
            )
        }


    override suspend fun saveEmployeeInfo(employeeInfo: EmployeeInfo) {
        context.dataStore.edit {
            it[PreferencesKeys.EMPLOYEE_ID] = employeeInfo.employeeId
            it[PreferencesKeys.EMPLOYEE_NAME] = employeeInfo.employeeName
        }
    }

    override fun isNewFCMTokenUploaded(): Flow<Boolean> =
        context.dataStore.data.catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map {
            it[PreferencesKeys.FCM_TOKEN_UPLOADED] ?: false
        }

    override suspend fun setNewFCMTokenUploaded(isUploaded: Boolean) {
        context.dataStore.edit {
            it[PreferencesKeys.FCM_TOKEN_UPLOADED] = isUploaded
        }
    }

    override fun getBiometricToken(): Flow<String> =
        context.dataStore.data.catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map {
            it[PreferencesKeys.BIOMETRIC_TOKEN] ?: ""
        }

    override suspend fun saveBiometricToken(token: String) {
        context.dataStore.edit {
            it[PreferencesKeys.BIOMETRIC_TOKEN] = token
        }
    }

    override fun getBiometricIV(): Flow<String> =
        context.dataStore.data.catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map {
            it[PreferencesKeys.BIOMETRIC_IV] ?: ""
        }

    override suspend fun saveBiometricIV(iv: String) {
        context.dataStore.edit {
            it[PreferencesKeys.BIOMETRIC_IV] = iv
        }
    }
}