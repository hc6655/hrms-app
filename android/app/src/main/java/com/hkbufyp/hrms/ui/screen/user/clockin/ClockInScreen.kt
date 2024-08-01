package com.hkbufyp.hrms.ui.screen.user.clockin

import android.annotation.SuppressLint
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hkbufyp.hrms.domain.model.isClockedIn
import com.hkbufyp.hrms.domain.model.isClockedOut
import com.hkbufyp.hrms.domain.model.isEarlyLeave
import com.hkbufyp.hrms.domain.model.isLate
import com.hkbufyp.hrms.domain.model.isOvertime
import com.hkbufyp.hrms.shared.SharedViewModel
import com.hkbufyp.hrms.ui.components.FullScreenLoading
import com.hkbufyp.hrms.ui.components.LoadingIndicator
import com.hkbufyp.hrms.util.getEndOfWeek
import com.hkbufyp.hrms.util.getStartOfWeek
import com.hkbufyp.hrms.util.isEmptyOrBlank
import com.hkbufyp.hrms.util.isToday
import com.hkbufyp.hrms.util.toCalendar12
import com.hkbufyp.hrms.util.toFormatStringByDate
import com.hkbufyp.hrms.util.toFormatStringByTime12
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import kotlin.random.Random
import kotlin.random.nextInt

@SuppressLint("SimpleDateFormat")
@Composable
fun ClockInScreen(
    snackbarHostState: SnackbarHostState,
    sharedViewModel: SharedViewModel,
    clockInViewModel: ClockInViewModel
) {
    val uiState by clockInViewModel.uiState.collectAsState()

    LaunchedEffect(true) {
        sharedViewModel.setAppBarTitle("Clock-in")

        launch {
            clockInViewModel.isShowSnackbar.collectLatest {
                if (it) {
                    snackbarHostState.showSnackbar(uiState.message)
                }
            }
        }
    }

    Box (
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        FullScreenLoading(
            show = uiState.isSubmitting
        )

        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column (
                    horizontalAlignment = Alignment.Start
                ) {
                    IconButton(
                        onClick = {
                            clockInViewModel.onEvent(ClockInEvent.Previous)
                        }
                    ) {
                        Icon(imageVector = Icons.Filled.ArrowBackIosNew, contentDescription = "Left")
                    }
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(top = 14.dp)
                ) {
                    Text(
                        text = "${uiState.startOfWeek} - ${uiState.endOfWeek}",
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                Column (
                    horizontalAlignment = Alignment.End
                ) {
                    IconButton(
                        onClick = {
                            clockInViewModel.onEvent(ClockInEvent.Next)
                        }
                    ) {
                        Icon(imageVector = Icons.Filled.ArrowForwardIos, contentDescription = "Left")
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (uiState.isLoading) {
                LoadingIndicator()
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(uiState.slots) { slot ->
                        val attendRecord = uiState.attendRecord?.brief?.find { it.date == slot.date }

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp),
                            shape = RoundedCornerShape(10.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column (
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Row (
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Shift: ${slot.name}",
                                    )

                                    Text(
                                        text = slot.date,
                                    )
                                }

                                Text(
                                    text = "Time: ${slot.start} - ${slot.end}"
                                )

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "IN: ",
                                        modifier = Modifier.fillMaxWidth(0.2f)
                                    )

                                    if (attendRecord?.isClockedIn() == true) {
                                        Text(text = attendRecord.inTime)
                                    } else {
                                        Button(
                                            onClick = {
                                                clockInViewModel.onEvent(ClockInEvent.ClockIn(slot))
                                            },
                                            shape = RectangleShape,
                                            modifier = Modifier.size(height = 35.dp, width = 150.dp),
                                            enabled = slot.date.isToday()
                                        ) {
                                            Text(text = "Clock-In")
                                        }
                                    }

                                    if (attendRecord?.isLate() == true) {
                                        Column (
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalAlignment = Alignment.End
                                        ) {
                                            Text(
                                                text = "LATE",
                                                color = Color.Red
                                            )
                                        }
                                    }
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "OUT: ",
                                        modifier = Modifier.fillMaxWidth(0.2f)
                                    )

                                    if (attendRecord?.isClockedOut() == true) {
                                        Text(text = attendRecord.outTime)
                                    } else {
                                        Button(
                                            onClick = {
                                                clockInViewModel.onEvent(ClockInEvent.ClockOut(slot))
                                            },
                                            shape = RectangleShape,
                                            modifier = Modifier.size(height = 35.dp, width = 150.dp),
                                            enabled = slot.date.isToday() && attendRecord?.isClockedIn() == true
                                        ) {
                                            Text(text = "Clock-Out")
                                        }
                                    }

                                    if (attendRecord?.isEarlyLeave() == true) {
                                        Column (
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalAlignment = Alignment.End
                                        ) {
                                            Text(
                                                text = "EARLY LEAVE",
                                                color = Color.Red
                                            )
                                        }
                                    } else if (attendRecord?.isOvertime() == true) {
                                        Column (
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalAlignment = Alignment.End
                                        ) {
                                            Text(
                                                text = "OT",
                                                color = Color.Red
                                            )
                                        }
                                    }

                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = "Late: %.2f".format(attendRecord?.lateHour))
                                    Text(text = "OT: %.2f".format(attendRecord?.overtimeHour))
                                    Text(text = "Early Leave: %.2f".format(attendRecord?.earlyLeaveHour))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun ClockInPreview() {

}