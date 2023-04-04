package com.example.wifipositioning

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.os.Handler
import android.util.Log

class WifiScanner(private val context: Context) : BroadcastReceiver() {

    private val wifiManager: WifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    private val handler = Handler()
    private val runnable: Runnable = object : Runnable {
        override fun run() {
            wifiManager.startScan()
            handler.postDelayed(this, 3000) // Scan every 3 seconds
        }
    }

    fun startScanning() {
        handler.post(runnable)
    }

    fun stopScanning() {
        handler.removeCallbacks(runnable)
    }

    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context, intent: Intent) {
        if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION == intent.action) {
            val results = wifiManager.scanResults
            for (result in results) {
                val ssid: String = result.BSSID
                val rssi: Int = result.level
                Log.d("AAA", "$ssid  ---  $rssi")
                // Do something with the SSID and RSSI
            }
        }
    }
}