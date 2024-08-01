package com.hkbufyp.hrms.ui.screen.management.employee.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hkbufyp.hrms.shared.SharedViewModel
import com.hkbufyp.hrms.ui.components.LoadingIndicator
import com.hkbufyp.hrms.ui.screen.management.employee.registration.EmployeeRegEvent
import kotlinx.coroutines.flow.collectLatest

@Composable
fun EmployeeDetailScreen(
    snackbarHostState: SnackbarHostState,
    sharedViewModel: SharedViewModel,
    employeeDetailViewModel: EmployeeDetailViewModel
){
    val uiState by employeeDetailViewModel.uiState.collectAsState()
    val focusManager = LocalFocusManager.current

    LaunchedEffect(true) {
        sharedViewModel.setAppBarTitle("Employee detail")

        employeeDetailViewModel.isShowSnackbar.collectLatest {
            if (it) {
                snackbarHostState.showSnackbar(uiState.message)
            }
        }
    }

    if (uiState.isLoadingData) {
        LoadingIndicator()
    } else if (uiState.permissionDenied) {
      Column(
          modifier = Modifier
              .fillMaxSize()
              .background(Color.White),
          verticalArrangement = Arrangement.Center,
          horizontalAlignment = Alignment.CenterHorizontally
      ) {
          Text(
              text = "Permission denied.",
              style = MaterialTheme.typography.bodyLarge
          )
      }
    } else {
        Scaffold(
            floatingActionButton = {
                if (uiState.isEdit) {
                    Column {
                        FloatingActionButton(
                            onClick = {
                                focusManager.clearFocus()
                                employeeDetailViewModel.onEvent(EmployeeDetailEvent.Save)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Save,
                                contentDescription = "Save"
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        FloatingActionButton(
                            onClick = {
                                employeeDetailViewModel.onEvent(EmployeeDetailEvent.CancelEdit)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Cancel,
                                contentDescription = "Cancel"
                            )
                        }
                    }

                } else {
                    FloatingActionButton(
                        onClick = {
                            employeeDetailViewModel.onEvent(EmployeeDetailEvent.Edit)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "Edit"
                        )
                    }
                }
            }
        ) {
            Box(
                contentAlignment = Alignment.TopCenter,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(it)
            ) {
                if (uiState.isSubmitting) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0.8f, 0.8f, 0.8f, 0.7f))
                    ) {
                        CircularProgressIndicator()
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 24.dp, start = 24.dp, end = 24.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Avatar(
                        text = "WT",
                        color = MaterialTheme.colorScheme.secondary
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp),
                    ){
                        ValueField(
                            label = "Employee ID",
                            value = uiState.editedEmployeeData?.id ?: "",
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ){
                        ValueField(
                            label = "First name",
                            value = uiState.editedEmployeeData?.firstName ?: "",
                            modifier = Modifier.fillMaxWidth(0.5f),
                            editable = uiState.isEdit && !uiState.isSubmitting,
                            onValueChange = { v ->
                                employeeDetailViewModel.onEvent(EmployeeDetailEvent.FirstNameChanged(v))
                            }
                        )

                        ValueField(
                            label = "Last name",
                            value = uiState.editedEmployeeData?.lastName ?: "",
                            editable = uiState.isEdit && !uiState.isSubmitting,
                            onValueChange = { v ->
                                employeeDetailViewModel.onEvent(EmployeeDetailEvent.LastNameChanged(v))
                            }
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ){
                        ValueField(
                            label = "Nick name",
                            value = uiState.editedEmployeeData?.nickName ?: "",
                            modifier = Modifier.fillMaxWidth(0.5f),
                            editable = uiState.isEdit && !uiState.isSubmitting,
                            onValueChange = { v ->
                                employeeDetailViewModel.onEvent(EmployeeDetailEvent.NicknameChanged(v))
                            }
                        )

                        ValueField(
                            label = "Phone",
                            value = uiState.editedEmployeeData?.phone ?: "",
                            editable = uiState.isEdit && !uiState.isSubmitting,
                            onValueChange = { v ->
                                employeeDetailViewModel.onEvent(EmployeeDetailEvent.PhoneChanged(v))
                            }
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp),
                    ){
                        DropdownField(
                            label = "Department",
                            value = uiState.editedEmployeeData?.department?.title ?: "Please select a department",
                            expanded = uiState.isDepartmentExpand,
                            dataMap = uiState.departmentMap,
                            editable = uiState.isEdit,
                            onExpandedChange = {
                                employeeDetailViewModel.onEvent(EmployeeDetailEvent.DepartmentExpand)
                            },
                            onValueChange = { department ->
                                employeeDetailViewModel.onEvent(EmployeeDetailEvent.SelectDepartment(department))
                            }
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp),
                    ){
                        DropdownField(
                            label = "Position",
                            value = uiState.editedEmployeeData?.position?.name ?: "Please select a position",
                            expanded = uiState.isRoleExpand,
                            dataMap = uiState.roleMap,
                            editable = uiState.isEdit,
                            onExpandedChange = {
                                employeeDetailViewModel.onEvent(EmployeeDetailEvent.RoleExpand)
                            },
                            onValueChange = { position ->
                                employeeDetailViewModel.onEvent(EmployeeDetailEvent.SelectRole(position))
                            }
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp),
                    ){
                        DropdownField(
                            label = "Working Type",
                            value = uiState.workingTypeMap.entries.find { map ->
                                map.value == (uiState.editedEmployeeData?.workingType ?: 0)
                            }?.key ?: "",
                            expanded = uiState.isWorkingTypeExpand,
                            dataMap = uiState.workingTypeMap,
                            editable = uiState.isEdit,
                            onExpandedChange = {
                                employeeDetailViewModel.onEvent(EmployeeDetailEvent.WorkingTypeExpand)
                            },
                            onValueChange = { t ->
                                employeeDetailViewModel.onEvent(EmployeeDetailEvent.SelectWorkingType(t ?: 0))
                            }
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp),
                    ){
                        DropdownField(
                            label = "Salary Type",
                            value = uiState.salaryTypeMap.entries.find { map ->
                                map.value == (uiState.editedEmployeeData?.salaryType ?: 0)
                            }?.key ?: "",
                            expanded = uiState.isSalaryTypeExpand,
                            dataMap = uiState.salaryTypeMap,
                            editable = uiState.isEdit,
                            onExpandedChange = {
                                employeeDetailViewModel.onEvent(EmployeeDetailEvent.SalaryTypeExpand)
                            },
                            onValueChange = { t ->
                                employeeDetailViewModel.onEvent(EmployeeDetailEvent.SelectSalaryType(t ?: 0))
                            }
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ){
                        ValueField(
                            label = "Salary",
                            value = uiState.editedEmployeeData?.salary?.toString() ?: "",
                            modifier = Modifier.fillMaxWidth(),
                            editable = uiState.isEdit && !uiState.isSubmitting,
                            onValueChange = { v ->
                                employeeDetailViewModel.onEvent(EmployeeDetailEvent.SalaryChanged(v))
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ){
                        ValueField(
                            label = "Overtime allowance",
                            value = uiState.editedEmployeeData?.otAllowance?.toString() ?: "",
                            modifier = Modifier.fillMaxWidth(0.5f),
                            editable = uiState.isEdit && !uiState.isSubmitting,
                            onValueChange = { v ->
                                employeeDetailViewModel.onEvent(EmployeeDetailEvent.OtAllowanceChanged(v))
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )

                        DropdownField(
                            label = "Allowance Type",
                            value = uiState.otAllowanceType.entries.find { map ->
                                map.value == (uiState.editedEmployeeData?.otAllowanceType ?: 0)
                            }?.key ?: "",
                            expanded = uiState.isOtAllowanceTypeExpand,
                            dataMap = uiState.otAllowanceType,
                            editable = uiState.isEdit,
                            onExpandedChange = {
                                employeeDetailViewModel.onEvent(EmployeeDetailEvent.OtAllowanceTypeExpand)
                            },
                            onValueChange = { v ->
                                employeeDetailViewModel.onEvent(EmployeeDetailEvent.SelectOtAllowanceType(v ?: 0))
                            }
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp),
                    ){
                        DropdownField(
                            label = "Shift Timing",
                            value = uiState.editedEmployeeData?.timeslotName ?: "------",
                            expanded = uiState.isTimeslotExpand,
                            dataMap = uiState.timeslotMap,
                            editable = uiState.isEdit,
                            onExpandedChange = {
                                employeeDetailViewModel.onEvent(EmployeeDetailEvent.TimeslotExpand)
                            },
                            onValueChange = { slot ->
                                employeeDetailViewModel.onEvent(EmployeeDetailEvent.SelectTimeslot(slot))
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Avatar(
    text: String,
    color: Color
){
    Surface (
        shape = CircleShape,
        modifier = Modifier.defaultMinSize(minWidth = 120.dp, minHeight = 120.dp),
        color = color
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = text,
                style = MaterialTheme.typography.headlineLarge
            )
        }
    }
}

@Composable
fun ValueField(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    editable: Boolean = false,
    onValueChange: (str: String) -> Unit = {},
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
){
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(text = label)
        OutlinedTextField(
            value = value,
            onValueChange = { onValueChange(it) },
            modifier = Modifier.fillMaxWidth(),
            readOnly = !editable,
            keyboardOptions = keyboardOptions
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> DropdownField(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    editable: Boolean = false,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit = {},
    dataMap: Map<String, T>,
    onValueChange: (T?) -> Unit = {}
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(text = label)

        if (editable) {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { onExpandedChange(it) },
                modifier = Modifier.fillMaxWidth())
            {
                TextField(
                    value = value,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
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
                    expanded = expanded,
                    onDismissRequest = { onExpandedChange(false) },
                    modifier = Modifier.background(color = Color.White),
                    content = {
                        dataMap.keys.forEach {
                            DropdownMenuItem(
                                text = { Text(text = it) },
                                onClick = {
                                    onValueChange(dataMap[it])
                                }
                            )
                        }
                    }
                )
            }
        } else {
            OutlinedTextField(
                value = value,
                onValueChange = {  },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
            )
        }
    }
}

@Preview
@Composable
fun EmployeeDetailPreview() {
}