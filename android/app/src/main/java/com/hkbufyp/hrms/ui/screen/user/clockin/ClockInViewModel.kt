package com.hkbufyp.hrms.ui.screen.user.clockin

import android.bluetooth.le.ScanResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hkbufyp.hrms.data.remote.dto.attendance.AttendTimeslotWeeklyPayload
import com.hkbufyp.hrms.data.remote.dto.attendance.GetAttendanceRecordPayload
import com.hkbufyp.hrms.data.remote.dto.attendance.TakeAttendancePayload
import com.hkbufyp.hrms.domain.model.AttendMethod
import com.hkbufyp.hrms.domain.model.AttendTimeslotDate
import com.hkbufyp.hrms.domain.model.BleDevice
import com.hkbufyp.hrms.domain.model.WifiDevice
import com.hkbufyp.hrms.domain.repository.AttendRepository
import com.hkbufyp.hrms.domain.repository.BiometricRepository
import com.hkbufyp.hrms.domain.repository.BleRepository
import com.hkbufyp.hrms.domain.repository.WifiRepository
import com.hkbufyp.hrms.util.NetworkResponse
import com.hkbufyp.hrms.util.getEndOfWeek
import com.hkbufyp.hrms.util.getStartOfWeek
import com.hkbufyp.hrms.util.isEmptyOrBlank
import com.hkbufyp.hrms.util.toFormatStringByDate
import com.hkbufyp.hrms.util.toInt
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar

class ClockInViewModel(
    private val wifiRepository: WifiRepository,
    private val bleRepository: BleRepository,
    private val biometricRepository: BiometricRepository,
    private val attendRepository: AttendRepository,
    private val employeeId: String
): ViewModel() {

    private val _uiState = MutableStateFlow(ClockInUiState())
    val uiState = _uiState.asStateFlow()

    private val _isShowSnackbar = MutableSharedFlow<Boolean>()
    val isShowSnackbar = _isShowSnackbar.asSharedFlow()

    private val startCalendar = Calendar.getInstance().getStartOfWeek()
    private val endCalendar = Calendar.getInstance().getEndOfWeek()
    private var _methodConfig: AttendMethod? = null
    private var _wifiDevices: List<WifiDevice> = emptyList()
    private var _bleDevices: List<BleDevice> = emptyList()
    private var _scannedBle: MutableMap<String, ScanResult> = mutableMapOf()
    private var _foundWifiDevice: WifiDevice? = null
    private var _foundBleDevice: BleDevice? = null
    private var _isAvailableForClockIn: Boolean = true
    private var _isScanningBle: Boolean = false

    init {
        initBiometric()
        _uiState.update { state ->
            state.copy(
                startOfWeek = startCalendar.getStartOfWeek().toFormatStringByDate(),
                endOfWeek = endCalendar.getEndOfWeek().toFormatStringByDate()
            )
        }
        fetchData()
    }

    fun onEvent(event: ClockInEvent) {
        when (event) {
            is ClockInEvent.ClockIn -> clockInOut(event.slot, true)
            is ClockInEvent.ClockOut -> clockInOut(event.slot, false)
            ClockInEvent.Previous -> prevWeek()
            ClockInEvent.Next -> nextWeek()
        }
    }

    private fun initBiometric() {
        viewModelScope.launch {
            biometricRepository.getBiometricStatus().collect {
                _uiState.update { state ->
                    state.copy(
                        biometricStatus = it,
                        shouldShowBiometricAlert = it.isKeyInvalidated() && it.isBiometricAvailable
                    )
                }
            }

            if (_uiState.value.biometricStatus?.canAuthByBiometric() == true) {
                _uiState.update { state ->
                    state.copy(
                        cryptoObject = biometricRepository.getCryptoObject(false)
                    )
                }
            }
        }
    }

    private fun fetchData() {
        _uiState.update { state ->
            state.copy(isLoading = true)
        }

        var errMessage = ""

        viewModelScope.launch {
            scanBle()
        }

        viewModelScope.launch {
            try {
                fetchMethod()
                fetchWifi()
                fetchBle()
                fetchTimeslot()
                fetchAttendRecord()
            } catch (e: Exception) {
                errMessage = e.message ?: "Unknown exception."
            }

            _uiState.update { state ->
                state.copy(
                    isLoading = false,
                    message = errMessage
                )
            }

            if (!errMessage.isEmptyOrBlank()) {
                _isShowSnackbar.emit(true)
            }
        }
    }

    private suspend fun fetchAttendRecord() {
        attendRepository.getAttendanceRecord(
            employeeId = employeeId,
            payload = GetAttendanceRecordPayload(
                startDate = _uiState.value.startOfWeek,
                endDate = _uiState.value.endOfWeek
            )
        ).catch { exception ->
            throw Exception(exception.message)
        }.collect { response ->
            when (response) {
                is NetworkResponse.Success -> {
                    _uiState.update { state ->
                        state.copy(attendRecord = response.data)
                    }
                }
                is NetworkResponse.Failure -> {
                    throw Exception(response.errMessage)
                }
            }
        }
    }

    private suspend fun fetchMethod() {
        attendRepository.getMethod().catch { exception ->
            throw Exception(exception.message)
        }.collect { response ->
            when (response) {
                is NetworkResponse.Success -> {
                    _methodConfig = response.data
                }
                is NetworkResponse.Failure -> {
                    throw Exception(response.errMessage)
                }
            }
        }
    }

    private suspend fun fetchWifi() {
        wifiRepository.getWifiDevices().catch { exception ->
            throw Exception(exception.message)
        }.collect { response ->
            when (response) {
                is NetworkResponse.Success -> {
                    _wifiDevices = response.data ?: emptyList()
                }
                is NetworkResponse.Failure -> {
                    throw Exception(response.errMessage)
                }
            }
        }
    }

    private suspend fun fetchBle() {
        bleRepository.getBleDevices().catch { exception ->
            throw Exception(exception.message)
        }.collect { response ->
            when (response) {
                is NetworkResponse.Success -> {
                    _bleDevices = response.data ?: emptyList()
                }
                is NetworkResponse.Failure -> {
                    throw Exception(response.errMessage)
                }
            }
        }
    }

    private fun checkWifiStat() {
        val info = wifiRepository.getCurrentConnectedWifi()
        if (info != null) {
            _foundWifiDevice = _wifiDevices.find { it.bssid == info.bssid }
        }
    }

    private fun checkBleStat() {
        _bleDevices.forEach { device ->
            if (_scannedBle[device.mac] != null) {
                _foundBleDevice = device
            }
        }
    }

    private suspend fun fetchTimeslot() {
        attendRepository.getWeeklyTimeslotByEmployeeId(
            id = employeeId,
            payload = AttendTimeslotWeeklyPayload(
                startDate = _uiState.value.startOfWeek,
                endDate = _uiState.value.endOfWeek
            )
        ).catch { exception ->
            throw Exception(exception.message)
        }.collect { response ->
            when (response) {
                is NetworkResponse.Success -> {
                    _uiState.update { state ->
                        state.copy(slots = response.data ?: emptyList())
                    }
                    println(response.data)
                }
                is NetworkResponse.Failure -> {
                    throw Exception(response.errMessage)
                }
            }
        }
    }

    private fun scanBle() {
        _isScanningBle = true

        viewModelScope.launch {
            bleRepository.scanBLE().collect { result ->
                if (result != null) {
                    _scannedBle[result.device.address] = result
                } else {
                    _isScanningBle = false
                }
            }
        }
    }

    private fun clockInOut(slot: AttendTimeslotDate, isIn: Boolean) {
        if (_methodConfig == null) {
            _uiState.update { state ->
                state.copy(
                    message = "Unable to get the config of take attendance"
                )
            }

            viewModelScope.launch {
                _isShowSnackbar.emit(true)
            }

            return
        }

        _uiState.update { state ->
            state.copy(isSubmitting = true)
        }

        viewModelScope.launch {
            while (_isScanningBle) {
                delay(100)
            }

            if (_methodConfig!!.wifiEnable) {
                checkWifiStat()
                if (_foundWifiDevice == null) {
                    _uiState.update { state ->
                        state.copy(
                            isSubmitting = false,
                            message = "Please connect the authorized device"
                        )
                    }

                    _isShowSnackbar.emit(true)

                    cancel()
                }
            }

            if (_methodConfig!!.bleEnable) {
                checkBleStat()
                if (_foundBleDevice == null) {
                    _uiState.update { state ->
                        state.copy(
                            isSubmitting = false,
                            message = "Please found the ble device"
                        )
                    }

                    _isShowSnackbar.emit(true)

                    cancel()
                }
            }


            attendRepository.takeAttendance(
                payload = TakeAttendancePayload(
                    attendDate = slot.date,
                    device = if (_foundWifiDevice != null) _foundWifiDevice!!.bssid else if (_foundBleDevice != null) _foundBleDevice!!.mac else "",
                    actionType = isIn.toInt(),
                    timeslotId = slot.id
                )
            ).catch { exception ->
                _uiState.update { state ->
                    state.copy(
                        isSubmitting = false,
                        message = exception.message ?: ""
                    )
                }
                _isShowSnackbar.emit(true)
            }.collect { response ->
                when (response) {
                    is NetworkResponse.Success -> {
                        _uiState.update { state ->
                            state.copy(
                                isSubmitting = false,
                                message = "Success"
                            )
                        }

                        fetchDataAfterSwitch()
                    }
                    is NetworkResponse.Failure -> {
                        _uiState.update { state ->
                            state.copy(
                                isSubmitting = false,
                                message = response.errMessage ?: ""
                            )
                        }
                    }
                }
                _isShowSnackbar.emit(true)
            }
        }
    }

    private fun fetchDataAfterSwitch() {
        _uiState.update { state ->
            state.copy(isLoading = true)
        }

        var errMessage = ""

        viewModelScope.launch {
            try {
                fetchTimeslot()
                fetchAttendRecord()
            } catch (e: Exception) {
                errMessage = e.message ?: "Unknown exception."
            }

            _uiState.update { state ->
                state.copy(
                    isLoading = false,
                    message = errMessage
                )
            }

            if (!errMessage.isEmptyOrBlank()) {
                _isShowSnackbar.emit(true)
            }
        }
    }

    private fun prevWeek() {
        val  currentWeek = startCalendar.get(Calendar.WEEK_OF_YEAR)
        startCalendar.set(Calendar.WEEK_OF_YEAR, currentWeek - 1)
        endCalendar.set(Calendar.WEEK_OF_YEAR, currentWeek - 1)

        _uiState.update { state ->
            state.copy(
                startOfWeek = startCalendar.getStartOfWeek().toFormatStringByDate(),
                endOfWeek = endCalendar.getEndOfWeek().toFormatStringByDate()
            )
        }

        fetchDataAfterSwitch()
    }

    private fun nextWeek() {
        val  currentWeek = startCalendar.get(Calendar.WEEK_OF_YEAR)
        startCalendar.set(Calendar.WEEK_OF_YEAR, currentWeek + 1)
        endCalendar.set(Calendar.WEEK_OF_YEAR, currentWeek + 1)

        _uiState.update { state ->
            state.copy(
                startOfWeek = startCalendar.getStartOfWeek().toFormatStringByDate(),
                endOfWeek = endCalendar.getEndOfWeek().toFormatStringByDate()
            )
        }

        fetchDataAfterSwitch()
    }
}