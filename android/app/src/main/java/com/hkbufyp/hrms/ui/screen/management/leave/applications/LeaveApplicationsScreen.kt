package com.hkbufyp.hrms.ui.screen.management.leave.applications

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.hkbufyp.hrms.domain.enums.isPending
import com.hkbufyp.hrms.domain.enums.toStr
import com.hkbufyp.hrms.ui.components.LoadingIndicator
import com.hkbufyp.hrms.shared.SharedViewModel

@Composable
fun LeaveApplicationsScreen(
    sharedViewModel: SharedViewModel,
    leaveApplicationsViewModel: LeaveApplicationsViewModel,
    onClickedVacationDetails: (id: Int) -> Unit
) {
    val uiState by leaveApplicationsViewModel.uiState.collectAsState()

    LaunchedEffect(true) {
        sharedViewModel.setAppBarTitle("Leave Applications")
        leaveApplicationsViewModel.onEvent(LeaveApplicationsEvent.Enter)
    }

    if (uiState.isLoadingData) {
        LoadingIndicator()
    } else {
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing = uiState.isRefreshing),
            onRefresh = {
                leaveApplicationsViewModel.onEvent(LeaveApplicationsEvent.Refresh)
            }
        ) {
            Box(modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)
            ) {
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize(),
                ) {
                    items(uiState.leaveApplications, itemContent = {
                        val fontWeight = if (it.status.isPending()) {
                            FontWeight.Bold
                        } else {
                            FontWeight.Normal
                        }

                        val fontColor = if (it.status.isPending()) {
                            Color.Black
                        } else {
                            Color.Gray
                        }

                        Card(
                            onClick = { onClickedVacationDetails(it.id) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(80.dp),
                            shape = RectangleShape,
                            colors = CardDefaults.cardColors(
                                contentColor = Color.Black,
                                containerColor = Color.White
                            )
                        ) {
                            Column(
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(start = 20.dp, end = 20.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = it.employeeId + ": " + it.leaveType.name,
                                        fontWeight = fontWeight,
                                        color = fontColor,
                                        modifier = Modifier.weight(1f)
                                    )

                                    Text(
                                        text = it.applyDate,
                                        fontWeight = fontWeight,
                                        color = fontColor,
                                    )
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = it.startDate + " - " + it.endDate,
                                        fontWeight = fontWeight,
                                        color = fontColor,
                                        modifier = Modifier.weight(1f)
                                    )

                                    Text(
                                        text = it.status.toStr(),
                                        fontWeight = fontWeight,
                                        color = fontColor,
                                    )
                                }

                                Text(
                                    text = "Apply for " + it.days.toString() + " days",
                                    fontWeight = fontWeight,
                                    color = fontColor,
                                )
                            }
                        }
                        Divider()
                    })
                }
            }
        }
    }
}