package com.hkbufyp.hrms.shared

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.biometric.BiometricPrompt
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging
import com.hkbufyp.hrms.data.remote.dto.fcm.SetFCMTokenPayload
import com.hkbufyp.hrms.domain.model.Permission
import com.hkbufyp.hrms.domain.model.UserJwt
import com.hkbufyp.hrms.domain.model.preference.EmployeeInfo
import com.hkbufyp.hrms.domain.repository.BiometricRepository
import com.hkbufyp.hrms.domain.repository.FCMRepository
import com.hkbufyp.hrms.domain.repository.UserPreferencesRepository
import com.hkbufyp.hrms.domain.repository.UserRepository
import com.hkbufyp.hrms.domain.repository.WifiRepository
import com.hkbufyp.hrms.ui.screen.home.SideBarActionNav
import com.hkbufyp.hrms.util.NetworkResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

class SharedViewModel(
    private val userRepository: UserRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
): ViewModel() {

    private val _sharedUiState = MutableStateFlow(SharedUiState())
    val sharedUiState = _sharedUiState.asStateFlow()

    /* ---- TEST ONLY ---- */
    var isAutoLoggedIn by mutableStateOf(false)
    /*---------------------*/

    //private val

    init {
        viewModelScope.launch {
            userPreferencesRepository.getEmployeeInfo().collect {
                _sharedUiState.update { state ->
                    state.copy(
                        employeeInfo = it,
                        initialized = true
                    )
                }
            }
        }

        viewModelScope.launch {
            val token = Firebase.messaging.token.await()
            println("Firebase Token: $token")
        }
    }

    fun setAppBarTitle(str: String) {
        _sharedUiState.update { state ->
            state.copy(
                appBarTitle = str,
                isShowTopAppBar = true
            )
        }
    }
    fun isShowAppBar(b: Boolean) {
        _sharedUiState.update { state ->
            state.copy(isShowTopAppBar = b)
        }
    }

    fun isEnableSwipeSideBar(isEnable: Boolean) {
        _sharedUiState.update { state ->
            state.copy(isEnableSwipeSideBar = isEnable)
        }
    }

    fun setEmployeeInfo(jwt: UserJwt?) {
        if (jwt == null) {
            return
        }

        saveEmployeeInfo(jwt)

        _sharedUiState.update { state ->
            state.copy(
                employeePermission = Permission(jwt.accessLevel, jwt.managementFeature, jwt.accessLog),
                jwt = jwt
            )
        }
    }

    suspend fun updateUserPermission() {
        /*userRepository.getUserPermission(_sharedUiState.value.employeeInfo?.employeeId ?: "").collect {
            when (it) {
                is NetworkResponse.Success -> {
                    _sharedUiState.update { state ->
                        state.copy(employeePermission = it.data)
                    }
                }
                else -> {

                }
            }
        }*/
    }

    private fun saveEmployeeInfo(jwt: UserJwt) {
        viewModelScope.launch(Dispatchers.IO) {
            userPreferencesRepository.saveEmployeeInfo(
                EmployeeInfo(
                    employeeId = jwt.id,
                    employeeName = jwt.nickName
                )
            )
        }
    }

    fun resetData() {
        /*_sharedUiState.update { state ->
            state.copy(userJwt = null)
        }*/

        SideBarActionNav.shrinkAll()
    }
}