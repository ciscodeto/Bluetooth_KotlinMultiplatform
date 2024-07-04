package com.example.testbluetooth.domain

import platform.CoreBluetooth.*
import platform.Foundation.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class BluetoothManager: NSObject(), CBCentralManagerDelegateProtocol, CBPeripheralDelegateProtocol {
    private lateinit var centralManager: CBCentralManager
    private val _scannedDevices = MutableStateFlow<List<CBPeripheral>>(emptyList())
    val scannedDevices: StateFlow<List<CBPeripheral>> = _scannedDevices.asStateFlow()
    private var connectedPeripheral: CBPeripheral? = null

    override fun init(): BluetoothManager {
        centralManager = CBCentralManager(delegate = this, queue = null)
        return this
    }

    fun startScan() {
        _scannedDevices.value = emptyList()
        centralManager.scanForPeripheralsWithServices(null, null)
    }

    fun stopScan() {
        centralManager.stopScan()
    }

    override fun centralManagerDidUpdateState(central: CBCentralManager) {
        if (central.state != CBManagerStatePoweredOn) {
            println("Bluetooth is not available.")
        }
    }

    override fun centralManager(
        central: CBCentralManager,
        didDiscoverPeripheral: CBPeripheral,
        advertisementData: Map<Any?, *>?,
        RSSI: NSNumber
    ) {
        if (!_scannedDevices.value.contains(didDiscoverPeripheral)) {
            _scannedDevices.value = _scannedDevices.value + didDiscoverPeripheral
        }
    }

    fun connectToDevice(peripheral: CBPeripheral) {
        connectedPeripheral = peripheral
        centralManager.connectPeripheral(peripheral, null)
    }

    override fun centralManager(
        central: CBCentralManager,
        didConnectPeripheral: CBPeripheral
    ) {
        didConnectPeripheral.delegate = this
        didConnectPeripheral.discoverServices(null)
    }

    override fun peripheral(
        peripheral: CBPeripheral,
        didDiscoverServices: NSError?
    ) {
        peripheral.services?.forEach { service ->
            peripheral.discoverCharacteristics(null, service as CBService)
        }
    }

    override fun peripheral(
        peripheral: CBPeripheral,
        didDiscoverCharacteristicsForService: CBService,
        error: NSError?
    ) {
        didDiscoverCharacteristicsForService.characteristics?.forEach { characteristic ->
            println("Discovered characteristic: ${(characteristic as CBCharacteristic).UUID}")
        }
    }
}