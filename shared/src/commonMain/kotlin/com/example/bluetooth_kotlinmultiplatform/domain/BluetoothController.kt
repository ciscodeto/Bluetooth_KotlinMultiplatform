package com.example.bluetooth_kotlinmultiplatform.domain

import kotlinx.coroutines.flow.StateFlow

interface BluetoothController{
    val scannedDevices: StateFlow<List<BluetoothDevice>>
    val pairedDevices: StateFlow<List<BluetoothDevice>>

    fun startDiscovery()
    fun stopDiscovery()

    fun release()
}

expect fun getBluetoothControlller(context: ApplicationContext): BluetoothController
