package com.joaovitor.tucaprodutosdelimpeza.bluetooth

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat

class Bluetooth {
    fun checkPermission(context: Context): Boolean {
        val printerFunctions = PrinterFunctions(context)

        val hasPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_CONNECT
            ) == PackageManager.PERMISSION_GRANTED)
        } else {
            (true)
        }
        val oldPermissionCheck = printerFunctions.btAdapter != null && printerFunctions.btAdapter.isEnabled

        return hasPermission && oldPermissionCheck
    }
}