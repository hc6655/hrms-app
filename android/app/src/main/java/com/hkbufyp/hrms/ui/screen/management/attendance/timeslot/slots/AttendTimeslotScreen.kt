package com.hkbufyp.hrms.ui.screen.management.attendance.timeslot.slots

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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hkbufyp.hrms.domain.enums.isPending
import com.hkbufyp.hrms.domain.enums.toStr
import com.hkbufyp.hrms.shared.SharedViewModel
import com.hkbufyp.hrms.ui.components.LoadingIndicator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendTimeslotScreen(
    snackbarHostState: SnackbarHostState,
    sharedViewModel: SharedViewModel,
    attendTimeslotViewModel: AttendTimeslotViewModel,
    onAddClicked: () -> Unit,
    onSlotClicked: (Int) -> Unit,
    onNavigateBackClicked: () -> Unit
) {

    val uiState by attendTimeslotViewModel.uiState.collectAsState()

    LaunchedEffect(true) {
        sharedViewModel.isShowAppBar(false)
        attendTimeslotViewModel.onEvent(AttendTimeslotEvent.Enter)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Timeslots") },
                navigationIcon = {
                    IconButton(onClick = { onNavigateBackClicked() }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { onAddClicked() }) {
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
            if (uiState.isLoadingData) {
                LoadingIndicator()
            } else if (uiState.timeslots == null || uiState.timeslots?.isEmpty() == true) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "No record.")
                }
            } else {
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize(),
                ) {
                    items(uiState.timeslots!!, itemContent = {
                        Card(
                            onClick = { onSlotClicked(it.id) },
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
                                    .padding(start = 20.dp, end = 20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(text = it.name)
                                Text(text = it.slots[0].start + " - " + it.slots[0].end)
                            }
                        }
                        Divider()
                    })
                }
            }
        }
    }
}