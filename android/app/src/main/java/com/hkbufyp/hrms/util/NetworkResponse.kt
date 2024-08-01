package com.hkbufyp.hrms.util

sealed class NetworkResponse<T> {
    data class Success<T>(val data: T? = null): NetworkResponse<T>()
    data class Failure<T>(val errMessage: String? = null): NetworkResponse<T>()
}