package com.hkbufyp.hrms.ui.components

import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.CryptoObject
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.hkbufyp.hrms.util.isEmptyOrBlank
import com.hkbufyp.hrms.util.toCalendar
import com.hkbufyp.hrms.util.toFormatString12
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

@Composable
fun LoadingIndicator(modifier: Modifier = Modifier, text: String? = null) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.secondary)
            Spacer(modifier = Modifier.height(20.dp))
            if (text != null)
                Text(text = text, textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun LoadingIndicatorForButton() {
    CircularProgressIndicator(
        color = Color.White,
        modifier = Modifier.size(ButtonDefaults.IconSize),
        strokeWidth = 2.dp
    )
}

@Composable
fun FullScreenLoading(message: String? = null, show: Boolean = true) {
    if (show) {
        Dialog(
            onDismissRequest = {  }
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator()

                if (message != null && !message.isEmptyOrBlank()) {
                    Text(
                        text = message,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenericDatePickerDialog(
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(selectableDates = object: SelectableDates {
        override fun isSelectableDate(utcTimeMillis: Long): Boolean {
            val current = Instant.ofEpochMilli(System.currentTimeMillis()).atZone(ZoneId.of("UTC")).toLocalDate()
            val target = Instant.ofEpochMilli(utcTimeMillis).atZone(ZoneId.of("UTC")).toLocalDate()
            return target.isEqual(current) || target.isAfter(current)
        }
    })

    val selectedDate = datePickerState.selectedDateMillis?.let {
        val dateTime = Instant.ofEpochMilli(it).atZone(ZoneId.of("UTC")).toLocalDateTime()
        dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    } ?: ""

    DatePickerDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            Button(onClick = {
                onConfirm(selectedDate)
                onDismiss()
            }
            ) {
                Text(text = "Confirm")
            }
        },
        dismissButton = {
            Button(onClick = { onDismiss() }) {
                Text(text = "Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@Composable
fun TimePickerDialog(
    title: String = "Select Time",
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
    toggle: @Composable () -> Unit = {},
    content: @Composable () -> Unit,
) {
    Dialog(
        onDismissRequest = onCancel,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ),
    ) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = 6.dp,
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .height(IntrinsicSize.Min)
                .background(
                    shape = MaterialTheme.shapes.extraLarge,
                    color = MaterialTheme.colorScheme.surface
                ),
        ) {
            toggle()
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    text = title,
                    style = MaterialTheme.typography.labelMedium
                )
                content()
                Row(
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(
                        onClick = onCancel
                    ) { Text("Cancel") }
                    TextButton(
                        onClick = onConfirm
                    ) { Text("OK") }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenericTimePickerDialog(
    initHour: Int = 8,
    initMinute: Int = 0,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val timePickerState = rememberTimePickerState(
        initialHour = initHour,
        initialMinute = initMinute,
        is24Hour = false
    )

    TimePickerDialog(
        onCancel = {
            onDismiss()
        },
        onConfirm = {
            onConfirm(timePickerState.toFormatString12())
            onDismiss()
        }
    ) {
        TimePicker(state = timePickerState)
    }
}

@Composable
fun LaunchBiometricPrompt(
    cryptoObject: CryptoObject,
    onSucceeded: (result: BiometricPrompt.AuthenticationResult) -> Unit,
    onError: (errorCode: Int, errString: CharSequence) -> Unit,
    onFailed: () -> Unit = {},
    finally: () -> Unit = {}
) {
    val context = LocalContext.current
    val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle("Biometric Authentication")
        .setSubtitle("-----")
        .setDescription("Authentication")
        .setNegativeButtonText("CANCEL")
        .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
        .build()

    val biometricPrompt = BiometricPrompt(
        context as FragmentActivity,
        ContextCompat.getMainExecutor(context),
        object: BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                onError(errorCode, errString)
                finally()
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                onSucceeded(result)
                finally()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                onFailed()
                finally()
            }
        }
    )

    biometricPrompt.authenticate(promptInfo, cryptoObject)
}

@Composable
fun ToggleField(
    desc: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    descModifier: Modifier = Modifier,
    toggleModifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .toggleable(
                value = checked,
                enabled = enabled,
                role = Role.Switch,
                onValueChange = { onCheckedChange(it) }
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = desc,
            style = MaterialTheme.typography.bodyLarge,
            modifier = descModifier.weight(1.0f)
        )
        Switch(
            checked = checked,
            onCheckedChange = null,
            enabled = enabled,
            modifier = toggleModifier
        )
    }
}