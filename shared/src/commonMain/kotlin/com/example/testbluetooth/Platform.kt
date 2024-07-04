package com.example.testbluetooth

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform