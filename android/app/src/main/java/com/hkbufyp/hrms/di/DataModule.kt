package com.hkbufyp.hrms.di

import androidx.biometric.BiometricManager
import com.hkbufyp.hrms.data.remote.api.AnnouncementService
import com.hkbufyp.hrms.data.remote.api.AttendService
import com.hkbufyp.hrms.data.remote.api.BleDeviceService
import com.hkbufyp.hrms.data.remote.api.DepartmentService
import com.hkbufyp.hrms.data.remote.api.FCMService
import com.hkbufyp.hrms.data.remote.api.RoleService
import com.hkbufyp.hrms.data.remote.api.UserService
import com.hkbufyp.hrms.data.remote.api.LeaveService
import com.hkbufyp.hrms.data.remote.api.LogService
import com.hkbufyp.hrms.data.remote.api.WifiDeviceService
import com.hkbufyp.hrms.data.repository.AnnouncementRepositoryImpl
import com.hkbufyp.hrms.data.repository.AttendRepositoryImpl
import com.hkbufyp.hrms.data.repository.BiometricRepositoryImpl
import com.hkbufyp.hrms.data.repository.BleRepositoryImpl
import com.hkbufyp.hrms.data.repository.DepartmentRepositoryImpl
import com.hkbufyp.hrms.data.repository.FCMRepositoryImpl
import com.hkbufyp.hrms.data.repository.RoleRepositoryImpl
import com.hkbufyp.hrms.data.repository.UserRepositoryImpl
import com.hkbufyp.hrms.data.repository.LeaveRepositoryImpl
import com.hkbufyp.hrms.data.repository.LogRepositoryImpl
import com.hkbufyp.hrms.data.repository.UserPreferencesRepositoryImpl
import com.hkbufyp.hrms.data.repository.WifiRepositoryImpl
import com.hkbufyp.hrms.domain.repository.AnnouncementRepository
import com.hkbufyp.hrms.domain.repository.AttendRepository
import com.hkbufyp.hrms.domain.repository.BiometricRepository
import com.hkbufyp.hrms.domain.repository.BleRepository
import com.hkbufyp.hrms.domain.repository.DepartmentRepository
import com.hkbufyp.hrms.domain.repository.FCMRepository
import com.hkbufyp.hrms.domain.repository.RoleRepository
import com.hkbufyp.hrms.domain.repository.UserRepository
import com.hkbufyp.hrms.domain.repository.LeaveRepository
import com.hkbufyp.hrms.domain.repository.LogRepository
import com.hkbufyp.hrms.domain.repository.UserPreferencesRepository
import com.hkbufyp.hrms.domain.repository.WifiRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {
    single { UserService(get()) }
    single { RoleService(get()) }
    single { DepartmentService(get()) }
    single { LeaveService(get()) }
    single { FCMService(get()) }
    single { AnnouncementService(get()) }
    single { WifiDeviceService(get()) }
    single { AttendService(get()) }
    single { LogService(get()) }
    single { BleDeviceService(get()) }
    single<UserRepository> { UserRepositoryImpl(get()) }
    single<RoleRepository> { RoleRepositoryImpl(get()) }
    single<DepartmentRepository> { DepartmentRepositoryImpl(get()) }
    single<LeaveRepository> { LeaveRepositoryImpl(get()) }
    single<FCMRepository> { FCMRepositoryImpl(get()) }
    single<UserPreferencesRepository> { UserPreferencesRepositoryImpl(get()) }
    single<AnnouncementRepository> { AnnouncementRepositoryImpl(get()) }
    single<BiometricRepository> { BiometricRepositoryImpl(BiometricManager.from(androidContext()), get(), get()) }
    single<WifiRepository> { WifiRepositoryImpl(androidContext(), get()) }
    single<AttendRepository> { AttendRepositoryImpl(get()) }
    single<BleRepository> { BleRepositoryImpl(androidContext(), get()) }
    single<LogRepository> { LogRepositoryImpl(get()) }
}