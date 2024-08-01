package com.hkbufyp.hrms.ui.screen.management.employee.registration

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.hkbufyp.hrms.shared.SharedViewModel
import com.hkbufyp.hrms.ui.components.LoadingIndicator
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeRegScreen(
    snackbarHostState: SnackbarHostState,
    sharedViewModel: SharedViewModel,
    employeeRegViewModel: EmployeeRegViewModel
) {

    val uiState by employeeRegViewModel.uiState.collectAsState()
    val focusManager = LocalFocusManager.current

    LaunchedEffect(true) {
        sharedViewModel.setAppBarTitle("Employee Registration")

        employeeRegViewModel.isShowSnackbar.collectLatest {
            if (it) {
                snackbarHostState.showSnackbar(uiState.message)
            }
        }
    }

    if (uiState.isLoadingData) {
        LoadingIndicator()
    } else {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .background(color = Color.White)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "First Name: ",
                    color = Color.Black,
                    modifier = Modifier.fillMaxWidth(0.3f),
                    textAlign = TextAlign.Left
                )
                TextField(
                    value = uiState.firstName,
                    onValueChange = {
                        employeeRegViewModel.onEvent(EmployeeRegEvent.FirstNameChanged(it))
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent,
                        focusedIndicatorColor = Color(0xff424549),
                        unfocusedIndicatorColor = Color(0xff424549),
                        disabledIndicatorColor = Color.Transparent,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                    ),
                    readOnly = uiState.isRegistering,
                    singleLine = true
                )
            }

            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Last Name: ",
                    color = Color.Black,
                    modifier = Modifier.fillMaxWidth(0.3f),
                    textAlign = TextAlign.Left
                )
                TextField(
                    value = uiState.lastName,
                    onValueChange = {
                        employeeRegViewModel.onEvent(EmployeeRegEvent.LastNameChanged(it))
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent,
                        focusedIndicatorColor = Color(0xff424549),
                        unfocusedIndicatorColor = Color(0xff424549),
                        disabledIndicatorColor = Color.Transparent,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                    ),
                    readOnly = uiState.isRegistering,
                    singleLine = true
                )
            }

            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Nick Name: ",
                    color = Color.Black,
                    modifier = Modifier.fillMaxWidth(0.3f),
                    textAlign = TextAlign.Left
                )
                TextField(
                    value = uiState.nickName,
                    onValueChange = {
                        employeeRegViewModel.onEvent(EmployeeRegEvent.NickNameChanged(it))
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent,
                        focusedIndicatorColor = Color(0xff424549),
                        unfocusedIndicatorColor = Color(0xff424549),
                        disabledIndicatorColor = Color.Transparent,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                    ),
                    readOnly = uiState.isRegistering,
                    singleLine = true
                )
            }

            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Phone: ",
                    color = Color.Black,
                    modifier = Modifier.fillMaxWidth(0.3f),
                    textAlign = TextAlign.Left
                )
                TextField(
                    value = uiState.phone,
                    onValueChange = {
                        employeeRegViewModel.onEvent(EmployeeRegEvent.PhoneChanged(it))
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent,
                        focusedIndicatorColor = Color(0xff424549),
                        unfocusedIndicatorColor = Color(0xff424549),
                        disabledIndicatorColor = Color.Transparent,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                    ),
                    readOnly = uiState.isRegistering,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true
                )
            }

            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Department: ",
                    color = Color.Black,
                    modifier = Modifier.fillMaxWidth(0.3f),
                    textAlign = TextAlign.Left
                )
                ExposedDropdownMenuBox(
                    expanded = uiState.isDepartmentExpand,
                    onExpandedChange = {
                        if (!uiState.isRegistering) {
                            employeeRegViewModel.onEvent(EmployeeRegEvent.DepartmentExpand)
                        }
                    },
                    modifier = Modifier.fillMaxWidth())
                {
                    TextField(
                        value = uiState.selectedDepartment?.title ?: "Please select a department",
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = uiState.isDepartmentExpand)
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.Transparent,
                            focusedIndicatorColor = Color(0xff424549),
                            unfocusedIndicatorColor = Color(0xff424549),
                            disabledIndicatorColor = Color.Transparent
                        ),
                    )

                    ExposedDropdownMenu(
                        expanded = uiState.isDepartmentExpand,
                        onDismissRequest = {
                            employeeRegViewModel.onEvent(EmployeeRegEvent.DepartmentExpand)
                        },
                        modifier = Modifier.background(color = Color.White))
                    {
                        uiState.departments.forEach {
                            DropdownMenuItem(
                                text = { Text(text = it.title) },
                                onClick = {
                                    employeeRegViewModel.onEvent(
                                        EmployeeRegEvent.SelectDepartment(
                                            it
                                        )
                                    )
                                }
                            )
                        }
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Position: ",
                    color = Color.Black,
                    modifier = Modifier.fillMaxWidth(0.3f),
                    textAlign = TextAlign.Left
                )
                ExposedDropdownMenuBox(
                    expanded = uiState.isRoleExpand,
                    onExpandedChange = {
                        if (!uiState.isRegistering) {
                            employeeRegViewModel.onEvent(EmployeeRegEvent.RoleExpand)
                        }
                    },
                    modifier = Modifier.fillMaxWidth())
                {
                    TextField(
                        value = uiState.selectedRole?.name ?: "Please select a position",
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = uiState.isRoleExpand)
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.Transparent,
                            focusedIndicatorColor = Color(0xff424549),
                            unfocusedIndicatorColor = Color(0xff424549),
                            disabledIndicatorColor = Color.Transparent
                        ),
                    )

                    ExposedDropdownMenu(
                        expanded = uiState.isRoleExpand,
                        onDismissRequest = { employeeRegViewModel.onEvent(EmployeeRegEvent.RoleExpand) },
                        modifier = Modifier.background(color = Color.White))
                    {
                        uiState.roles.forEach {
                            DropdownMenuItem(
                                text = { Text(text = it.name) },
                                onClick = {
                                    employeeRegViewModel.onEvent(EmployeeRegEvent.SelectRole(it))
                                }
                            )
                        }
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Working Type: ",
                    color = Color.Black,
                    modifier = Modifier.fillMaxWidth(0.3f),
                    textAlign = TextAlign.Left
                )
                ExposedDropdownMenuBox(
                    expanded = uiState.isWorkingTypeExpand,
                    onExpandedChange = {
                        if (!uiState.isRegistering) {
                            employeeRegViewModel.onEvent(EmployeeRegEvent.WorkingTypeExpand)
                        }
                    },
                    modifier = Modifier.fillMaxWidth())
                {
                    TextField(
                        value = uiState.workingTypeList.getOrNull(uiState.selectedWorkingType) ?: "",
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = uiState.isWorkingTypeExpand)
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.Transparent,
                            focusedIndicatorColor = Color(0xff424549),
                            unfocusedIndicatorColor = Color(0xff424549),
                            disabledIndicatorColor = Color.Transparent
                        ),
                    )

                    ExposedDropdownMenu(
                        expanded = uiState.isWorkingTypeExpand,
                        onDismissRequest = {
                            employeeRegViewModel.onEvent(EmployeeRegEvent.WorkingTypeExpand)
                        },
                        modifier = Modifier.background(color = Color.White))
                    {
                        uiState.workingTypeList.forEachIndexed { index, s ->
                            DropdownMenuItem(
                                text = { Text(text = s) },
                                onClick = {
                                    employeeRegViewModel.onEvent(
                                        EmployeeRegEvent.SelectWorkingType(index)
                                    )
                                }
                            )
                        }
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Salary Type: ",
                    color = Color.Black,
                    modifier = Modifier.fillMaxWidth(0.3f),
                    textAlign = TextAlign.Left
                )
                ExposedDropdownMenuBox(
                    expanded = uiState.isSalaryTypeExpand,
                    onExpandedChange = {
                        if (!uiState.isRegistering) {
                            employeeRegViewModel.onEvent(EmployeeRegEvent.SalaryTypeExpand)
                        }
                    },
                    modifier = Modifier.fillMaxWidth())
                {
                    TextField(
                        value = uiState.salaryTypeList.getOrNull(uiState.selectedSalaryType) ?: "",
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = uiState.isSalaryTypeExpand)
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.Transparent,
                            focusedIndicatorColor = Color(0xff424549),
                            unfocusedIndicatorColor = Color(0xff424549),
                            disabledIndicatorColor = Color.Transparent
                        ),
                    )

                    ExposedDropdownMenu(
                        expanded = uiState.isSalaryTypeExpand,
                        onDismissRequest = {
                            employeeRegViewModel.onEvent(EmployeeRegEvent.SalaryTypeExpand)
                        },
                        modifier = Modifier.background(color = Color.White))
                    {
                        uiState.salaryTypeList.forEachIndexed { index, s ->
                            DropdownMenuItem(
                                text = { Text(text = s) },
                                onClick = {
                                    employeeRegViewModel.onEvent(
                                        EmployeeRegEvent.SelectSalaryType(index)
                                    )
                                }
                            )
                        }
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Salary: ",
                    color = Color.Black,
                    modifier = Modifier.fillMaxWidth(0.3f),
                    textAlign = TextAlign.Left
                )
                TextField(
                    value = uiState.salary.toString(),
                    onValueChange = {
                        employeeRegViewModel.onEvent(EmployeeRegEvent.SalaryChanged(it))
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent,
                        focusedIndicatorColor = Color(0xff424549),
                        unfocusedIndicatorColor = Color(0xff424549),
                        disabledIndicatorColor = Color.Transparent,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                    ),
                    readOnly = uiState.isRegistering,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
            }

            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Overtime Allowance: ",
                    color = Color.Black,
                    modifier = Modifier.fillMaxWidth(0.3f),
                    textAlign = TextAlign.Left
                )
                TextField(
                    value = uiState.otAllowance.toString(),
                    onValueChange = {
                        employeeRegViewModel.onEvent(EmployeeRegEvent.OtAllowanceChanged(it))
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent,
                        focusedIndicatorColor = Color(0xff424549),
                        unfocusedIndicatorColor = Color(0xff424549),
                        disabledIndicatorColor = Color.Transparent,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                    ),
                    readOnly = uiState.isRegistering,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
            }

            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Allowance Type: ",
                    color = Color.Black,
                    modifier = Modifier.fillMaxWidth(0.3f),
                    textAlign = TextAlign.Left
                )
                ExposedDropdownMenuBox(
                    expanded = uiState.isOtAllowanceTypeExpand,
                    onExpandedChange = {
                        if (!uiState.isRegistering) {
                            employeeRegViewModel.onEvent(EmployeeRegEvent.OtAllowanceTypeExpand)
                        }
                    },
                    modifier = Modifier.fillMaxWidth())
                {
                    TextField(
                        value = uiState.otAllowanceTypeList.getOrNull(uiState.selectedOtAllowanceType) ?: "",
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = uiState.isOtAllowanceTypeExpand)
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.Transparent,
                            focusedIndicatorColor = Color(0xff424549),
                            unfocusedIndicatorColor = Color(0xff424549),
                            disabledIndicatorColor = Color.Transparent
                        ),
                    )

                    ExposedDropdownMenu(
                        expanded = uiState.isOtAllowanceTypeExpand,
                        onDismissRequest = {
                            employeeRegViewModel.onEvent(EmployeeRegEvent.OtAllowanceTypeExpand)
                        },
                        modifier = Modifier.background(color = Color.White))
                    {
                        uiState.otAllowanceTypeList.forEachIndexed { index, s ->
                            DropdownMenuItem(
                                text = { Text(text = s) },
                                onClick = {
                                    employeeRegViewModel.onEvent(
                                        EmployeeRegEvent.SelectOtAllowanceType(index)
                                    )
                                }
                            )
                        }
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Shift Timing: ",
                    color = Color.Black,
                    modifier = Modifier.fillMaxWidth(0.3f),
                    textAlign = TextAlign.Left
                )
                ExposedDropdownMenuBox(
                    expanded = uiState.isTimeslotExpand,
                    onExpandedChange = {
                        if (!uiState.isRegistering) {
                            employeeRegViewModel.onEvent(EmployeeRegEvent.TimeslotExpand)
                        }
                    },
                    modifier = Modifier.fillMaxWidth())
                {
                    TextField(
                        value = uiState.selectedTimeslot?.name ?: "Please select a shifting",
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = uiState.isTimeslotExpand)
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.Transparent,
                            focusedIndicatorColor = Color(0xff424549),
                            unfocusedIndicatorColor = Color(0xff424549),
                            disabledIndicatorColor = Color.Transparent
                        ),
                    )

                    ExposedDropdownMenu(
                        expanded = uiState.isTimeslotExpand,
                        onDismissRequest = {
                            employeeRegViewModel.onEvent(EmployeeRegEvent.TimeslotExpand)
                        },
                        modifier = Modifier.background(color = Color.White))
                    {
                        uiState.timeslots.forEach {
                            DropdownMenuItem(
                                text = { Text(text = it.name) },
                                onClick = {
                                    employeeRegViewModel.onEvent(
                                        EmployeeRegEvent.SelectTimeslot(it)
                                    )
                                }
                            )
                        }
                    }
                }
            }

            Button(
                onClick = {
                    focusManager.clearFocus()
                    employeeRegViewModel.onEvent(EmployeeRegEvent.Apply)
                },
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxWidth(),
            ) {
                if (uiState.isRegistering) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(ButtonDefaults.IconSize),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(text = "Confirm")
                }
            }
        }
    }
}