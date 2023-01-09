package com.example.qrcodescanner

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import com.example.qrcodescanner.databinding.ActivityMainBinding
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val tvQRContent = binding.tvContent
        val tvWifiInfo = binding.tvWifiInfo

        try {
            val barcodeEncoder = BarcodeEncoder()
            val bitmap = barcodeEncoder.encodeBitmap("Android Wifi Rssi Value", BarcodeFormat.QR_CODE, 400, 400)
            binding.qrCode.setImageBitmap(bitmap)
        } catch (e: Exception) {
        }

        // Register the launcher and result handler
        val barcodeLauncher = registerForActivityResult(
            ScanContract()
        ) { result: ScanIntentResult ->
            if (result.contents == null) {
                Toast.makeText(this@MainActivity, "Cancelled", Toast.LENGTH_LONG).show()
            } else {
                tvQRContent.text = result.contents
            }
        }

        binding.qrDecode.setOnClickListener {
            barcodeLauncher.launch(ScanOptions())
        }

        val wifiManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        } else {
            TODO("VERSION.SDK_INT < M")
        }
        val info = wifiManager.connectionInfo
        val ssid = info.ssid
        val rssi = info.rssi
        tvWifiInfo.text = info.toString()

        val result = wifiManager.scanResults.size
        Log.e("listWifiInfo", result.toString())


    }

}