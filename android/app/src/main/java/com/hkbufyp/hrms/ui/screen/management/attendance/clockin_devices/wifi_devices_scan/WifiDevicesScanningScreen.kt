package com.hkbufyp.hrms.ui.screen.management.attendance.clockin_devices.wifi_devices_scan

import android.net.wifi.ScanResult
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
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hkbufyp.hrms.domain.model.WifiDevice
import com.hkbufyp.hrms.shared.SharedViewModel
import com.hkbufyp.hrms.ui.components.FullScreenLoading
import com.hkbufyp.hrms.ui.components.LoadingIndicator
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WifiDevicesScanningScreen(
    snackbarHostState: SnackbarHostState,
    sharedViewModel: SharedViewModel,
    wifiScanningViewModel: WifiScanningViewModel,
    onNavigationUpClicked: () -> Unit,
    onAddSucceeded: () -> Unit

) {
    val uiState by wifiScanningViewModel.uiState.collectAsState()

    LaunchedEffect(true) {
        sharedViewModel.isShowAppBar(false)

        launch {
            wifiScanningViewModel.isShowSnackbar.collectLatest {
                if (it) {
                    snackbarHostState.showSnackbar(uiState.message)
                }
            }
        }

        launch {
            wifiScanningViewModel.isAddDeviceSucceeded.collectLatest {
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
                    Text(text = "Scanning Wifi")
                },
                actions = {
                    IconButton(
                        onClick = {
                            wifiScanningViewModel.onEvent(WifiScanningEvent.Refresh)
                        }
                    ) {
                        Icon(imageVector = Icons.Filled.Refresh, contentDescription = "Refresh")
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = { onNavigationUpClicked() }
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
            if (uiState.isLoading) {
                LoadingIndicator()
            } else {

                if (uiState.isSubmitting) {
                    FullScreenLoading()
                }

                if (uiState.isShowConfirmationBox) {
                    WifiConfirmationBox(
                        scanResult = uiState.selectedWifi,
                        onConfirm = {
                            wifiScanningViewModel.onEvent(WifiScanningEvent.ConfirmSelect)
                        },
                        onDismiss = {
                            wifiScanningViewModel.onEvent(WifiScanningEvent.CancelSelect)
                        }
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                ) {
                    if (uiState.wifiList?.isEmpty() != false) {
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
                            items(uiState.wifiList!!) { scanResult ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(80.dp)
                                        .clickable {
                                            wifiScanningViewModel.onEvent(
                                                WifiScanningEvent.SelectDevice(
                                                    scanResult
                                                )
                                            )
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
                                        Text(text = scanResult.SSID)
                                        Text(text = scanResult.BSSID)
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
fun WifiConfirmationBox(
    scanResult: WifiDevice?,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    if (scanResult != null) {
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
                    Text(text = "Are you sure to chose this wifi for clock-in verification?")
                    Text(text = "SSID: ${scanResult.ssid}")
                    Text(text = "BSSID: ${scanResult.bssid}")
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun WifiScanningPreview() {

}