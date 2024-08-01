package com.hkbufyp.hrms.ui.screen.management.employee.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hkbufyp.hrms.data.remote.dto.user.CreateUserPayload
import com.hkbufyp.hrms.domain.model.Position
import com.hkbufyp.hrms.domain.model.Role
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

class EmployeeRegViewModel(
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
    private val departmentRepository: DepartmentRepository,
    private val attendRepository: AttendRepository,
): ViewModel() {

    private val _uiState = MutableStateFlow(EmployeeRegUiState())
    val uiState = _uiState.asStateFlow()

    private val _isShowSnackbar = MutableSharedFlow<Boolean>()
    val isShowSnackbar = _isShowSnackbar.asSharedFlow()

    private val _roleList = mutableListOf<Position>()

    init {
        fetchData()
    }

    fun onEvent(event: EmployeeRegEvent) {
        when (event) {
            EmployeeRegEvent.Apply -> { apply() }

            EmployeeRegEvent.RoleExpand -> {
                _uiState.update { state ->
                    state.copy(isRoleExpand = !state.isRoleExpand)
                }
            }

            EmployeeRegEvent.DepartmentExpand -> {
                _uiState.update { state ->
                    state.copy(isDepartmentExpand = !state.isDepartmentExpand)
                }
            }

            EmployeeRegEvent.WorkingTypeExpand -> {
                _uiState.update { state ->
                    state.copy(isWorkingTypeExpand = !state.isWorkingTypeExpand)
                }
            }

            EmployeeRegEvent.SalaryTypeExpand -> {
                _uiState.update { state ->
                    state.copy(isSalaryTypeExpand = !state.isSalaryTypeExpand)
                }
            }

            EmployeeRegEvent.OtAllowanceTypeExpand -> {
                _uiState.update { state ->
                    state.copy(isOtAllowanceTypeExpand = !state.isOtAllowanceTypeExpand)
                }
            }

            EmployeeRegEvent.TimeslotExpand -> {
                _uiState.update { state ->
                    state.copy(isTimeslotExpand = !state.isTimeslotExpand)
                }
            }

            is EmployeeRegEvent.FirstNameChanged -> {
                _uiState.update { state ->
                    state.copy(firstName = event.name)
                }
            }

            is EmployeeRegEvent.LastNameChanged -> {
                _uiState.update { state ->
                    state.copy(lastName = event.name)
                }
            }

            is EmployeeRegEvent.NickNameChanged -> {
                _uiState.update { state ->
                    state.copy(nickName = event.name)
                }
            }

            is EmployeeRegEvent.PhoneChanged -> {
                _uiState.update { state ->
                    state.copy(phone = event.phone)
                }
            }

            is EmployeeRegEvent.SalaryChanged -> {
                val v = event.salary.toIntOrNull() ?: return
                _uiState.update { state ->
                    state.copy(salary = v)
                }
            }

            is EmployeeRegEvent.OtAllowanceChanged -> {
                val v = event.allowance.toIntOrNull() ?: return
                _uiState.update { state ->
                    state.copy(otAllowance = v)
                }
            }

            is EmployeeRegEvent.SelectRole -> {
                _uiState.update { state ->
                    state.copy(
                        selectedRole = event.role,
                        isRoleExpand = false
                    )
                }
            }

            is EmployeeRegEvent.SelectDepartment -> {
                _uiState.update { state ->
                    state.copy(
                        selectedDepartment = event.department,
                        isDepartmentExpand = false,
                        roles = _roleList.filter { role ->
                            role.departmentId == event.department.id
                        },
                        selectedRole = null
                    )
                }
            }

            is EmployeeRegEvent.SelectWorkingType -> {
                _uiState.update { state ->
                    state.copy(
                        selectedWorkingType = event.v,
                        isWorkingTypeExpand = false
                    )
                }
            }

            is EmployeeRegEvent.SelectSalaryType -> {
                _uiState.update { state ->
                    state.copy(
                        selectedSalaryType = event.v,
                        isSalaryTypeExpand = false
                    )
                }
            }

            is EmployeeRegEvent.SelectOtAllowanceType -> {
                _uiState.update { state ->
                    state.copy(
                        selectedOtAllowanceType = event.v,
                        isOtAllowanceTypeExpand = false
                    )
                }
            }

            is EmployeeRegEvent.SelectTimeslot -> {
                _uiState.update { state ->
                    state.copy(
                        selectedTimeslot = event.slot,
                        isTimeslotExpand = false
                    )
                }
            }
        }
    }

    private fun fetchData() {
        _uiState.update { state ->
            state.copy(isLoadingData = true)
        }

        var errorMessage = ""
        viewModelScope.launch {
            try {
                fetchRoles()
                fetchDepartments()
                fetchAttendTimeslot()
                initFixedList()
            } catch (e: Exception) {
                errorMessage = e.message ?: ""
            }

            _uiState.update { state ->
                state.copy(
                    isLoadingData = false,
                    message = errorMessage
                )
            }

            if (!errorMessage.isEmptyOrBlank()) {
                _isShowSnackbar.emit(true)
            }
        }
    }

    private fun initFixedList() {
        _uiState.update { state ->
            state.copy(
                workingTypeList = listOf("Full Time", "Part Time"),
                salaryTypeList = listOf("Monthly", "Hourly"),
                otAllowanceTypeList = listOf("Hourly", "Half-Hour")
            )
        }
    }

    private suspend fun fetchRoles() {
        roleRepository.getRoles().catch { exception ->
            throw Exception(exception.message ?: "")
        }.collect { response ->
            when (response) {
                is NetworkResponse.Success -> {
                    println(response.data)
                    _roleList.clear()
                    response.data?.let { _roleList.addAll(it) }
                }
                is NetworkResponse.Failure -> {
                    throw Exception(response.errMessage ?: "")
                }
            }
        }
    }

    private suspend fun fetchDepartments() {
        departmentRepository.getDepartments().catch { exception ->
            throw Exception(exception.message ?: "")
        }.collect { response ->
            when (response) {
                is NetworkResponse.Success -> {
                    println(response.data)
                    _uiState.update { state ->
                        state.copy(departments = response.data ?: emptyList())
                    }
                }
                is NetworkResponse.Failure -> {
                    throw Exception(response.errMessage ?: "")
                }
            }
        }
    }

    private suspend fun fetchAttendTimeslot() {
        attendRepository.getTimeslotBrief().catch { exception ->
            println(exception.message)
            throw Exception(exception.message)
        }.collect { response ->
            when(response) {
                is NetworkResponse.Success -> {
                    println(response.data)
                    _uiState.update { state ->
                        state.copy(
                            timeslots = response.data ?: emptyList(),
                        )
                    }
                }
                is NetworkResponse.Failure -> {
                    throw Exception(response.errMessage)
                }
            }
        }
    }

    private fun apply() {
        if (uiState.value.isRegistering) {
            return
        }

        _uiState.update { state ->
            state.copy(isRegistering = true)
        }

        val firstName = _uiState.value.firstName
        val lastName = _uiState.value.lastName
        val nickName = _uiState.value.nickName
        val phone = _uiState.value.phone
        val selectedRole = _uiState.value.selectedRole
        val selectedDepartment = _uiState.value.selectedDepartment
        val selectedWorkingType = _uiState.value.selectedWorkingType
        val selectedSalaryType = _uiState.value.selectedSalaryType
        val salary = _uiState.value.salary
        val otAllowance = _uiState.value.otAllowance
        val selectedOtAllowanceType = _uiState.value.selectedOtAllowanceType
        val selectedTimeslot = _uiState.value.selectedTimeslot

        if (firstName.isEmptyOrBlank() ||
            lastName.isEmptyOrBlank() ||
            phone.isEmptyOrBlank() ||
            selectedRole == null ||
            selectedDepartment == null ||
            salary <= 0 ||
            selectedTimeslot == null) {
            _uiState.update { state ->
                state.copy(
                    isRegistering = false,
                    message = "Some data are missing"
                )
            }
            viewModelScope.launch {
                _isShowSnackbar.emit(true)
            }
            return
        }

        viewModelScope.launch {
            userRepository.createUser(
                CreateUserPayload(
                    firstName = firstName,
                    lastName = lastName,
                    roleId = selectedRole.id,
                    departmentId = selectedDepartment.id,
                    phone = phone,
                    nickName = nickName,
                    timeslotId = selectedTimeslot.id,
                    workingType = selectedWorkingType,
                    salaryType = selectedSalaryType,
                    salary = salary,
                    otAllowance = otAllowance,
                    otAllowanceType = selectedOtAllowanceType
                )
            ).catch { exception ->
                _uiState.update { state ->
                    state.copy(
                        isRegistering = false,
                        message = exception.message ?: ""
                    )
                }
                _isShowSnackbar.emit(true)
            }.collect { response ->
                when (response) {
                    is NetworkResponse.Success -> {
                        _uiState.update { state ->
                            state.copy(
                                isRegistering = false,
                                message = "Success, the employee id is ${response.data?.employeeId ?: "Unknown employee id"}"
                            )
                        }
                        _isShowSnackbar.emit(true)
                    }
                    is NetworkResponse.Failure -> {
                        _uiState.update { state ->
                            state.copy(
                                isRegistering = false,
                                message = response.errMessage ?: ""
                            )
                        }
                        _isShowSnackbar.emit(true)
                    }
                }
            }
        }
    }
}