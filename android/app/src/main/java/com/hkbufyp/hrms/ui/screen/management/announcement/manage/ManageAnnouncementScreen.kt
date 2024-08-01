package com.hkbufyp.hrms.ui.screen.management.announcement.manage

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.hkbufyp.hrms.domain.model.Announcement
import com.hkbufyp.hrms.shared.SharedViewModel
import com.hkbufyp.hrms.ui.components.FullScreenLoading
import com.hkbufyp.hrms.ui.components.LoadingIndicator
import com.hkbufyp.hrms.ui.screen.management.department.departments.DepartmentsEvent
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ManageAnnouncementScreen(
    snackbarHostState: SnackbarHostState,
    sharedViewModel: SharedViewModel,
    manageAnnouncementViewModel: ManageAnnouncementViewModel,
    onEditClicked: (id: Int) -> Unit
) {

    val uiState by manageAnnouncementViewModel.uiState.collectAsState()

    LaunchedEffect(true) {
        sharedViewModel.setAppBarTitle("Manage Announcement")
        manageAnnouncementViewModel.onEvent(ManageAnnouncementEvent.Enter)

        manageAnnouncementViewModel.isShowSnackbar.collectLatest {
            if (it) {
                snackbarHostState.showSnackbar(uiState.message)
            }
        }
    }

    if (uiState.isLoadingData) {
        LoadingIndicator()
    } else {
        SwipeRefresh(
            state = rememberSwipeRefreshState(
                isRefreshing = uiState.isRefreshing
            ),
            onRefresh = {
                manageAnnouncementViewModel.onEvent(ManageAnnouncementEvent.Refresh)
            }
        ) {
            if (uiState.announcements?.isNotEmpty() == true) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                ) {
                    if (uiState.isRemoving) {
                        FullScreenLoading()
                    }

                    if (uiState.isShowAlert) {
                        RemoveAlert(
                            announcement = uiState.removingAnnounce!!,
                            onCancel = {
                                manageAnnouncementViewModel.onEvent(ManageAnnouncementEvent.CancelRemove)
                            },
                            onConfirm = {
                                manageAnnouncementViewModel.onEvent(ManageAnnouncementEvent.Remove)
                            }
                        )
                    }

                    LazyColumn(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        items(uiState.announcements!!, itemContent = { announce ->
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
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(start = 16.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = announce.title,
                                                modifier = Modifier.fillMaxWidth()
                                            )
                                        }

                                        Row (
                                            horizontalArrangement = Arrangement.End,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            IconButton(onClick = {
                                                onEditClicked(announce.id)
                                            }) {
                                                Icon(imageVector = Icons.Filled.Edit, contentDescription = "Edit")
                                            }
                                            IconButton(onClick = {
                                                manageAnnouncementViewModel.onEvent(ManageAnnouncementEvent.SelectRemove(announce))
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
            } else {
                Column (
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "No announcement")
                }
            }
        }
    }
}

@Composable
fun RemoveAlert(
    announcement: Announcement,
    onCancel: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {
            onCancel()
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
        dismissButton = {
            TextButton(
                onClick = {
                    onCancel()
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
            Text(text = "Are you sure to delete this announcement? \n ${announcement.title}")
        }
    )
}

@Composable
@Preview
fun ManageAnnouncementPreview() {
    val list = listOf(
        Announcement(
            id = 0,
            title = "Test Announcement",
            content = "Test Announcement",
            publishDate = "2024-03-21",
            publisher = "Peter Chan",
            publisherId = "23000"
        ),
        Announcement(
            id = 0,
            title = "Test Announcement1",
            content = "Test Announcement1",
            publishDate = "2024-03-21",
            publisher = "Peter Chan",
            publisherId = "23000"
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(list, itemContent = { announce ->
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
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = announce.title,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }

                            Row (
                                horizontalArrangement = Arrangement.End,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                IconButton(onClick = {  }) {
                                    Icon(imageVector = Icons.Filled.Edit, contentDescription = "Edit")
                                }
                                IconButton(onClick = {
                                    //departmentsViewModel.onEvent(DepartmentsEvent.DeleteClicked(it))
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