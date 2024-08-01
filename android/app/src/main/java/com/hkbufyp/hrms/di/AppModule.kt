package com.hkbufyp.hrms.di

import org.koin.dsl.module

val appModule = module {
    includes(
        networkModule,
        dataModule,
        viewModelModule,
    )
}