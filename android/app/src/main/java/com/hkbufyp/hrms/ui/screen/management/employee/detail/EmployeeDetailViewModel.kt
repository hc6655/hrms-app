package com.hkbufyp.hrms.ui.screen.management.employee.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hkbufyp.hrms.domain.model.Position
import com.hkbufyp.hrms.domain.model.PositionBrief
import com.hkbufyp.hrms.domain.model.toBrief
import com.hkbufyp.hrms.domain.model.toUpdatePayload
import com.hkbufyp.hrms.domain.repository.AttendRepository
import com.hkbufyp.hrms.domain.repository.DepartmentRepository
import com.hkbufyp.hrms.domain.repository.RoleRepository
import com.hkbufyp.hrms.domain.repository.UserRepository
import com.hkbufyp.hrms.util.NetworkResponse
import com.hkbufyp.hrms.util.isEmptyOrBlank
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EmployeeDetailViewModel(
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
    private val departmentRepository: DepartmentRepository,
    private val attendRepository: AttendRepository,
    private val employeeId: String
): ViewModel() {

    private val _uiState = MutableStateFlow(EmployeeDetailUiState())
    val uiState = _uiState.asStateFlow()

    private val _isShowSnackbar = MutableSharedFlow<Boolean>()
    val isShowSnackbar = _isShowSnackbar.asSharedFlow()

    private val _positionList = mutableListOf<Position>()

    init {
        fetchData()
    }

    fun onEvent(event: EmployeeDetailEvent) {
        when (event) {
            EmployeeDetailEvent.Refresh -> fetchData(true)
            EmployeeDetailEvent.Edit -> {
                _uiState.update { state ->
                    state.copy(isEdit = true)
                }
            }
            EmployeeDetailEvent.Save -> {
                updateEmployee()
            }
            EmployeeDetailEvent.CancelEdit -> {
                _uiState.update { state ->
                    state.copy(
                        isEdit = false,
                        editedEmployeeData = state.employeeData
                    )
                }
            }
            EmployeeDetailEvent.DepartmentExpand -> {
                _uiState.update { state ->
                    state.copy(isDepartmentExpand = !state.isDepartmentExpand)
                }
            }
            EmployeeDetailEvent.RoleExpand -> {
                _uiState.update { state ->
                    state.copy(isRoleExpand = !state.isRoleExpand)
                }
            }
            EmployeeDetailEvent.TimeslotExpand -> {
                _uiState.update { state ->
                    state.copy(isTimeslotExpand = !state.isTimeslotExpand)
                }
            }
            EmployeeDetailEvent.WorkingTypeExpand -> {
                _uiState.update { state ->
                    state.copy(isWorkingTypeExpand = !state.isWorkingTypeExpand)
                }
            }
            EmployeeDetailEvent.SalaryTypeExpand -> {
                _uiState.update { state ->
                    state.copy(isSalaryTypeExpand = !state.isSalaryTypeExpand)
                }
            }
            EmployeeDetailEvent.OtAllowanceTypeExpand -> {
                _uiState.update { state ->
                    state.copy(isOtAllowanceTypeExpand = !state.isOtAllowanceTypeExpand)
                }
            }
            is EmployeeDetailEvent.SelectDepartment -> {
                if (event.department != null) {
                    _uiState.update { state ->
                        state.copy(
                            editedEmployeeData = state.editedEmployeeData?.copy(
                                department = event.department,
                                position = _positionList.filter { it.departmentId == event.department.id }[0].toBrief()
                            ),
                            isDepartmentExpand = false,
                            roleMap = _positionList.filter { it.departmentId == event.department.id }
                                .associateBy { it.name }
                        )
                    }
                }
            }
            is EmployeeDetailEvent.SelectRole -> {
                if (event.role != null) {
                    _uiState.update { state ->
                        state.copy(
                            editedEmployeeData = state.editedEmployeeData?.copy(
                                position = PositionBrief(event.role.id, event.role.name)
                            ),
                            isRoleExpand = false
                        )
                    }
                }
            }
            is EmployeeDetailEvent.SelectTimeslot -> {
                if (event.slot != null) {
                    _uiState.update { state ->
                        state.copy(
                            editedEmployeeData = state.editedEmployeeData?.copy(
                                fixedTimeslotId = event.slot.id,
                                timeslotName = event.slot.name
                            ),
                            isTimeslotExpand = false
                        )
                    }
                }
            }
            is EmployeeDetailEvent.SelectWorkingType -> {
                _uiState.update { state ->
                    state.copy(
                        editedEmployeeData = state.editedEmployeeData?.copy(
                            workingType = event.t
                        ),
                        isWorkingTypeExpand = false
                    )
                }
            }
            is EmployeeDetailEvent.SelectSalaryType -> {
                _uiState.update { state ->
                    state.copy(
                        editedEmployeeData = state.editedEmployeeData?.copy(
                            salaryType = event.t
                        ),
                        isSalaryTypeExpand = false
                    )
                }
            }
            is EmployeeDetailEvent.SelectOtAllowanceType -> {
                _uiState.update { state ->
                    state.copy(
                        editedEmployeeData = state.editedEmployeeData?.copy(
                            otAllowanceType = event.t
                        ),
                        isOtAllowanceTypeExpand = false
                    )
                }
            }
            is EmployeeDetailEvent.FirstNameChanged -> {
                _uiState.update { state ->
                    state.copy(
                        editedEmployeeData = state.editedEmployeeData?.copy(
                            firstName = event.firstName
                        )
                    )
                }
            }
            is EmployeeDetailEvent.LastNameChanged -> {
                _uiState.update { state ->
                    state.copy(
                        editedEmployeeData = state.editedEmployeeData?.copy(
                            lastName = event.lastName
                        )
                    )
                }
            }
            is EmployeeDetailEvent.PhoneChanged -> {
                _uiState.update { state ->
                    state.copy(
                        editedEmployeeData = state.editedEmployeeData?.copy(
                            phone = event.phone
                        )
                    )
                }
            }
            is EmployeeDetailEvent.NicknameChanged -> {
                _uiState.update { state ->
                    state.copy(
                        editedEmployeeData = state.editedEmployeeData?.copy(
                            nickName = event.nickName
                        )
                    )
                }
            }
            is EmployeeDetailEvent.SalaryChanged -> {
                val salary = event.v.toIntOrNull() ?: return

                _uiState.update { state ->
                    state.copy(
                        editedEmployeeData = state.editedEmployeeData?.copy(
                            salary = salary
                        )
                    )
                }
            }
            is EmployeeDetailEvent.OtAllowanceChanged -> {
                val allowance = event.v.toIntOrNull() ?: return

                _uiState.update { state ->
                    state.copy(
                        editedEmployeeData = state.editedEmployeeData?.copy(
                            otAllowance = allowance
                        )
                    )
                }
            }
        }
    }

    private fun fetchData(isRefresh: Boolean = false) {
        _uiState.update { state ->
            state.copy(
                isLoadingData = !isRefresh,
                isRefreshing = isRefresh
            )
        }

        var errMessage = ""

        viewModelScope.launch {
            try {
                fetchDepartment()
                fetchRole()
                fetchEmployee()
                fetchAttendTimeslot()
                setFixedMap()
            } catch (e: Exception) {
                errMessage = e.message ?: "Unknown exception"
            }

            if (errMessage.equals("Permission denied", true)) {
                _uiState.update { state ->
                    state.copy(permissionDenied = true)
                }
            }

            _uiState.update { state ->
                state.copy(
                    isLoadingData = false,
                    isRefreshing = false,
                    message = errMessage
                )
            }

            if (!errMessage.isEmptyOrBlank()) {
                _isShowSnackbar.emit(true)
            }
        }
    }

    private suspend fun fetchDepartment() {
        departmentRepository.getDepartments().catch { exception ->
            throw Exception(exception.message)
        }.collect { response ->
            when(response) {
                is NetworkResponse.Success -> {
                    _uiState.update { state ->
                        state.copy(
                            departments = response.data ?: emptyList(),
                            departmentMap = response.data?.associate { it.title to it } ?: emptyMap()
                        )
                    }
                }
                is NetworkResponse.Failure -> {
                    throw Exception(response.errMessage)
                }
            }
        }
    }

    private suspend fun fetchRole() {
        roleRepository.getRoles().catch { exception ->
            throw Exception(exception.message)
        }.collect { response ->
            when(response) {
                is NetworkResponse.Success -> {
                    /*_uiState.update { state ->
                        state.copy(
                            roles = response.data ?: emptyList(),
                            roleMap = response.data?.associate { it.name to it } ?: emptyMap()
                        )
                    }*/
                    _positionList.clear()
                    response.data?.let { _positionList.addAll(it) }
                }
                is NetworkResponse.Failure -> {
                    throw Exception(response.errMessage)
                }
            }
        }
    }

    private fun setFixedMap() {
        _uiState.update { state ->
            state.copy(
                workingTypeMap = mapOf("Full Time" to 0, "Part Time" to 1),
                salaryTypeMap = mapOf("Monthly" to 0, "Hourly" to 1),
                otAllowanceType = mapOf("Hourly" to 0, "Half-Hour" to 1)
            )
        }
    }

    private suspend fun fetchAttendTimeslot() {
        attendRepository.getTimeslotBrief().catch { exception ->
            throw Exception(exception.message)
        }.collect { response ->
            when(response) {
                is NetworkResponse.Success -> {
                    _uiState.update { state ->
                        state.copy(
                            timeslots = response.data ?: emptyList(),
                            timeslotMap = response.data?.associate { it.name to it } ?: emptyMap()
                        )
                    }
                }
                is NetworkResponse.Failure -> {
                    throw Exception(response.errMessage)
                }
            }
        }
    }

    private suspend fun fetchEmployee() {
        userRepository.getUserInfo(employeeId).catch { exception ->
            throw Exception(exception.message)
        }.collect { response ->
            when(response) {
                is NetworkResponse.Success -> {
                    _uiState.update { state ->
                        state.copy(
                            employeeData = response.data,
                            editedEmployeeData = response.data,
                            roleMap = _positionList.filter { it.departmentId == response.data?.department?.id }
                                .associateBy { it.name }
                        )
                    }
                }
                is NetworkResponse.Failure -> {
                    throw Exception(response.errMessage)
                }
            }
        }
    }

    private fun updateEmployee() {
        val payload = _uiState.value.editedEmployeeData?.toUpdatePayload() ?: return

        _uiState.update { state ->
            state.copy(isSubmitting = true)
        }

        viewModelScope.launch {
            userRepository.updateUser(employeeId, payload).catch { exception ->
                _uiState.update { state ->
                    state.copy(
                        message = exception.message ?: "",
                        isSubmitting = false,
                    )
                }
                _isShowSnackbar.emit(true)
            }.collect { response ->
                when (response) {
                    is NetworkResponse.Success -> {
                        _uiState.update { state ->
                            state.copy(
                                message = "Update succeed",
                                isSubmitting = false,
                                isEdit = false
                            )
                        }
                        _isShowSnackbar.emit(true)
                    }
                    is NetworkResponse.Failure -> {
                        _uiState.update { state ->
                            state.copy(
                                message = response.errMessage ?: "",
                                isSubmitting = false,
                            )
                        }
                        _isShowSnackbar.emit(true)
                    }
                }
            }
        }
    }
}