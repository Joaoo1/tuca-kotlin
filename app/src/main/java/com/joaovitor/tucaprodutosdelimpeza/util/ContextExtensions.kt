package com.joaovitor.tucaprodutosdelimpeza.util

import android.content.Context
import android.widget.Toast

fun Context.toast(message: CharSequence) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

fun Context.toastLong(message: CharSequence) =
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()