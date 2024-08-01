package com.hkbufyp.hrms.ui.screen.user.leave.balance

import com.hkbufyp.hrms.data.remote.dto.leave.LeaveBalanceResponse
import com.hkbufyp.hrms.domain.model.LeaveBalance

data class LeaveBalanceUiState(
    val isLoadingData: Boolean = false,
    val balances: List<LeaveBalance> = emptyList(),
    val message: String = ""
)
