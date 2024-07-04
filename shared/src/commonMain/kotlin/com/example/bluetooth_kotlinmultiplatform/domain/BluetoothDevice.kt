package com.example.bluetooth_kotlinmultiplatform.data

typealias BluetoothDeviceDomain = BluetoothDevice

data class BluetoothDevice(
    val name: String?,
    val address: String
)