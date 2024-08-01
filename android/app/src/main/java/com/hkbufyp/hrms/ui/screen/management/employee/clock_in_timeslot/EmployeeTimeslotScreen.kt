package com.hkbufyp.hrms.ui.screen.management.employee.clock_in_timeslot

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.hkbufyp.hrms.domain.model.AttendTimeslotBrief
import com.hkbufyp.hrms.shared.SharedViewModel
import com.hkbufyp.hrms.ui.components.FullScreenLoading
import com.hkbufyp.hrms.ui.components.LoadingIndicator
import com.kizitonwose.calendar.compose.CalendarState
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.core.nextMonth
import com.kizitonwose.calendar.core.previousMonth
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun EmployeeTimeslotScreen(
    snackbarHostState: SnackbarHostState,
    sharedViewModel: SharedViewModel,
    employeeTimeslotViewModel: EmployeeTimeslotViewModel
) {
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(100) } // Adjust as needed
    val endMonth = remember { currentMonth.plusMonths(100) } // Adjust as needed
    val firstDayOfWeek = remember { firstDayOfWeekFromLocale() } // Available from the library
    val daysOfWeek = daysOfWeek()
    val scope = rememberCoroutineScope()
    val today = remember { LocalDate.now() }
    val uiState by employeeTimeslotViewModel.uiState.collectAsState()

    val state = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = firstDayOfWeek
    )

    LaunchedEffect(true) {
        sharedViewModel.setAppBarTitle("Custom Shift Timing")

        employeeTimeslotViewModel.isShowSnacknar.collectLatest {
            if (it) {
                snackbarHostState.showSnackbar(uiState.message)
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    employeeTimeslotViewModel.onEvent(EmployeeTimeslotEvent.AddShift)
                }
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Add")
            }
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(it)
        ) {
            if (uiState.isLoadingData) {
                LoadingIndicator()
            } else {
                FullScreenLoading(show = uiState.isSubmitting)

                RemoveDialog(
                    show = uiState.isShowRemoveDialog,
                    date = uiState.removingShift?.date ?: "",
                    name = uiState.removingShift?.timeslotName ?: "",
                    onConfirm = {
                        employeeTimeslotViewModel.onEvent(EmployeeTimeslotEvent.ConfirmRemove)
                    },
                    onDissmiss = {
                        employeeTimeslotViewModel.onEvent(EmployeeTimeslotEvent.CancelRemove)
                    }
                )

                AddShiftDialog(
                    show = uiState.isShowAddDialog,
                    slots = uiState.timeslots,
                    onConfirm = {
                        employeeTimeslotViewModel.onEvent(EmployeeTimeslotEvent.ConfirmAdd(it))
                    },
                    onCancel = {
                        employeeTimeslotViewModel.onEvent(EmployeeTimeslotEvent.CancelAdd)
                    },
                    snackbarHostState = snackbarHostState
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    state.animateScrollToMonth(state.firstVisibleMonth.yearMonth.previousMonth)
                                }
                            },
                        ) {
                            Icon(imageVector = Icons.Filled.ArrowBackIosNew, contentDescription = "Back")
                        }

                        Text(
                            text = "${state.firstVisibleMonth.yearMonth}",
                            style = MaterialTheme.typography.titleLarge
                        )

                        IconButton(
                            onClick = {
                                scope.launch {
                                    state.animateScrollToMonth(state.firstVisibleMonth.yearMonth.nextMonth)
                                }
                            },
                        ) {
                            Icon(imageVector = Icons.Filled.ArrowForwardIos, contentDescription = "Forward")
                        }
                    }

                    HorizontalCalendar(
                        state = state,
                        dayContent = { calendarDay ->
                            Day(
                                calendarDay,
                                today,
                                onClick = { day ->
                                    employeeTimeslotViewModel.onEvent(EmployeeTimeslotEvent.SelectDate(day.date))
                                },
                                selected = calendarDay.date == uiState.selectedDate && calendarDay.date != today,
                                notice = uiState.allShifts.find { it.date == calendarDay.date.toString() } != null
                            )
                        },
                        monthHeader = {
                            DaysOfWeekTitle(daysOfWeek = daysOfWeek)
                        },
                        modifier = Modifier.padding(top = 16.dp)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(192, 192, 192))
                            .padding(top = 16.dp)
                            .height(35.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "SHIFT ${uiState.selectedDate}",
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }

                    LazyColumn {
                        items(uiState.shifts) { shift ->
                            Row(
                                modifier = Modifier
                                    .padding(12.dp)
                                    .fillMaxWidth()
                            ) {
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(6.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(text = shift.timeslotName)
                                    Text(text = "${shift.start} - ${shift.end}")
                                }

                                IconButton(
                                    onClick = {
                                        employeeTimeslotViewModel.onEvent(EmployeeTimeslotEvent.RemoveShift(shift))
                                    }
                                ) {
                                    Icon(imageVector = Icons.Filled.Delete, contentDescription = "Delete")
                                }
                            }

                            HorizontalDivider()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DaysOfWeekTitle(daysOfWeek: List<DayOfWeek>) {
    Row(modifier = Modifier.fillMaxWidth()) {
        for (dayOfWeek in daysOfWeek) {
            Text(
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ENGLISH),
            )
        }
    }
}

@Composable
fun Day(
    day: CalendarDay,
    today: LocalDate,
    onClick: (CalendarDay) -> Unit,
    selected: Boolean = false,
    notice: Boolean = false
) {
    val borderColor = if (selected && day.date != today) Color.Red else Color.Transparent
    val dotColor = if (notice) Color.Blue else Color.Transparent

    Box(
        modifier = Modifier
            .aspectRatio(1f) // This is important for square sizing!
            .background(
                color = if (day.date == today) Color(102, 255, 255) else Color.White,
                shape = CircleShape
            )
            .padding(4.dp)
            .clickable(
                enabled = day.position == DayPosition.MonthDate,
                onClick = {
                    onClick(day)
                }
            )
            .border(2.dp, borderColor, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day.date.dayOfMonth.toString(),
            color = if (day.position == DayPosition.MonthDate) Color.Black else Color.Gray
        )

        Box(
            modifier = Modifier
                .background(dotColor, shape = CircleShape)
                .size(8.dp)
                .align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun RemoveDialog(
    show: Boolean,
    date: String,
    name: String,
    onConfirm: () -> Unit,
    onDissmiss: () -> Unit
) {
    if (show) {
        AlertDialog(
            onDismissRequest = {
                onDissmiss()
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onConfirm()
                    }
                ) {
                    Text(text = "Confirm")
                }
            },
            icon = {
                Icon(imageVector = Icons.Filled.Delete, contentDescription = "Delete")
            },
            text = {
                Text(text = "Do you confirm that to remove the custom shift of \"$name\" on the date $date?")
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        onDissmiss()
                    }
                ) {
                    Text(text = "Cancel")
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddShiftDialog(
    show: Boolean,
    slots: List<AttendTimeslotBrief>,
    onConfirm: (AttendTimeslotBrief) -> Unit,
    onCancel: () -> Unit,
    snackbarHostState: SnackbarHostState
) {
    var expanded by remember { mutableStateOf(false) }
    var selected by remember { mutableStateOf<AttendTimeslotBrief?>(null) }
    val scope = rememberCoroutineScope()

    if (show) {
        Dialog(
            onDismissRequest = {
                onCancel()
            }
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column (
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Please select a shifting",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(top = 10.dp)
                    )

                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        TextField(
                            value = selected?.name ?: "--------",
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                            },
                            modifier = Modifier.menuAnchor()
                        )

                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            slots.forEach { slot ->
                                DropdownMenuItem(
                                    text = { Text(text = slot.name) },
                                    onClick = {
                                        selected = slot
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(
                            onClick = {
                                onCancel()
                            }
                        ) {
                            Text(text = "Cancel")
                        }

                        TextButton(
                            onClick = {
                                if (selected == null) {
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Please select a shifting")
                                    }
                                } else {
                                    onConfirm(selected!!)
                                }
                            }
                        ) {
                            Text(text = "Confirm")
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun EmployeeTimeslotPreview() {
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(100) } // Adjust as needed
    val endMonth = remember { currentMonth.plusMonths(100) } // Adjust as needed
    val firstDayOfWeek = remember { firstDayOfWeekFromLocale() } // Available from the library
    val daysOfWeek = daysOfWeek()
    val today = remember { LocalDate.now() }

    val state = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = firstDayOfWeek
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Custom Timeslot") },
                navigationIcon = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Add")
            }
        }
    ) {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .background(Color.White)
        ) {

            AddShiftDialog(
                show = true,
                slots = emptyList(),
                onConfirm = {},
                onCancel = {},
                snackbarHostState = SnackbarHostState()
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = { /*TODO*/ },
                    ) {
                        Icon(imageVector = Icons.Filled.ArrowBackIosNew, contentDescription = "Back")
                    }

                    Text(
                        text = "${state.firstVisibleMonth.yearMonth}",
                        style = MaterialTheme.typography.titleLarge
                    )

                    IconButton(
                        onClick = { /*TODO*/ },
                    ) {
                        Icon(imageVector = Icons.Filled.ArrowForwardIos, contentDescription = "Forward")
                    }
                }

                HorizontalCalendar(
                    state = state,
                    dayContent = { Day(it, today, onClick = {}, selected = true) },
                    monthHeader = {
                        DaysOfWeekTitle(daysOfWeek = daysOfWeek)
                    },
                    modifier = Modifier.padding(top = 16.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(192, 192, 192))
                        .padding(top = 16.dp)
                        .height(35.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "SHIFT",
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }

                Row(
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxWidth()
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "Demo day")
                        Text(text = "07:00 AM - 04:00 PM")
                    }

                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Filled.Delete, contentDescription = "Delete")
                    }
                }

                HorizontalDivider()
            }
        }
    }
}
