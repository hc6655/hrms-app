package com.hkbufyp.hrms.ui.screen.management.attendance.clockin_devices.wifi_devices

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.hkbufyp.hrms.domain.model.WifiDevice
import com.hkbufyp.hrms.shared.SharedViewModel
import com.hkbufyp.hrms.ui.components.FullScreenLoading
import com.hkbufyp.hrms.ui.components.LoadingIndicator
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WifiDeviceScreen(
    snackbarHostState: SnackbarHostState,
    sharedViewModel: SharedViewModel,
    wifiDeviceViewModel: WifiDeviceViewModel,
    onAddDeviceClicked: () -> Unit,
    onNavigateUpClicked: () -> Unit
) {
    val uiState by wifiDeviceViewModel.uiState.collectAsState()

    LaunchedEffect(true) {
        sharedViewModel.isShowAppBar(false)
        wifiDeviceViewModel.onEvent(WifiDeviceEvent.Enter)

        launch {
            wifiDeviceViewModel.isShowSnackbar.collectLatest {
                if (it) {
                    snackbarHostState.showSnackbar(uiState.message)
                }
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Wifi Devices") },
                navigationIcon = {
                    IconButton(
                        onClick = { onNavigateUpClicked() }
                    ) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(
                        onClick = { onAddDeviceClicked() }
                    ) {
                        Icon(imageVector = Icons.Filled.Add, contentDescription = "Add")
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
            if (uiState.isLoading) {
                LoadingIndicator()
            } else {
                if (uiState.isRemoving) {
                    FullScreenLoading()
                }

                if (uiState.isShowConfirmationBox) {
                    RemoveWifiDeviceConfirmationBox(
                        device = uiState.removeDevice,
                        onConfirm = {
                            wifiDeviceViewModel.onEvent(WifiDeviceEvent.ConfirmRemove)
                        },
                        onDismiss = {
                            wifiDeviceViewModel.onEvent(WifiDeviceEvent.CancelRemove)
                        }
                    )
                }

                Column(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    if (uiState.devices?.isEmpty() != false) {
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
                            items(uiState.devices!!) { device ->
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
                                    Box (
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Row (
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.Center,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Column {
                                                Text(
                                                    text = device.ssid,
                                                    textAlign = TextAlign.Center,
                                                    modifier = Modifier.fillMaxWidth()
                                                )
                                                Text(
                                                    text = device.bssid,
                                                    textAlign = TextAlign.Center,
                                                    modifier = Modifier.fillMaxWidth()
                                                )
                                            }
                                        }

                                        Row (
                                            horizontalArrangement = Arrangement.End,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            IconButton(onClick = {
                                                wifiDeviceViewModel.onEvent(WifiDeviceEvent.RemoveDevice(device))
                                            }) {
                                                Icon(imageVector = Icons.Filled.Delete, contentDescription = "Remove")
                                            }
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
}

@Composable
fun RemoveWifiDeviceConfirmationBox(
    device: WifiDevice?,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    if (device != null) {
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
                    Text(text = "Are you sure to remove this wifi for clock-in verification?")
                    Text(text = "SSID: ${device.ssid}")
                    Text(text = "BSSID: ${device.bssid}")
                }
            }
        )
    }
}