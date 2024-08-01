package com.hkbufyp.hrms.ui.screen.management.attendance.timeslot.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.hkbufyp.hrms.domain.model.AttendWorkingTimeslot
import com.hkbufyp.hrms.domain.model.ValidClockInRange
import com.hkbufyp.hrms.shared.SharedViewModel
import com.hkbufyp.hrms.ui.components.FullScreenLoading
import com.hkbufyp.hrms.ui.components.GenericTimePickerDialog
import com.hkbufyp.hrms.ui.components.LoadingIndicator
import com.hkbufyp.hrms.ui.components.ToggleField
import com.hkbufyp.hrms.util.toCalendar12
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendTimeslotDetailScreen(
    snackbarHostState: SnackbarHostState,
    sharedViewModel: SharedViewModel,
    attendTimeslotDetailViewModel: AttendTimeslotDetailViewModel,
    onSubmitSucceeded: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val uiState by attendTimeslotDetailViewModel.uiState.collectAsState()

    LaunchedEffect(true) {
        sharedViewModel.isShowAppBar(false)

        launch {
            attendTimeslotDetailViewModel.isShowSnackbar.collectLatest {
                if (it) {
                    snackbarHostState.showSnackbar(uiState.message)
                }
            }
        }

        launch {
            attendTimeslotDetailViewModel.isSubmitSucceeded.collectLatest {
                if (it) {
                    onSubmitSucceeded()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = if (uiState.isCreate) "Create Timeslot" else "Edit Timeslot"
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onNavigateBack() }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            attendTimeslotDetailViewModel.onEvent(
                                AttendTimeslotDetailEvent.Submit
                            )
                        }
                    ) {
                        Icon(imageVector = Icons.Filled.Send, contentDescription = "Submit")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        },
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .verticalScroll(rememberScrollState())
                .padding(it)
        ) {
            if (uiState.showSelectRangeDialog) {
                SelectRangeDialog(
                    defaultValue = uiState.attendTimeslot?.validClockInRange ?: ValidClockInRange.Automatic,
                    onConfirm = {
                        attendTimeslotDetailViewModel.onEvent(
                            AttendTimeslotDetailEvent.SetValidRangeMode(it)
                        )
                    },
                    onDismiss = {
                        attendTimeslotDetailViewModel.onEvent(
                            AttendTimeslotDetailEvent.ShowSelectRangeDialog(false)
                        )
                    }
                )
            }

            if (uiState.isSubmitting) {
                FullScreenLoading()
            }

            if (uiState.isLoadingData) {
                Column (
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    LoadingIndicator()
                }
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AttendNameSection(
                        value = uiState.attendTimeslot?.name ?: "",
                        onValueChanged = {
                            attendTimeslotDetailViewModel.onEvent(
                                AttendTimeslotDetailEvent.SetName(it)
                            )
                        },
                        modifier = Modifier.padding(20.dp)
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 14.dp))

                    AttendTimeSection(
                        timeList = uiState.attendTimeslot?.slots ?: emptyList(),
                        onTimePicked = { isStart, index, timeStr ->
                            if (isStart) {
                                attendTimeslotDetailViewModel.onEvent(
                                    AttendTimeslotDetailEvent.SetStartTime(index, timeStr)
                                )
                            } else {
                                attendTimeslotDetailViewModel.onEvent(
                                    AttendTimeslotDetailEvent.SetEndTime(index, timeStr)
                                )
                            }
                        },
                        onSlotAdd = {
                            attendTimeslotDetailViewModel.onEvent(
                                AttendTimeslotDetailEvent.AddSlot
                            )
                        },
                        onSlotRemove = { index ->
                            attendTimeslotDetailViewModel.onEvent(
                                AttendTimeslotDetailEvent.RemoveSlot(index)
                            )
                        }
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 14.dp))

                    AttendTimeSettingSection(
                        uiState = uiState,
                        attendTimeslotDetailViewModel = attendTimeslotDetailViewModel
                    )
                }
            }
        }
    }
}

