package com.hkbufyp.hrms.shared

import com.hkbufyp.hrms.domain.model.BiometricStatus
import com.hkbufyp.hrms.domain.model.Permission
import com.hkbufyp.hrms.domain.model.User
import com.hkbufyp.hrms.domain.model.UserJwt
import com.hkbufyp.hrms.domain.model.preference.EmployeeInfo

data class SharedUiState(
    val initialized: Boolean = false,
    val appBarTitle: String = "",
    val isShowTopAppBar: Boolean = false,
    val isEnableSwipeSideBar: Boolean = false,
    val employeeInfo: EmployeeInfo? = null,
    val employeePermission: Permission? = null,
    val jwt: UserJwt? = null,
)
