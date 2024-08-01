package com.hkbufyp.hrms.ui.screen.user.leave.apply

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.hkbufyp.hrms.ui.components.GenericDatePickerDialog
import com.hkbufyp.hrms.ui.components.LoadingIndicator
import com.hkbufyp.hrms.shared.SharedViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaveApplyScreen(
    snackbarHostState: SnackbarHostState,
    sharedViewModel: SharedViewModel,
    leaveApplyViewModel: LeaveApplyViewModel,
    onApplySucceed: (applicationId: Int) -> Unit
) {
    val uiState by leaveApplyViewModel.uiState.collectAsState()
    val focusManager = LocalFocusManager.current

    LaunchedEffect(true) {
        sharedViewModel.setAppBarTitle("Leave Applications")

        launch {
            leaveApplyViewModel.isShowSnackbar.collectLatest {
                if (it) {
                    snackbarHostState.showSnackbar(uiState.message)
                }
            }
        }

        launch {
            leaveApplyViewModel.applicationId.collectLatest {
                if (it > 0) {
                    onApplySucceed(it)
                }
            }
        }
    }

    if (uiState.isLoadingData) {
        LoadingIndicator()
    } else {

        if (uiState.isShowDatePickerForStartDate) {
            GenericDatePickerDialog(
                onConfirm = {
                    leaveApplyViewModel.onEvent(LeaveApplyEvent.SelectedStartDate(it))
                },
                onDismiss = {
                    leaveApplyViewModel.onEvent(LeaveApplyEvent.DismissDatePickerForStartDate)
                }
            )
        }

        if (uiState.isShowDatePickerForEndDate) {
            GenericDatePickerDialog(
                onConfirm = {
                    leaveApplyViewModel.onEvent(LeaveApplyEvent.SelectedEndDate(it))
                },
                onDismiss = {
                    leaveApplyViewModel.onEvent(LeaveApplyEvent.DismissDatePickerForEndDate)
                }
            )
        }

        Box(modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
        ) {
            Column(
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
                        text = "Start Date: ",
                        color = Color.Black,
                        modifier = Modifier.fillMaxWidth(0.3f),
                        textAlign = TextAlign.Left
                    )
                    TextField(
                        value = uiState.startDate,
                        onValueChange = {  },
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.Transparent,
                            focusedIndicatorColor = Color(0xff424549),
                            unfocusedIndicatorColor = Color(0xff424549),
                            disabledIndicatorColor = Color.Transparent,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                        ),
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    leaveApplyViewModel.onEvent(LeaveApplyEvent.ShowDatePickerForStartDate)
                                }
                            ) {
                                Icon(imageVector = Icons.Filled.DateRange, contentDescription = "Select a date")
                            }
                        },
                        placeholder = {
                            Text(text = "YYYY-MM-DD")
                        },
                        readOnly = true
                    )
                }

                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "End Date: ",
                        color = Color.Black,
                        modifier = Modifier.fillMaxWidth(0.3f),
                        textAlign = TextAlign.Left
                    )
                    TextField(
                        value = uiState.endDate,
                        onValueChange = {  },
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.Transparent,
                            focusedIndicatorColor = Color(0xff424549),
                            unfocusedIndicatorColor = Color(0xff424549),
                            disabledIndicatorColor = Color.Transparent,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                        ),
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    leaveApplyViewModel.onEvent(LeaveApplyEvent.ShowDatePickerForEndDate)
                                }
                            ) {
                                Icon(imageVector = Icons.Filled.DateRange, contentDescription = "Select a date")
                            }
                        },
                        placeholder = {
                            Text(text = "YYYY-MM-DD")
                        },
                        readOnly = true
                    )
                }

                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Days: ",
                        color = Color.Black,
                        modifier = Modifier.fillMaxWidth(0.3f),
                        textAlign = TextAlign.Left
                    )
                    TextField(
                        value = uiState.applyDays,
                        onValueChange = {
                            leaveApplyViewModel.onEvent(LeaveApplyEvent.ApplyDaysChanged(it))
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.Transparent,
                            focusedIndicatorColor = Color(0xff424549),
                            unfocusedIndicatorColor = Color(0xff424549),
                            disabledIndicatorColor = Color.Transparent,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )
                }

                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Leave Type: ",
                        color = Color.Black,
                        modifier = Modifier.fillMaxWidth(0.3f),
                        textAlign = TextAlign.Left
                    )
                    ExposedDropdownMenuBox(
                        expanded = uiState.isLeaveTypeExpand,
                        onExpandedChange = {
                            leaveApplyViewModel.onEvent(LeaveApplyEvent.LeaveTypeExpandChanged)
                        },
                        modifier = Modifier.fillMaxWidth())
                    {
                        TextField(
                            value = uiState.selectedLeaveType?.name ?: "Please select a leave type",
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = uiState.isLeaveTypeExpand)
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
                            expanded = uiState.isLeaveTypeExpand,
                            onDismissRequest = {
                                leaveApplyViewModel.onEvent(LeaveApplyEvent.LeaveTypeExpandChanged)
                            },
                            modifier = Modifier.background(color = Color.White))
                        {
                            uiState.leaveTypes.forEach {
                                DropdownMenuItem(
                                    text = { Text(text = it.name) },
                                    onClick = {
                                        leaveApplyViewModel.onEvent(LeaveApplyEvent.SelectedLeaveType(it))
                                    }
                                )
                            }
                        }
                    }
                }

                AnimatedVisibility(visible = uiState.selectedLeaveType?.isNeedReasonForApply ?: false) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Reason: ",
                            color = Color.Black,
                            modifier = Modifier.fillMaxWidth(0.3f),
                            textAlign = TextAlign.Left
                        )
                        TextField(
                            value = uiState.reason,
                            onValueChange = {
                                leaveApplyViewModel.onEvent(LeaveApplyEvent.ReasonChanged(it))
                            },
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = Color.Transparent,
                                focusedIndicatorColor = Color(0xff424549),
                                unfocusedIndicatorColor = Color(0xff424549),
                                disabledIndicatorColor = Color.Transparent,
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black,
                            )
                        )
                    }
                }

                Button(
                    onClick = {
                        focusManager.clearFocus()
                        leaveApplyViewModel.onEvent(LeaveApplyEvent.Apply)
                    },
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .fillMaxWidth()
                ) {
                    if (uiState.isApplying) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(ButtonDefaults.IconSize),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(text = "Confirm Apply")
                    }
                }
            }
        }

    }
}