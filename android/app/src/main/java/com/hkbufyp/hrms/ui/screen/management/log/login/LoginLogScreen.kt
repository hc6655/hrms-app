package com.hkbufyp.hrms.ui.screen.management.log.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AssistChip
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginLogScreen(
    snackbarHostState: SnackbarHostState,
    sharedViewModel: SharedViewModel,
    loginLogViewModel: LoginLogViewModel,
    onNavigationClicked: () -> Unit
) {

    val uiState by loginLogViewModel.uiState.collectAsState()

    LaunchedEffect(true) {
        sharedViewModel.isShowAppBar(false)

        loginLogViewModel.isShowSnackbar.collectLatest {
            if (it) {
                snackbarHostState.showSnackbar(uiState.message)
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Login Log")
                },
                navigationIcon = {
                    IconButton(onClick = { onNavigationClicked() }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Filled.Search, contentDescription = "Search")
                    }
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Filled.FilterList, contentDescription = "Filter")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) {
        Box (
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(it)
        ) {
            if (uiState.isLoadingData) {
                LoadingIndicator()
            } else {
                if (uiState.logs.isEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = "No record found")
                    }
                } else {
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
                                DataColumn {
                                    Text(text = "ID")
                                },
                                DataColumn {
                                    Text(text = "IP")
                                },
                                DataColumn {
                                    Text(text = "Method")
                                },
                                DataColumn {
                                    Text(text = "Successful")
                                }
                            ),
                            state = rememberPaginatedDataTableState(initialPageSize = 10),
                            horizontalPadding = 8.dp
                        ) {
                            uiState.logs.forEach { log ->
                                row {
                                    cell {
                                        Text(text = log.dateTime, style = MaterialTheme.typography.bodySmall)
                                    }
                                    cell {
                                        Text(text = log.employeeId, style = MaterialTheme.typography.bodySmall)
                                    }
                                    cell {
                                        Text(text = log.ip, style = MaterialTheme.typography.bodySmall)
                                    }
                                    cell {
                                        Text(text = log.loginMethod, style = MaterialTheme.typography.bodySmall)
                                    }
                                    cell {
                                        Text(
                                            text = if (log.isSuccess) "Success" else "Failed",
                                            style = MaterialTheme.typography.bodySmall
                                        )
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
fun LoginLogPreview() {

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Login Log")
                },
                navigationIcon = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Filled.Search, contentDescription = "Search")
                    }
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Filled.FilterList, contentDescription = "Filter")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) {
        Box (
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(it)
        ) {
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
                            Text(text = "ID")
                        },
                        DataColumn(
                            width = TableColumnWidth.MinIntrinsic
                        ) {
                            Text(text = "IP")
                        },
                        DataColumn(
                            width = TableColumnWidth.MinIntrinsic
                        ) {
                            Text(text = "Method")
                        },
                        DataColumn(
                            width = TableColumnWidth.MinIntrinsic
                        ) {
                            Text(text = "Successful")
                        }
                    ),
                    state = rememberPaginatedDataTableState(initialPageSize = 10),
                    horizontalPadding = 8.dp
                ) {
                    row {
                        cell {
                            Text(text = "2024-03-31 14:00:00", style = MaterialTheme.typography.bodySmall)
                        }
                        cell {
                            Text(text = "23000", style = MaterialTheme.typography.bodySmall)
                        }
                        cell {
                            Text(text = "123.41.25.3", style = MaterialTheme.typography.bodySmall)
                        }
                        cell {
                            Text(text = "Biometric", style = MaterialTheme.typography.bodySmall)
                        }
                        cell {
                            Text(text = "Success", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}