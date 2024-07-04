package com.example.bluetooth_kotlinmultiplatform.data

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import com.example.bluetooth_kotlinmultiplatform.domain.BluetoothDeviceDomain

@SuppressLint("MissingPermission")
fun BluetoothDevice.toBluetoothDeviceDomain(): BluetoothDeviceDomain {
    return BluetoothDeviceDomain(
        name = name,
        address = address
    )
}