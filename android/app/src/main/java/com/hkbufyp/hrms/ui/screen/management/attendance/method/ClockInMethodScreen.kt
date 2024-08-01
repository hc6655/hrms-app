package com.hkbufyp.hrms.ui.screen.management.attendance.method

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hkbufyp.hrms.shared.SharedViewModel
import com.hkbufyp.hrms.ui.components.FullScreenLoading
import com.hkbufyp.hrms.ui.components.LoadingIndicator
import com.hkbufyp.hrms.ui.components.ToggleField
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ClockInMethodScreen(
    snackbarHostState: SnackbarHostState,
    sharedViewModel: SharedViewModel,
    clockInMethodViewModel: ClockInMethodViewModel
) {

    val uiState by clockInMethodViewModel.uiState.collectAsState()

    LaunchedEffect(true) {
        sharedViewModel.setAppBarTitle("Clock-In Method")

        clockInMethodViewModel.isShowSnackbar.collectLatest {
            if (it) {
                snackbarHostState.showSnackbar(uiState.message)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        if (uiState.isLoadingData) {
            LoadingIndicator()
        } else {
            if (uiState.method == null) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "No config found")
                }
            } else {
                FullScreenLoading(
                    show = uiState.isSubmitting
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp)
                ) {
                    Text(
                        text = "Setting",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.DarkGray
                    )

                    Column(
                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        ToggleField(
                            desc = "WiFi Device",
                            checked = uiState.method?.wifiEnable ?: false,
                            onCheckedChange = {
                                clockInMethodViewModel.onEvent(ClockInMethodEvent.SwitchWifi(it))
                            }
                        )

                        ToggleField(
                            desc = "Bluetooth Device",
                            checked = uiState.method?.bleEnable ?: false,
                            onCheckedChange = {
                                clockInMethodViewModel.onEvent(ClockInMethodEvent.SwitchBLE(it))
                            }
                        )

                        Button(
                            onClick = {
                                clockInMethodViewModel.onEvent(ClockInMethodEvent.Submit)
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "Save")
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun ClockInMethodPreview() {

}