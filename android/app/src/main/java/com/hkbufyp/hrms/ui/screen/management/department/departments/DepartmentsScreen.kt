package com.hkbufyp.hrms.ui.screen.management.department.departments

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.hkbufyp.hrms.shared.SharedViewModel
import com.hkbufyp.hrms.ui.components.LoadingIndicator
import kotlinx.coroutines.flow.collectLatest

@Composable
fun DepartmentsScreen(
    snackbarHostState: SnackbarHostState,
    sharedViewModel: SharedViewModel,
    departmentsViewModel: DepartmentsViewModel,
    onEditOrCreateClicked: (String?) -> Unit
) {
    val uiState by departmentsViewModel.uiState.collectAsState()

    LaunchedEffect(true) {
        sharedViewModel.setAppBarTitle("Departments")
        departmentsViewModel.onEvent(DepartmentsEvent.Enter)

        departmentsViewModel.isShowSnackbar.collectLatest {
            if (it) {
                snackbarHostState.showSnackbar(uiState.message)
            }
        }
    }

    if (uiState.isDeleteClicked) {
        AlertDialog(
            onDismissRequest = {
               departmentsViewModel.onEvent(DepartmentsEvent.DeleteCancelled)
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        departmentsViewModel.onEvent(DepartmentsEvent.DeleteConfirmed)
                    }
                ) {
                    Text(text = "Confirm")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        departmentsViewModel.onEvent(DepartmentsEvent.DeleteCancelled)
                    }
                ) {
                    Text(text = "Cancel")
                }
            },
            icon = {
                Icon(imageVector = Icons.Filled.Delete, contentDescription = "Delete")
            },
            title = {
                Text(text = "Confirmation Needed")
            },
            text = {
                Text(text = "Are you sure to delete ${uiState.currentDeleteDepartment?.title} department?")
            }
        )
    }

    if (uiState.isLoadingData) {
        LoadingIndicator()
    } else {
        Scaffold (
            floatingActionButton = {
                FloatingActionButton(onClick = { onEditOrCreateClicked(null) }) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = "Add")
                }
            }
        ) {
            SwipeRefresh(
                state = rememberSwipeRefreshState(
                    isRefreshing = uiState.isRefreshing
                ),
                onRefresh = {
                    departmentsViewModel.onEvent(DepartmentsEvent.Refresh)
                }
            ) {
                Column (
                    modifier = Modifier
                        .padding(it)
                        .background(color = Color.White)
                        .fillMaxSize()
                ) {
                    LazyColumn(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        items(uiState.departments, itemContent = {
                            Card(
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
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Box (
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Row (
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.Center,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = it.title,
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier.fillMaxWidth()
                                            )
                                        }

                                        Row (
                                            horizontalArrangement = Arrangement.End,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            IconButton(onClick = { onEditOrCreateClicked(it.id) }) {
                                                Icon(imageVector = Icons.Filled.Edit, contentDescription = "Edit")
                                            }
                                            IconButton(onClick = {
                                                departmentsViewModel.onEvent(DepartmentsEvent.DeleteClicked(it))
                                            }) {
                                                Icon(imageVector = Icons.Filled.Delete, contentDescription = "Delete")
                                            }
                                        }
                                    }

                                }
                            }
                            HorizontalDivider()
                        })
                    }
                }
            }
        }
    }
}