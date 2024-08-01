package com.hkbufyp.hrms.ui.screen.management.leave.approval

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.hkbufyp.hrms.domain.enums.isApproved
import com.hkbufyp.hrms.domain.enums.isRejected
import com.hkbufyp.hrms.domain.enums.toStr
import com.hkbufyp.hrms.ui.components.LoadingIndicator
import com.hkbufyp.hrms.shared.SharedViewModel
import com.hkbufyp.hrms.ui.components.FullScreenLoading
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LeaveApprovalScreen(
    snackbarHostState: SnackbarHostState,
    sharedViewModel: SharedViewModel,
    leaveApprovalViewModel: LeaveApprovalViewModel
) {

    val uiState by leaveApprovalViewModel.uiState.collectAsState()

    LaunchedEffect(true) {
        sharedViewModel.setAppBarTitle("Leave Approval")
        leaveApprovalViewModel.isShowSnackbar.collectLatest {
            if (it) {
                snackbarHostState.showSnackbar(uiState.message)
            }
        }
    }

    if (uiState.isLoadingData) {
        LoadingIndicator()
    } else {
        FullScreenLoading(
            show = uiState.isApproving || uiState.isRejecting
        )

        SwipeRefresh(
            state = rememberSwipeRefreshState(uiState.isRefreshing),
            onRefresh = { leaveApprovalViewModel.onEvent(LeaveApprovalEvent.Refresh) },
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
                RejectReasonDialog(
                    showDialog = uiState.isShowDialog,
                    value = uiState.reason,
                    onValueChanged = {
                        leaveApprovalViewModel.onEvent(LeaveApprovalEvent.ReasonChanged(it))
                    },
                    onConfirm = {
                        leaveApprovalViewModel.onEvent(LeaveApprovalEvent.Reject)
                    },
                    onCancel = {
                        leaveApprovalViewModel.onEvent(LeaveApprovalEvent.HideDialog)
                    }
                )

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
                            text = uiState.leaveApplication?.days.toString(),
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

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (uiState.leaveApplication?.status?.isApproved() == false) {
                            Button(
                                onClick = {
                                    leaveApprovalViewModel.onEvent(LeaveApprovalEvent.Approve)
                                },
                                modifier = Modifier.weight(1f),
                            ) {
                                Text(text = "Approve")
                            }

                            Spacer(modifier = Modifier.width(15.dp))
                        }

                        if (uiState.leaveApplication?.status?.isRejected() == false) {
                            Button(
                                onClick = {
                                    leaveApprovalViewModel.onEvent(LeaveApprovalEvent.ShowDialog)
                                },
                                modifier = Modifier.weight(1f),
                            ) {
                                Text(text = "Reject")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RejectReasonDialog(
    showDialog: Boolean,
    value: String,
    onValueChanged: (value: String) -> Unit,
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(showDialog) {
        if (showDialog) {
            focusRequester.requestFocus()
        }
    }

    if (showDialog) {
        Dialog(
            onDismissRequest = {
                focusManager.clearFocus()
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
                        text = "Please provide a reason",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(top = 10.dp)
                    )

                    TextField(
                        value = value,
                        onValueChange = { onValueChanged(it) },
                        placeholder = {
                            Text(text = "Reason")
                        },
                        modifier = Modifier.focusRequester(focusRequester)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(
                            onClick = {
                                focusManager.clearFocus()
                                onCancel()
                            }
                        ) {
                            Text(text = "Cancel")
                        }

                        TextButton(
                            onClick = {
                                focusManager.clearFocus()
                                onConfirm() 
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