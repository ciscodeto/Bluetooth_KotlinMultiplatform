import SwiftUI
import shared

class BluetoothManagerWrapper: ObservableObject {
    private var bluetoothManager: BluetoothManager

    @Published var scannedDevices: [CBPeripheral] = []

    init() {
        bluetoothManager = BluetoothManager().init()
        bluetoothManager.scannedDevices.collect { devices in
            self.scannedDevices = devices as! [CBPeripheral]
        } completionHandler: { (throwable) in
            print("Error collecting scanned devices: \(throwable.localizedDescription)")
        }
    }

    func startScan() {
        bluetoothManager.startScan()
    }

    func stopScan() {
        bluetoothManager.stopScan()
    }
}