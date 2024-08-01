package com.hkbufyp.hrms.di

import com.hkbufyp.hrms.util.Ktor
import org.koin.dsl.module

val networkModule = module {
    single { Ktor() }
}