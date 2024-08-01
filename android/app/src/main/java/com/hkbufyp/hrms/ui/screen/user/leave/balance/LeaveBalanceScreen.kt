package com.hkbufyp.hrms.ui.screen.user.leave.balance

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hkbufyp.hrms.domain.model.LeaveBalance
import com.hkbufyp.hrms.domain.model.LeaveType
import com.hkbufyp.hrms.ui.components.LoadingIndicator
import com.hkbufyp.hrms.shared.SharedViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LeaveBalanceScreen(
    snackbarHostState: SnackbarHostState,
    sharedViewModel: SharedViewModel,
    leaveBalanceViewModel: LeaveBalanceViewModel
) {
    val leaveStatusUiState by leaveBalanceViewModel.uiState.collectAsState()

    LaunchedEffect(key1 = true) {
        sharedViewModel.setAppBarTitle("My Leave Balance")
        leaveBalanceViewModel.isShowSnackbar.collectLatest {
            if (it) {
                snackbarHostState.showSnackbar(leaveStatusUiState.message)
            }
        }
    }

    if (leaveStatusUiState.isLoadingData) {
        LoadingIndicator()
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                content = {
                    items(leaveStatusUiState.balances) { balance ->
                        Card(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp),
                            shape = RoundedCornerShape(10.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = balance.leaveType.name,
                                    style = MaterialTheme.typography.titleLarge,
                                    modifier = Modifier.padding(top = 12.dp)
                                )

                                Text(
                                    text = "${balance.daysLeft} / ${balance.totalDays}",
                                    style = MaterialTheme.typography.headlineLarge,
                                    modifier = Modifier.padding(vertical = 16.dp)
                                )
                            }
                        }
                    }
                }
            )
        }
    }
}

@Composable
@Preview
fun LeaveBalancePreview() {
    val list = listOf(
        LeaveBalance(
            employeeId = "123",
            leaveType = LeaveType(0, "Annual Leave"),
            totalDays = 7,
            daysLeft = 6),
        LeaveBalance(
            employeeId = "123",
            leaveType = LeaveType(0, "Sick Leave"),
            totalDays = 7,
            daysLeft = 5),
        LeaveBalance(
            employeeId = "123",
            leaveType = LeaveType(0, "Birthday Leave"),
            totalDays = 1,
            daysLeft = 1),
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            content = {
                items(list) { balance ->
                    Card(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp),
                        shape = RoundedCornerShape(10.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = balance.leaveType.name,
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.padding(top = 12.dp)
                            )

                            Text(
                                text = "${balance.daysLeft} / ${balance.totalDays}",
                                style = MaterialTheme.typography.headlineLarge,
                                modifier = Modifier.padding(vertical = 16.dp)
                            )
                        }
                    }
                }
            }
        )
    }
}