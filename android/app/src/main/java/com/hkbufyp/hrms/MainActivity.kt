package com.hkbufyp.hrms

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.hkbufyp.hrms.data.services.HrFirebaseMessagingService
import com.hkbufyp.hrms.ui.AppScaffold
import com.hkbufyp.hrms.ui.theme.HRMSTheme
import com.hkbufyp.hrms.shared.SharedViewModel
import com.hkbufyp.hrms.ui.components.LoadingIndicator
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.compose.koinInject

class MainActivity : FragmentActivity() {
    private val sharedViewModel: SharedViewModel by viewModel()
    private var bssid = ""

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        setContent {
            HRMSTheme(darkTheme = false) {
                ScaffoldDefaults.contentWindowInsets
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navHostController = rememberNavController()
                    val systemUiController = rememberSystemUiController()

                    SideEffect {
                        systemUiController.setStatusBarColor(
                            color = Color(0xffffffff),
                            darkIcons = true
                        )

                        systemUiController.setNavigationBarColor(
                            color = Color(0xffffffff),
                            darkIcons = true
                        )
                    }

                    /*val receiver = object: BroadcastReceiver() {
                        override fun onReceive(context: Context?, intent: Intent?) {
                            if (WifiManager.NETWORK_STATE_CHANGED_ACTION == intent?.action) {
                                val networkInfo = intent.getParcelableExtra<NetworkInfo>(WifiManager.EXTRA_NETWORK_INFO)
                                if (ConnectivityManager.TYPE_WIFI == networkInfo?.type) {
                                    val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
                                    val info = wifiManager.connectionInfo
                                    println("info: $info")
                                    bssid = info.bssid
                                }
                            }
                        }
                    }

                    val intentFilter = IntentFilter()
                    intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION)
                    applicationContext.registerReceiver(receiver, intentFilter)*/

                    /*val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                    val networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                    val wifiInfo = networkCapabilities?.transportInfo as WifiInfo

                    println("wifiInfo: $wifiInfo")
                    bssid = wifiInfo.bssid*/

                    SplashScreen(
                        navHostController = navHostController,
                        sharedViewModel = sharedViewModel
                    )
                }
            }
        }
    }

    @Composable
    fun SplashScreen(
        navHostController: NavHostController,
        sharedViewModel: SharedViewModel
    ) {
        val uiState by sharedViewModel.sharedUiState.collectAsState()

        RequiresPermission()

        if (uiState.initialized) {
            AppScaffold(navHostController, sharedViewModel)
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color(0xffffffff))
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(top = 50.dp)
                ) {
                    /*Image(
                        painter = painterResource(id = ),
                        contentDescription = "",
                        modifier = Modifier.size(150.dp, 150.dp)
                    )*/
                }

                Row {
                    LoadingIndicator()
                }


            }
        }
    }

    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    fun RequiresPermission() {
        val permissionState = rememberMultiplePermissionsState(
            permissions = listOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.CHANGE_WIFI_STATE,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.POST_NOTIFICATIONS,
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT
            )
        )

        val scope = rememberCoroutineScope()

        scope.launch { permissionState.launchMultiplePermissionRequest() }

        when {
            permissionState.allPermissionsGranted -> {
                println("All permission granted")
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
}