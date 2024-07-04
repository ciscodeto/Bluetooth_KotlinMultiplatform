package com.example.testbluetooth.android


import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.example.testbluetooth.domain.AndroidBluetoothLeController
import com.example.testbluetooth.domain.AndroidBluetoothLeControllerFactory
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat

class MainActivity : ComponentActivity() {

    private lateinit var androidBluetoothLeController: AndroidBluetoothLeController

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.BLUETOOTH_CONNECT] == true &&
            permissions[Manifest.permission.BLUETOOTH_SCAN] == true &&
            permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true) {
            // Permissions granted, proceed with Bluetooth operations
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        androidBluetoothLeController = ViewModelProvider(this, AndroidBluetoothLeControllerFactory(this))
            .get(AndroidBluetoothLeController::class.java)

        requestPermissionsIfNecessary()

        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BluetoothScanScreen(androidBluetoothLeController)
                }
            }
        }
    }

    private fun requestPermissionsIfNecessary() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_SCAN
            ) != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            )
        }
    }
}

@Composable
fun BluetoothScanScreen(androidBluetoothLeController: AndroidBluetoothLeController) {
    val scannedDevices by androidBluetoothLeController.scannedDevices.collectAsState()
    val context = LocalContext.current

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Button(onClick = { androidBluetoothLeController.startScan() }) {
            Text("Start Scan")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { androidBluetoothLeController.stopScan() }) {
            Text("Stop Scan")
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn {
            items(scannedDevices) { device ->
                val deviceName = if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.BLUETOOTH_CONNECT
                    ) == PackageManager.PERMISSION_GRANTED) {
                    device.name ?: "Unknown Device"
                } else {
                    "Permission required to access device name"
                }
                Text(text = "$deviceName (${device.address})")
            }
        }
    }
}

@Preview
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        val context = LocalContext.current
        val dummyAndroidBluetoothLeController = AndroidBluetoothLeController(context)
        BluetoothScanScreen(androidBluetoothLeController = dummyAndroidBluetoothLeController)
    }
}