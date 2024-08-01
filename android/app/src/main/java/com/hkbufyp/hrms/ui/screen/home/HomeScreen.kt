package com.hkbufyp.hrms.ui.screen.home

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.hkbufyp.hrms.R
import com.hkbufyp.hrms.ui.components.LoadingIndicator
import com.hkbufyp.hrms.shared.SharedViewModel
import com.hkbufyp.hrms.ui.components.LaunchBiometricPrompt
import com.hkbufyp.hrms.ui.navigation.Screen
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    snackbarHostState: SnackbarHostState,
    sharedViewModel: SharedViewModel,
    homeViewModel: HomeViewModel,
    onQuickActionClicked: (screen: Screen) -> Unit,
    onAnnouncementClicked: (id: Int) -> Unit
) {
    val homeUiState by homeViewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        sharedViewModel.isShowAppBar(true)
        sharedViewModel.isEnableSwipeSideBar(true)
        sharedViewModel.setAppBarTitle("Home")
        homeViewModel.onEvent(HomeEvent.Enter)
        homeViewModel.isShowSnackbar.collectLatest {
            if (it) {
                scope.launch {
                    snackbarHostState.showSnackbar(homeUiState.message)
                }
            }
        }
    }


    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        RequireNotificationPermission()
    }

    RequiresPermission()

    if (homeUiState.shouldShowBiometricAlert) {
        SuggestBiometricBox(
            onConfirm = {
                homeViewModel.onEvent(HomeEvent.ShowBiometricPrompt(true))
            },
            onDismiss = {
                homeViewModel.onEvent(HomeEvent.ShowBiometricPrompt(false))
            }
        )
    }

    if (homeUiState.showBiometricPrompt) {
        LaunchBiometricPrompt(
            cryptoObject = homeUiState.cryptoObject!!,
            onSucceeded = {
                homeViewModel.onEvent(HomeEvent.BiometricSucceeded(it))
            },
            onFailed = {

            },
            onError = { _, _ ->

            },
            finally = {
                homeViewModel.onEvent(HomeEvent.ShowBiometricPrompt(false))
            }
        )
    }

    if (homeUiState.isLoadingData) {
        LoadingIndicator()
    } else {
        HomeContent(
            onQuickActionClicked = onQuickActionClicked,
            onAnnouncementClicked = onAnnouncementClicked,
            uiState = homeUiState
        )
    }
}

@Composable
fun HomeContent(
    onQuickActionClicked: (screen: Screen) -> Unit,
    onAnnouncementClicked: (id: Int) -> Unit,
    uiState: HomeUiState
) {
    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        if (uiState.isLoadingToken) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0.8f, 0.8f, 0.8f, 0.7f))
            ) {
                CircularProgressIndicator()
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)
        ) {
            Image(
                painter = painterResource(id = R.drawable.tech_bg),
                contentDescription = "",
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(start = 10.dp, top = 15.dp)
            ) {
                ActionNav.homeQuickActions.forEach {
                    Column(
                        modifier = Modifier
                            .padding(end = 15.dp)
                            .width(100.dp)
                            .height(120.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(
                            onClick = { onQuickActionClicked(it.screen) },
                            shape = CircleShape,
                            colors = ButtonDefaults.buttonColors(Color.Blue),
                            modifier = Modifier.size(70.dp)
                        ) {
                            Icon(
                                imageVector = it.icon,
                                contentDescription = it.name,
                                modifier = Modifier.size(70.dp)
                            )
                        }
                        Text(
                            text = it.name,
                            modifier = Modifier
                                .height(50.dp)
                                .padding(top = 5.dp)
                                .fillMaxWidth(),
                            maxLines = 2,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Text(
                text = "Announcement",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(top = 20.dp, start = 12.dp)
            )

            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 12.dp, top = 10.dp),
            ) {
                items(uiState.announcements, itemContent = { item ->
                    Card(
                        onClick = { onAnnouncementClicked(item.id) },
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
                                    text = item.title,
                                    color = Color.Black,
                                )
                            }

                        }
                    }
                    Divider()
                })
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequireNotificationPermission() {
    val permissionState = rememberPermissionState(
        permission = android.Manifest.permission.POST_NOTIFICATIONS
    )
    val scope = rememberCoroutineScope()

    when {
        permissionState.status.isGranted -> {}
        permissionState.status.shouldShowRationale -> {
            AlertDialog(
                onDismissRequest = {
                    scope.launch { permissionState.launchPermissionRequest() }
                }, 
                confirmButton = { 
                    TextButton(onClick = { 
                        scope.launch { permissionState.launchPermissionRequest() } 
                    }
                    ) {
                        Text(text = "Request permission")
                    }
                },
                icon = {
                    Icon(imageVector = Icons.Filled.Warning, contentDescription = "Warning")
                },
                title = {
                    Text(text = "Permission Needed")
                },
                text = {
                    Text(text = "Notification is important for this app. Please grant the permission.")
                }
            )
        }
        else -> {
            println("Notification permission denied")
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequiresPermission() {
    val permissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_WIFI_STATE,
            android.Manifest.permission.CHANGE_WIFI_STATE,
            android.Manifest.permission.BLUETOOTH_SCAN,
            android.Manifest.permission.BLUETOOTH_CONNECT
        )
    )

    val scope = rememberCoroutineScope()

    scope.launch { permissionState.launchMultiplePermissionRequest() }

    when {
        permissionState.allPermissionsGranted -> {

        }
        permissionState.shouldShowRationale -> {
            AlertDialog(
                onDismissRequest = {
                    scope.launch { permissionState.launchMultiplePermissionRequest() }
                },
                confirmButton = {
                    TextButton(onClick = {
                        scope.launch { permissionState.launchMultiplePermissionRequest() }
                    }
                    ) {
                        Text(text = "Request permission")
                    }
                },
                icon = {
                    Icon(imageVector = Icons.Filled.Warning, contentDescription = "Warning")
                },
                title = {
                    Text(text = "Permission Needed")
                },
                text = {
                    Text(text = "Permission is important for this app. Please grant the permission.")
                }
            )
        }
        else -> {
            println("All permission denied")
        }
    }
}

@Composable
fun SuggestBiometricBox(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {
            onDismiss()
        },
        confirmButton = {
            TextButton(onClick = {
                onConfirm()
            }
            ) {
                Text(text = "Enable")
            }
        },
        dismissButton = {
            TextButton(onClick = {
                onDismiss()
            }
            ) {
                Text(text = "Later")
            }
        },
        icon = {
            Icon(imageVector = Icons.Filled.Fingerprint, contentDescription = "Fingerprint")
        },
        title = {
            Text(text = "Enable Biometric Authentication")
        },
        text = {
            Text(text = "Biometric Authentication is much convenient for using this app, we strongly recommended to enable it.")
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun DefaultPreview() {

}