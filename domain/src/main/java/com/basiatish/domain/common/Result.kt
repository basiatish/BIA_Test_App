package com.basiatish.domain.common

import java.lang.Exception

sealed class Result<out R> {

    data class Success<out T>(val resultData: T) : Result<T>()

    data class Error(val exception: Exception) : Result<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$resultData]"
            is Error -> "Error[exception=$exception]"
        }
    }
}