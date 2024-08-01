package com.hkbufyp.hrms.ui.screen.user.leave.history

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
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.shimmer
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.hkbufyp.hrms.domain.enums.isPending
import com.hkbufyp.hrms.domain.enums.toStr
import com.hkbufyp.hrms.ui.components.LoadingIndicator
import com.hkbufyp.hrms.shared.SharedViewModel

@Composable
fun LeaveHistoryScreen(
    sharedViewModel: SharedViewModel,
    leaveHistoryViewModel: LeaveHistoryViewModel,
    onClickedVacationDetails: (id: Int) -> Unit
) {

    val uiState by leaveHistoryViewModel.uiState.collectAsState()

    LaunchedEffect(key1 = true) {
        sharedViewModel.setAppBarTitle("My Leave History")
        leaveHistoryViewModel.onEvent(LeaveHistoryEvent.Enter)
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing = uiState.isRefreshing),
            onRefresh = { leaveHistoryViewModel.onEvent(LeaveHistoryEvent.Refresh) },
        ) {
            if (uiState.leaveApplications.isNotEmpty()) {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.White)
                ) {
                    LazyColumn(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxSize(),
                        userScrollEnabled = !uiState.isLoadingData
                    ) {
                        items(uiState.leaveApplications, itemContent = {
                            val fontWeight = if (it?.status?.isPending() == true) {
                                FontWeight.Bold
                            } else {
                                FontWeight.Normal
                            }

                            val fontColor = if (it?.status?.isPending() == true) {
                                Color.Black
                            } else {
                                Color.Gray
                            }

                            Card(
                                onClick = { onClickedVacationDetails(it?.id ?: 0) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(100.dp),
                                shape = RectangleShape,
                                colors = CardDefaults.cardColors(
                                    contentColor = Color.Black,
                                    containerColor = Color.White,
                                    disabledContainerColor = Color.White
                                ),
                                enabled = !uiState.isLoadingData
                            ) {
                                Column(
                                    verticalArrangement = Arrangement.Center,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(start = 20.dp, end = 20.dp)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(bottom = 10.dp)
                                    ) {
                                        Text(
                                            text = if (it == null) "" else it.employeeId + ": " + it.leaveType.name,
                                            fontWeight = fontWeight,
                                            color = fontColor,
                                            modifier = Modifier.weight(1f).placeholder(visible = it == null)
                                        )

                                        Text(
                                            text = it?.applyDate ?: "",
                                            fontWeight = fontWeight,
                                            color = fontColor,
                                            modifier = Modifier.placeholder(visible = it == null)
                                        )
                                    }

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(bottom = 10.dp)
                                    ) {
                                        Text(
                                            text = if (it == null) "" else it.startDate + " - " + it.endDate,
                                            fontWeight = fontWeight,
                                            color = fontColor,
                                            modifier = Modifier.weight(1f).placeholder(visible = it == null)
                                        )

                                        Text(
                                            text = it?.status?.toStr() ?: "",
                                            fontWeight = fontWeight,
                                            color = fontColor,
                                            modifier = Modifier.placeholder(visible = it == null)
                                        )
                                    }

                                    Text(
                                        text = if (it == null) "" else "Apply for " + it.days.toString() + " days",
                                        fontWeight = fontWeight,
                                        color = fontColor,
                                        modifier = Modifier.placeholder(visible = it == null)
                                    )
                                }
                            }
                            Divider()
                        })
                    }
                }
            } else {
                Column (
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "No Record",)
                }
            }
        }
    }
}