@Composable
fun AttendNameSection(
    value: String,
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = { onValueChanged(it) },
        modifier = modifier.fillMaxWidth(),
        placeholder = {
            Text(text = "Name")
        },
        label = {
            Text(text = "Name")
        },
        singleLine = true
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendTimeSection(
    timeList: List<AttendWorkingTimeslot>,
    onTimePicked: (Boolean, Int, String) -> Unit,
    onSlotAdd: () -> Unit,
    onSlotRemove: (Int) -> Unit
) {
    var isShowTimePickerForStart by remember { mutableStateOf(false) }
    var isShowTimePickerForEnd by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableStateOf(-1) }

    if (isShowTimePickerForStart) {
        GenericTimePickerDialog(
            onConfirm = {
                onTimePicked(true, selectedIndex, it)
            },
            onDismiss = {
                isShowTimePickerForStart = false
            },
            initHour = timeList[selectedIndex].start.toCalendar12().get(Calendar.HOUR_OF_DAY),
            initMinute = timeList[selectedIndex].start.toCalendar12().get(Calendar.MINUTE)
        )
    }

    if (isShowTimePickerForEnd) {
        GenericTimePickerDialog(
            onConfirm = {
                onTimePicked(false, selectedIndex, it)
            },
            onDismiss = {
                isShowTimePickerForEnd = false
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Text(
            text = "Working timeslot",
            style = MaterialTheme.typography.bodySmall,
            color = Color.DarkGray
        )

        Column(
            modifier = Modifier.padding(top = 12.dp, end = 8.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                timeList.forEachIndexed { index, attendWorkingTimeslot ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Timeslot ${index + 1}",
                            style = MaterialTheme.typography.bodySmall
                        )

                        BasicTextField(
                            value = attendWorkingTimeslot.start,
                            onValueChange = { },
                            modifier = Modifier
                                .height(35.dp)
                                .fillMaxWidth(0.43f),
                            readOnly = true,
                            singleLine = true,
                            interactionSource = remember { MutableInteractionSource() }
                                .also { interactionSource ->
                                    LaunchedEffect(interactionSource) {
                                        interactionSource.interactions.collect { interaction ->
                                            if (interaction is PressInteraction.Release) {
                                                selectedIndex = index
                                                isShowTimePickerForStart = true
                                            }
                                        }
                                    }
                                },
                        ) {
                            OutlinedTextFieldDefaults.DecorationBox(
                                value = attendWorkingTimeslot.start,
                                innerTextField = it,
                                enabled = true,
                                singleLine = true,
                                visualTransformation = VisualTransformation.None,
                                interactionSource = remember { MutableInteractionSource() }
                                    .also { interactionSource ->
                                        LaunchedEffect(interactionSource) {
                                            interactionSource.interactions.collect { interaction ->
                                                if (interaction is PressInteraction.Release) {
                                                    selectedIndex = index
                                                    isShowTimePickerForStart = true
                                                }
                                            }
                                        }
                                    },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = false)
                                },
                                contentPadding = OutlinedTextFieldDefaults.contentPadding(
                                    top = 0.dp,
                                    bottom = 0.dp
                                ),
                            )
                        }

                        BasicTextField(
                            value = attendWorkingTimeslot.end,
                            onValueChange = { },
                            modifier = Modifier
                                .height(35.dp)
                                .fillMaxWidth(0.8f),
                            readOnly = true,
                            singleLine = true,
                            interactionSource = remember { MutableInteractionSource() }
                                .also { interactionSource ->
                                    LaunchedEffect(interactionSource) {
                                        interactionSource.interactions.collect { interaction ->
                                            if (interaction is PressInteraction.Release) {
                                                selectedIndex = index
                                                isShowTimePickerForEnd = true
                                            }
                                        }
                                    }
                                }
                        ) {
                            OutlinedTextFieldDefaults.DecorationBox(
                                value = attendWorkingTimeslot.end,
                                innerTextField = it,
                                enabled = true,
                                singleLine = true,
                                visualTransformation = VisualTransformation.None,
                                interactionSource = remember { MutableInteractionSource() }
                                    .also { interactionSource ->
                                        LaunchedEffect(interactionSource) {
                                            interactionSource.interactions.collect { interaction ->
                                                if (interaction is PressInteraction.Release) {
                                                    selectedIndex = index
                                                    isShowTimePickerForEnd = true
                                                }
                                            }
                                        }
                                    },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = false)
                                },
                                contentPadding = OutlinedTextFieldDefaults.contentPadding(
                                    top = 0.dp,
                                    bottom = 0.dp
                                ),
                            )
                        }

                        if (index > 0) {
                            IconButton(
                                onClick = { onSlotRemove(index) }
                            ) {
                                Icon(imageVector = Icons.Filled.Remove, contentDescription = "Remove")
                            }
                        }
                    }
                }
            }

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                IconButton(
                    onClick = { onSlotAdd() }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add",
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun AttendTimeSettingSection(
    uiState: AttendTimeslotDetailUiState,
    attendTimeslotDetailViewModel: AttendTimeslotDetailViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Text(
            text = "Setting",
            style = MaterialTheme.typography.bodySmall,
            color = Color.DarkGray
        )

        Column(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ToggleField(
                desc = "Require Clock-in",
                checked = uiState.attendTimeslot?.requireClockIn ?: false,
                onCheckedChange = {
                    attendTimeslotDetailViewModel.onEvent(
                        AttendTimeslotDetailEvent.SetRequireClockIn(it)
                    )
                }
            )

            ToggleField(
                desc = "Require Clock-out",
                checked = uiState.attendTimeslot?.requireClockOut ?: false,
                onCheckedChange = {
                    attendTimeslotDetailViewModel.onEvent(
                        AttendTimeslotDetailEvent.SetRequireClockOut(it)
                    )
                }
            )

            ToggleField(
                desc = "Count Absent",
                checked = uiState.attendTimeslot?.countAbsent ?: false,
                onCheckedChange = {
                    attendTimeslotDetailViewModel.onEvent(
                        AttendTimeslotDetailEvent.SetCountAbsent(it)
                    )
                }
            )

            ToggleField(
                desc = "Count Late",
                checked = uiState.attendTimeslot?.countLate ?: false,
                onCheckedChange = {
                    attendTimeslotDetailViewModel.onEvent(
                        AttendTimeslotDetailEvent.SetCountLate(it)
                    )
                }
            )

            ToggleField(
                desc = "Count Leave Early",
                checked = uiState.attendTimeslot?.countLeaveEarly ?: false,
                onCheckedChange = {
                    attendTimeslotDetailViewModel.onEvent(
                        AttendTimeslotDetailEvent.SetCountLeaveEarly(it)
                    )
                }
            )

            ToggleField(
                desc = "Count Overtime",
                checked = uiState.attendTimeslot?.countOvertime ?: false,
                onCheckedChange = {
                    attendTimeslotDetailViewModel.onEvent(
                        AttendTimeslotDetailEvent.SetCountOvertime(it)
                    )
                }
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total Working Hours",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1.0f)
                )

                OutlinedTextField(
                    value = uiState.attendTimeslot?.totalWorkingHours?.toString() ?: "",
                    onValueChange = {
                        attendTimeslotDetailViewModel.onEvent(
                            AttendTimeslotDetailEvent.SetTotalWorkingHours(it)
                        )
                    },
                    placeholder = {
                        Text(text = "Hours")
                    },
                    modifier = Modifier.fillMaxWidth(0.4f),
                    label = {
                        Text(text = "Hours")
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal
                    ),
                    singleLine = true
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Lateness grace",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1.0f)
                )

                OutlinedTextField(
                    value = uiState.attendTimeslot?.latenessGrace?.toString() ?: "",
                    onValueChange = {
                        attendTimeslotDetailViewModel.onEvent(
                            AttendTimeslotDetailEvent.SetLatenessGrace(it)
                        )
                    },
                    placeholder = {
                        Text(text = "Minutes")
                    },
                    modifier = Modifier.fillMaxWidth(0.4f),
                    label = {
                        Text(text = "Minutes")
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    singleLine = true
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Early leave grace",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1.0f)
                )

                OutlinedTextField(
                    value = uiState.attendTimeslot?.earlyLeaveGrace?.toString() ?: "",
                    onValueChange = {
                        attendTimeslotDetailViewModel.onEvent(
                            AttendTimeslotDetailEvent.SetEarlyLeaveGrace(it)
                        )
                    },
                    placeholder = {
                        Text(text = "Minutes")
                    },
                    modifier = Modifier.fillMaxWidth(0.4f),
                    label = {
                        Text(text = "Minutes")
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    singleLine = true
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Overtime Start",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1.0f)
                )

                OutlinedTextField(
                    value = uiState.attendTimeslot?.overtimeStart?.toString() ?: "",
                    onValueChange = {
                        attendTimeslotDetailViewModel.onEvent(
                            AttendTimeslotDetailEvent.SetOvertimeStart(it)
                        )
                    },
                    placeholder = {
                        Text(text = "Minutes")
                    },
                    modifier = Modifier.fillMaxWidth(0.4f),
                    label = {
                        Text(text = "Minutes")
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    singleLine = true
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Valid clock-in/out range",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1.0f)
                )

                Button(
                    onClick = {
                        attendTimeslotDetailViewModel.onEvent(
                            AttendTimeslotDetailEvent.ShowSelectRangeDialog(true)
                        )
                    }
                ) {
                    Text(text = uiState.attendTimeslot?.validClockInRange?.toString() ?: ValidClockInRange.Automatic.toString())
                }
            }

            AnimatedVisibility(
                visible = uiState.attendTimeslot?.validClockInRange == ValidClockInRange.Manual
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Before work",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.weight(1.0f)
                        )

                        OutlinedTextField(
                            value = uiState.attendTimeslot?.rangeOfBeforeWork?.toString() ?: "",
                            onValueChange = {
                                attendTimeslotDetailViewModel.onEvent(
                                    AttendTimeslotDetailEvent.SetRangeOfBeforeWork(it)
                                )
                            },
                            placeholder = {
                                Text(text = "Minutes")
                            },
                            modifier = Modifier.fillMaxWidth(0.4f),
                            label = {
                                Text(text = "Minutes")
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number
                            ),
                            singleLine = true
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "After work",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.weight(1.0f)
                        )

                        OutlinedTextField(
                            value = uiState.attendTimeslot?.rangeOfAfterWork?.toString() ?: "",
                            onValueChange = {
                                attendTimeslotDetailViewModel.onEvent(
                                    AttendTimeslotDetailEvent.SetRangeOfAfterWork(it)
                                )
                            },
                            placeholder = {
                                Text(text = "Minutes")
                            },
                            modifier = Modifier.fillMaxWidth(0.4f),
                            label = {
                                Text(text = "Minutes")
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number
                            ),
                            singleLine = true
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Before leave",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.weight(1.0f)
                        )

                        OutlinedTextField(
                            value = uiState.attendTimeslot?.rangeOfBeforeLeave?.toString() ?: "",
                            onValueChange = {
                                attendTimeslotDetailViewModel.onEvent(
                                    AttendTimeslotDetailEvent.SetRangeOfBeforeLeave(it)
                                )
                            },
                            placeholder = {
                                Text(text = "Minutes")
                            },
                            modifier = Modifier.fillMaxWidth(0.4f),
                            label = {
                                Text(text = "Minutes")
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number
                            ),
                            singleLine = true
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "After leave",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.weight(1.0f)
                        )

                        OutlinedTextField(
                            value = uiState.attendTimeslot?.rangeOfAfterLeave?.toString() ?: "",
                            onValueChange = {
                                attendTimeslotDetailViewModel.onEvent(
                                    AttendTimeslotDetailEvent.SetRangeOfAfterLeave(it)
                                )
                            },
                            placeholder = {
                                Text(text = "Minutes")
                            },
                            modifier = Modifier.fillMaxWidth(0.4f),
                            label = {
                                Text(text = "Minutes")
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number
                            ),
                            singleLine = true
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SelectRangeDialog(
    defaultValue: ValidClockInRange,
    onConfirm: (ValidClockInRange) -> Unit,
    onDismiss: () -> Unit
) {
    var selected by remember { mutableStateOf(defaultValue) }

    Dialog(
        onDismissRequest = { onDismiss() }
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                ValidClockInRange.values().forEach {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable { selected = it }
                            .padding(bottom = 10.dp)
                    ) {
                        Text(
                            text = it.toString(),
                            modifier = Modifier.weight(1.0f)
                        )

                        RadioButton(
                            selected = selected == it,
                            onClick = null
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = {
                            onConfirm(selected)
                            onDismiss()
                        },
                        modifier = Modifier.padding(top = 10.dp)
                    ) {
                        Text(text = "Confirm")
                    }
                }
            }
        }
    }
}