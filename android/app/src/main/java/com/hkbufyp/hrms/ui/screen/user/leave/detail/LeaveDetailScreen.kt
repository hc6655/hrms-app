package com.hkbufyp.hrms.ui.screen.user.leave.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.hkbufyp.hrms.domain.enums.toStr
import com.hkbufyp.hrms.ui.components.LoadingIndicator
import com.hkbufyp.hrms.shared.SharedViewModel
import com.seanproctor.datatable.DataColumn
import com.seanproctor.datatable.TableColumnWidth
import com.seanproctor.datatable.material3.PaginatedDataTable
import com.seanproctor.datatable.paging.rememberPaginatedDataTableState

@Composable
fun LeaveDetailScreen(
    sharedViewModel: SharedViewModel,
    leaveDetailViewModel: LeaveDetailViewModel
) {

    val uiState by leaveDetailViewModel.uiState.collectAsState()

    LaunchedEffect(key1 = true) {
        sharedViewModel.setAppBarTitle("My Leave Record")
    }

    if (uiState.isLoadingData) {
        LoadingIndicator()
    } else {
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing = uiState.isRefreshing),
            onRefresh = { leaveDetailViewModel.onEvent(LeaveDetailEvent.Refresh) }
        ) {
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {

                if (uiState.leaveApplication == null) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Unable to get the record!",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                } else {
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
                                text = "Employee ID: ",
                                color = Color.Black,
                                modifier = Modifier.fillMaxWidth(0.4f),
                                textAlign = TextAlign.Left
                            )
                            Text(
                                text = uiState.leaveApplication?.employeeId ?: "",
                                color = Color.Black,
                                textAlign = TextAlign.Left
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Apply Date: ",
                                color = Color.Black,
                                modifier = Modifier.fillMaxWidth(0.4f),
                                textAlign = TextAlign.Left
                            )
                            Text(
                                text = uiState.leaveApplication?.applyDate ?: "",
                                color = Color.Black,
                                textAlign = TextAlign.Left
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Start Date: ",
                                color = Color.Black,
                                modifier = Modifier.fillMaxWidth(0.4f),
                                textAlign = TextAlign.Left
                            )
                            Text(
                                text = uiState.leaveApplication?.startDate ?: "",
                                color = Color.Black,
                                textAlign = TextAlign.Left
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "End Date: ",
                                color = Color.Black,
                                modifier = Modifier.fillMaxWidth(0.4f),
                                textAlign = TextAlign.Left
                            )
                            Text(
                                text = uiState.leaveApplication?.endDate ?: "",
                                color = Color.Black,
                                textAlign = TextAlign.Left
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Days: ",
                                color = Color.Black,
                                modifier = Modifier.fillMaxWidth(0.4f),
                                textAlign = TextAlign.Left
                            )
                            Text(
                                text = uiState.leaveApplication?.days?.toString() ?: "",
                                color = Color.Black,
                                textAlign = TextAlign.Left
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Leave Type: ",
                                color = Color.Black,
                                modifier = Modifier.fillMaxWidth(0.4f),
                                textAlign = TextAlign.Left
                            )
                            Text(
                                text = uiState.leaveApplication?.leaveType?.name ?: "",
                                color = Color.Black,
                                textAlign = TextAlign.Left
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Reason: ",
                                color = Color.Black,
                                modifier = Modifier.fillMaxWidth(0.4f),
                                textAlign = TextAlign.Left
                            )
                            Text(
                                text = uiState.leaveApplication?.reason ?: "",
                                color = Color.Black,
                                textAlign = TextAlign.Left
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Status: ",
                                color = Color.Black,
                                modifier = Modifier.fillMaxWidth(0.4f),
                                textAlign = TextAlign.Left
                            )
                            Text(
                                text = uiState.leaveApplication?.status?.toStr() ?: "",
                                color = Color.Black,
                                textAlign = TextAlign.Left
                            )
                        }

                        HorizontalDivider(modifier = Modifier.padding(top = 24.dp))

                        Column (
                            modifier = Modifier.padding(top = 12.dp)
                        ) {
                            Text(
                                text = "Timeline",
                                color = Color.DarkGray
                            )
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp)
                        ) {
                            PaginatedDataTable(
                                columns = listOf(
                                    DataColumn(
                                        width = TableColumnWidth.Fixed(90.dp)
                                    ) {
                                        Text(text = "DateTime")
                                    },
                                    DataColumn(
                                        width = TableColumnWidth.MinIntrinsic
                                    ) {
                                        Text(text = "Operator")
                                    },
                                    DataColumn(
                                        width = TableColumnWidth.MinIntrinsic
                                    ) {
                                        Text(text = "Status")
                                    },
                                    DataColumn(
                                        width = TableColumnWidth.MinIntrinsic
                                    ) {
                                        Text(text = "Comment")
                                    }
                                ),
                                state = rememberPaginatedDataTableState(initialPageSize = 10),
                                horizontalPadding = 8.dp
                            ) {
                                uiState.leaveApplication?.logs?.forEach { log ->
                                    row {
                                        cell {
                                            Text(text = log.dateTime, style = MaterialTheme.typography.bodySmall)
                                        }
                                        cell {
                                            Text(text = log.operatorId, style = MaterialTheme.typography.bodySmall)
                                        }
                                        cell {
                                            Text(text = log.status, style = MaterialTheme.typography.bodySmall)
                                        }
                                        cell {
                                            Text(text = log.reason, style = MaterialTheme.typography.bodySmall)
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
}