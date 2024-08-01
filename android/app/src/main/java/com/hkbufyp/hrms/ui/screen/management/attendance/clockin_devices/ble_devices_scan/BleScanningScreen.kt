package com.hkbufyp.hrms.ui.screen.management.attendance.clockin_devices.ble_devices_scan

import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
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
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.hkbufyp.hrms.domain.model.BleDevice
import com.hkbufyp.hrms.domain.model.WifiDevice
import com.hkbufyp.hrms.shared.SharedViewModel
import com.hkbufyp.hrms.ui.components.FullScreenLoading
import com.hkbufyp.hrms.ui.components.LoadingIndicator
import com.hkbufyp.hrms.ui.screen.management.attendance.clockin_devices.wifi_devices_scan.WifiConfirmationBox
import com.hkbufyp.hrms.ui.screen.management.attendance.clockin_devices.wifi_devices_scan.WifiScanningEvent
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BleScanningScreen(
    snackbarHostState: SnackbarHostState,
    sharedViewModel: SharedViewModel,
    bleScanningViewModel: BleScanningViewModel,
    onNavigateUpClicked: () -> Unit,
    onAddSucceeded: () -> Unit
) {
    val uiState by bleScanningViewModel.uiState.collectAsState()

    LaunchedEffect(true) {
        bleScanningViewModel.onEvent(BleScanningEvent.Enter)

        launch {
            bleScanningViewModel.isShowSnackbar.collectLatest {
                if (it) {
                    snackbarHostState.showSnackbar(uiState.message)
                }
            }
        }

        launch {
            bleScanningViewModel.isAddDeviceSucceeded.collectLatest {
                if (it) {
                    onAddSucceeded()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Scanning Ble")
                },
                actions = {
                    IconButton(
                        onClick = {
                            bleScanningViewModel.onEvent(BleScanningEvent.Refresh)
                        }
                    ) {
                        Icon(imageVector = Icons.Filled.Refresh, contentDescription = "Refresh")
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = { onNavigateUpClicked() }
                    ) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .background(Color.White)
        ) {
            FullScreenLoading(
                show = uiState.isSubmitting
            )

            BleConfirmationBox(
                showDialog = uiState.isShowConfirmationBox,
                scanResult = uiState.selectedBle,
                onConfirm = {
                    bleScanningViewModel.onEvent(BleScanningEvent.Confirm)
                },
                onDismiss = {
                    bleScanningViewModel.onEvent(BleScanningEvent.Cancel)
                }
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                if (uiState.bleList.isEmpty() && !uiState.isScanning) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "No Device",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                } else {
                    LazyColumn {
                        items(uiState.bleList.toList()) { map ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(80.dp)
                                    .clickable {
                                        bleScanningViewModel.onEvent(BleScanningEvent.SelectDevice(map.second))
                                    },
                                shape = RectangleShape,
                                colors = CardDefaults.cardColors(
                                    contentColor = Color.Black,
                                    containerColor = Color.White
                                )
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    if (ActivityCompat.checkSelfPermission(
                                            LocalContext.current,
                                            Manifest.permission.BLUETOOTH_CONNECT
                                        ) == PackageManager.PERMISSION_GRANTED
                                    ) {
                                        Text(text = map.second.device.name ?: "N/A")
                                        Text(text = map.first)
                                    }
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
fun BleConfirmationBox(
    showDialog: Boolean,
    scanResult: BleDevice?,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    if (showDialog && scanResult != null) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            confirmButton = {
                TextButton(
                    onClick = { onConfirm() }
                ) {
                    Text(text = "Confirm")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { onDismiss() }
                ) {
                    Text(text = "Cancel")
                }
            },
            text = {
                Column {
                    Text(text = "Are you sure to chose this ble for clock-in verification?")
                    Text(text = "Name: ${scanResult.name}")
                    Text(text = "Mac: ${scanResult.mac}")
                }
            }
        )
    }
}