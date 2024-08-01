package com.hkbufyp.hrms.util

import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.Navigator
import com.hkbufyp.hrms.ui.navigation.Screen
import io.ktor.client.HttpClient
import java.nio.charset.Charset
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

fun NavHostController.navigateWithPopup(
    route: String,
    fromRoute: String
) {
    navigate(route) {
        popUpTo(fromRoute) {
            inclusive = true
        }
        launchSingleTop = true
    }
}

fun NavHostController.navigate(
    screen: Screen
) {
    if (screen.sensitiveAuth) {
        navigate(Screen.SensitiveAuth.route.replace(
            oldValue = "{originalRoute}",
            newValue = screen.route
        ))
    } else {
        navigate(screen.route)
    }
}

fun NavHostController.navigateWithPopup(
    to: Screen,
    from: Screen
) {
    if (to.sensitiveAuth) {
        navigate(Screen.SensitiveAuth.route.replace(
            oldValue = "{originalRoute}",
            newValue = to.route
        ))
    } else {
        navigateWithPopup(to.route, from.route)
    }
}

fun Boolean.toInt() = if (this) 1 else 0

fun Int.toBoolean() = this != 0

fun String.hash(): String {
    return MessageDigest.getInstance("SHA-512")
        .digest(this.toByteArray(Charset.forName("UTF-8")))
        .fold("") {
                str, it -> str + "%02x".format(it)
        }
}

fun String.isEmptyOrBlank() = isEmpty() || isBlank()

fun Calendar.toFormatStringByTime12(): String {
    val formatter =  SimpleDateFormat("hh:mm a", Locale.getDefault())
    return formatter.format(this.time)
}

fun Calendar.toFormatStringByTime24(): String {
    val formatter =  SimpleDateFormat("HH:mm", Locale.getDefault())
    return formatter.format(this.time)
}

@OptIn(ExperimentalMaterial3Api::class)
fun TimePickerState.toCalendar(): Calendar {
    val cal = Calendar.getInstance()

    cal.set(Calendar.HOUR_OF_DAY, this.hour)
    cal.set(Calendar.MINUTE, this.minute)
    cal.isLenient = false

    return cal
}

@OptIn(ExperimentalMaterial3Api::class)
fun TimePickerState.toFormatString12(): String =
    this.toCalendar().toFormatStringByTime12()

@OptIn(ExperimentalMaterial3Api::class)
fun TimePickerState.toFormatString24(): String =
    this.toCalendar().toFormatStringByTime24()

fun String.toCalendar12(): Calendar {
    val formatter = SimpleDateFormat("hh:mm a", Locale.getDefault())
    val date = formatter.parse(this)
    val calendar = Calendar.getInstance()

    if (date != null) {
        calendar.time = date
    }

    return calendar
}

fun String.toCalendar24(): Calendar {
    val formatter = SimpleDateFormat("hh:mm", Locale.getDefault())
    val date = formatter.parse(this)
    val calendar = Calendar.getInstance()

    if (date != null) {
        calendar.time = date
    }

    return calendar
}

fun Calendar.getStartOfWeek() =
    this.apply {
        firstDayOfWeek = Calendar.SUNDAY
        set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
    }

fun Calendar.getEndOfWeek() =
    this.apply {
        firstDayOfWeek = Calendar.SUNDAY
        set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY)
    }

fun Calendar.toFormatStringByDate(): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return formatter.format(this.time)
}

fun String.isToday(): Boolean {
    val cal = Calendar.getInstance()
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val today = formatter.format(cal.time)

    return today == this
}