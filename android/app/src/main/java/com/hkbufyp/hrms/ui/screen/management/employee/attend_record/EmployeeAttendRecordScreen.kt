package com.hkbufyp.hrms.ui.screen.management.employee.attend_record

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hkbufyp.hrms.shared.SharedViewModel
import com.hkbufyp.hrms.ui.components.LoadingIndicator
import com.seanproctor.datatable.DataColumn
import com.seanproctor.datatable.TableColumnWidth
import com.seanproctor.datatable.material3.PaginatedDataTable
import com.seanproctor.datatable.paging.rememberPaginatedDataTableState
import kotlinx.coroutines.flow.collectLatest

@Composable
fun EmployeeAttendRecordScreen(
    snackbarHostState: SnackbarHostState,
    sharedViewModel: SharedViewModel,
    employeeAttendRecordViewModel: EmployeeAttendRecordViewModel
) {

    val uiState by employeeAttendRecordViewModel.uiState.collectAsState()

    LaunchedEffect(true) {
        sharedViewModel.setAppBarTitle("Attendance Record")

        employeeAttendRecordViewModel.isShowSnackbar.collectLatest {
            if (it) {
                snackbarHostState.showSnackbar(uiState.message)
            }
        }
    }

    Box (
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column (
                    horizontalAlignment = Alignment.Start
                ) {
                    IconButton(
                        onClick = {
                            employeeAttendRecordViewModel.onEvent(EmployeeAttendRecordEvent.Previous)
                        }
                    ) {
                        Icon(imageVector = Icons.Filled.ArrowBackIosNew, contentDescription = "Left")
                    }
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(top = 14.dp)
                ) {
                    Text(
                        text = "${uiState.startDate} - ${uiState.endDate}",
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                Column (
                    horizontalAlignment = Alignment.End
                ) {
                    IconButton(
                        onClick = {
                            employeeAttendRecordViewModel.onEvent(EmployeeAttendRecordEvent.Next)
                        }
                    ) {
                        Icon(imageVector = Icons.Filled.ArrowForwardIos, contentDescription = "Left")
                    }
                }
            }

            Spacer(modifier = Modifier.height(5.dp))

            if (uiState.isLoadingData) {
                LoadingIndicator()
            } else {
                if (uiState.user == null || uiState.attendRecord == null) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = "Unable to fetch data")
                    }
                } else {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(290.dp),
                        shape = RoundedCornerShape(10.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Row (
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Employee Information",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }

                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "Name: ${uiState.user!!.lastName} ${uiState.user!!.firstName}",
                                    modifier = Modifier.fillMaxWidth(0.5f)
                                )

                                Text(
                                    text = "Basic Salary: $${uiState.user!!.salary}",
                                )
                            }

                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                            Text(
                                text = "Total",
                                style = MaterialTheme.typography.titleSmall
                            )

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp)
                            ) {
                                Text(
                                    text = "Working days: ${uiState.attendRecord!!.totalAttendDays}",
                                    modifier = Modifier.fillMaxWidth(0.5f)
                                )

                                Text(
                                    text = "Working hours: %.2f".format(uiState.attendRecord!!.totalWorkingHours),
                                )
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 4.dp),
                            ) {
                                Text(
                                    text = "Late: %.2f".format(uiState.attendRecord!!.totalLateHours),
                                    modifier = Modifier.fillMaxWidth(0.3f)
                                )

                                Text(
                                    text = "OT: %.2f".format(uiState.attendRecord!!.totalOvertimeHours),
                                    modifier = Modifier.fillMaxWidth(0.29f)
                                )

                                Text(
                                    text = "Early Leave: %.2f".format(uiState.attendRecord!!.totalEarlyLeaveHours),
                                )
                            }

                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                            Text(
                                text = "Balance",
                                style = MaterialTheme.typography.titleSmall
                            )

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Late: ${uiState.lateBalance}",
                                )

                                Text(
                                    text = "OT: ${uiState.overtimeBalance}",
                                )

                                Text(
                                    text = "Early Leave: ${uiState.earlyLeaveBalance}",
                                )
                            }

                            Row (
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 10.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "Salary: ${uiState.salary}",
                                    style = MaterialTheme.typography.titleSmall
                                )
                            }

                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))

                    HorizontalDivider()

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Attendance Record",
                        color = Color.DarkGray
                    )

                    PaginatedDataTable(
                        columns = listOf(
                            DataColumn(
                                width = TableColumnWidth.MaxIntrinsic
                            ) {
                                Text(text = "Date")
                            },
                            DataColumn(
                                width = TableColumnWidth.MaxIntrinsic
                            ) {
                                Text(text = "Shift")
                            },
                            DataColumn(
                                width = TableColumnWidth.MaxIntrinsic
                            ) {
                                Text(text = "IN")
                            },
                            DataColumn(
                                width = TableColumnWidth.MaxIntrinsic
                            ) {
                                Text(text = "OUT")
                            },
                            DataColumn(
                                width = TableColumnWidth.MaxIntrinsic
                            ) {
                                Text(text = "Status")
                            },
                        ),
                        state = rememberPaginatedDataTableState(initialPageSize = 5),
                        horizontalPadding = 8.dp
                    ) {
                        uiState.attendRecord!!.brief.forEach { log ->
                            if(log.inTime.isNotEmpty() || log.outTime.isNotEmpty()) {
                                row {
                                    cell {
                                        Text(text = log.date, style = MaterialTheme.typography.bodySmall)
                                    }
                                    cell {
                                        Text(text = log.timeslotName, style = MaterialTheme.typography.bodySmall)
                                    }
                                    cell {
                                        Text(text = log.inTime, style = MaterialTheme.typography.bodySmall)
                                    }
                                    cell {
                                        Text(text = log.outTime, style = MaterialTheme.typography.bodySmall)
                                    }
                                    cell {
                                        Text(text = "Attended", style = MaterialTheme.typography.bodySmall)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun EmployeeAttendRecordPreview() {
    Scaffold (
        topBar = {
            CenterAlignedTopAppBar(title = { Text(text = "Attendance Record") })
        }
    ) {
        Box (
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(it)
        ) {
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column (
                        horizontalAlignment = Alignment.Start
                    ) {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(imageVector = Icons.Filled.ArrowBackIosNew, contentDescription = "Left")
                        }
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(top = 14.dp)
                    ) {
                        Text(
                            text = "2024-04-01 - 2024-04-30",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    Column (
                        horizontalAlignment = Alignment.End
                    ) {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(imageVector = Icons.Filled.ArrowForwardIos, contentDescription = "Left")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(290.dp),
                    shape = RoundedCornerShape(10.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Employee Information",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Name: Wong Mei Yan",
                                modifier = Modifier.fillMaxWidth(0.5f)
                            )

                            Text(
                                text = "Basic Salary: $18000",
                            )
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                        Text(
                            text = "Total",
                            style = MaterialTheme.typography.titleSmall
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp)
                        ) {
                            Text(
                                text = "Working days: 25",
                                modifier = Modifier.fillMaxWidth(0.5f)
                            )

                            Text(
                                text = "Working hours: 225",
                            )
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp),
                        ) {
                            Text(
                                text = "Late: 2.31",
                                modifier = Modifier.fillMaxWidth(0.3f)
                            )

                            Text(
                                text = "OT: 1.5",
                                modifier = Modifier.fillMaxWidth(0.29f)
                            )

                            Text(
                                text = "Early Leave: 0.0",
                            )
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                        Text(
                            text = "Balance",
                            style = MaterialTheme.typography.titleSmall
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Late: -173.2",
                            )

                            Text(
                                text = "OT: +112.5",
                            )

                            Text(
                                text = "Early Leave: 0",
                            )
                        }

                        Row (
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 24.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Salary: $17939.3",
                                style = MaterialTheme.typography.titleSmall
                            )
                        }

                    }
                }
                Spacer(modifier = Modifier.height(20.dp))

                HorizontalDivider()

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Attendance Record",
                    color = Color.DarkGray
                )

                PaginatedDataTable(
                    columns = listOf(
                        DataColumn(
                            width = TableColumnWidth.MaxIntrinsic
                        ) {
                            Text(text = "Date")
                        },
                        DataColumn(
                            width = TableColumnWidth.MaxIntrinsic
                        ) {
                            Text(text = "Plan")
                        },
                        DataColumn(
                            width = TableColumnWidth.MaxIntrinsic
                        ) {
                            Text(text = "Shift")
                        },
                        DataColumn(
                            width = TableColumnWidth.MaxIntrinsic
                        ) {
                            Text(text = "IN")
                        },
                        DataColumn(
                            width = TableColumnWidth.MaxIntrinsic
                        ) {
                            Text(text = "OUT")
                        },
                        DataColumn(
                            width = TableColumnWidth.MaxIntrinsic
                        ) {
                            Text(text = "Status")
                        },
                    ),
                    state = rememberPaginatedDataTableState(initialPageSize = 10),
                    horizontalPadding = 8.dp
                ) {
                    row {
                        cell {
                            Text(text = "2024-04-01", style = MaterialTheme.typography.bodySmall)
                        }
                        cell {
                            Text(text = "On-Duty", style = MaterialTheme.typography.bodySmall)
                        }
                        cell {
                            Text(text = "Office", style = MaterialTheme.typography.bodySmall)
                        }
                        cell {
                            Text(text = "09:00", style = MaterialTheme.typography.bodySmall)
                        }
                        cell {
                            Text(text = "18:00", style = MaterialTheme.typography.bodySmall)
                        }
                        cell {
                            Text(text = "Attended", style = MaterialTheme.typography.bodySmall)
                        }
                    }

                    row {
                        cell {
                            Text(text = "2024-04-02", style = MaterialTheme.typography.bodySmall)
                        }
                        cell {
                            Text(text = "On-Duty", style = MaterialTheme.typography.bodySmall)
                        }
                        cell {
                            Text(text = "Office", style = MaterialTheme.typography.bodySmall)
                        }
                        cell {
                            Text(text = "08:55", style = MaterialTheme.typography.bodySmall)
                        }
                        cell {
                            Text(text = "18:03", style = MaterialTheme.typography.bodySmall)
                        }
                        cell {
                            Text(text = "Attended", style = MaterialTheme.typography.bodySmall)
                        }
                    }

                    row {
                        cell {
                            Text(text = "2024-04-03", style = MaterialTheme.typography.bodySmall)
                        }
                        cell {
                            Text(text = "On-Duty", style = MaterialTheme.typography.bodySmall)
                        }
                        cell {
                            Text(text = "Office", style = MaterialTheme.typography.bodySmall)
                        }
                        cell {
                            Text(text = "08:58", style = MaterialTheme.typography.bodySmall)
                        }
                        cell {
                            Text(text = "18:00", style = MaterialTheme.typography.bodySmall)
                        }
                        cell {
                            Text(text = "Attended", style = MaterialTheme.typography.bodySmall)
                        }
                    }

                    row {
                        cell {
                            Text(text = "2024-04-04", style = MaterialTheme.typography.bodySmall)
                        }
                        cell {
                            Text(text = "On-Duty", style = MaterialTheme.typography.bodySmall)
                        }
                        cell {
                            Text(text = "Office", style = MaterialTheme.typography.bodySmall)
                        }
                        cell {
                            Text(text = "08:52", style = MaterialTheme.typography.bodySmall)
                        }
                        cell {
                            Text(text = "17:59", style = MaterialTheme.typography.bodySmall)
                        }
                        cell {
                            Text(text = "Attended", style = MaterialTheme.typography.bodySmall)
                        }
                    }

                    row {
                        cell {
                            Text(text = "2024-04-05", style = MaterialTheme.typography.bodySmall)
                        }
                        cell {
                            Text(text = "On-Duty", style = MaterialTheme.typography.bodySmall)
                        }
                        cell {
                            Text(text = "Office", style = MaterialTheme.typography.bodySmall)
                        }
                        cell {
                            Text(text = "09:03", style = MaterialTheme.typography.bodySmall)
                        }
                        cell {
                            Text(text = "18:05", style = MaterialTheme.typography.bodySmall)
                        }
                        cell {
                            Text(text = "Attended", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }

        }
    }
}