package com.joaovitor.tucaprodutosdelimpeza.data

import com.google.firebase.crashlytics.FirebaseCrashlytics

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class Result<out T : Any> {

    data class Success<out T : Any>(val data: T?) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>() {
        init {
            FirebaseCrashlytics.getInstance().recordException(exception)
        }
    }

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
        }
    }
}