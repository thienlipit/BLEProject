package com.example.wifipositioning

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.CombinedChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import java.util.*


class MainActivity : AppCompatActivity() {
    lateinit var receiver: WifiScanner
    /*private var requestBluetoothOn =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                //granted
                Log.d("TAG", "Bluetooth is ON")
            } else {
                //deny
                Log.d("TAG", "Bluetooth is OFF")
            }
        }*/

    @RequiresApi(Build.VERSION_CODES.N)
    val requestMultiplePermissionsAPI30Below =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
                Log.d("List Permissions: ", "${it.key} = ${it.value}")
            }
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, true) -> {
                    // Precise location access granted.
                }
                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, true) -> {
                    // Only approximate location access granted.
                }
                else -> {
                    // No location access granted.
                }
            }
        }
    private val requestMultiplePermissionsAPI31Above =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
                Log.d("List Permissions: ", "${it.key} = ${it.value}")
            }
        }

    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestAllPermission()
        val combinedChart = findViewById<CombinedChart>(R.id.combinedChart)
        combinedChart(combinedChart)

        val lineChart = findViewById<LineChart>(R.id.lineChart1)
        lineChart(lineChart)



        receiver = WifiScanner(this)
        val hasWifiRTT = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            applicationContext.packageManager.hasSystemFeature(PackageManager.FEATURE_WIFI_RTT)
        } else {
            TODO("VERSION.SDK_INT < P")
        }
        Log.d("Wifi RTT", hasWifiRTT.toString())

        val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
//        wifiManager.scanResults


        val currentConnection = wifiManager.connectionInfo
        Log.d(TAG, currentConnection.toString())


/*        val results: List<ScanResult> = wifiManager.scanResults

        for (result in results) {
            // Get the RSSI and MAC address of the WiFi AP
            val rssi: Int = result.level
            val macAddress: String = result.BSSID

            // Calculate the distance between the device and the WiFi AP
            val distance = calculateDistance(rssi, result.frequency)

            val wf = result.toString()
            Log.d("TAG", "Distance: $distance WiFi: $wf")

        }*/

/*        val SERVICE_UUID = "0000FEAA-0000-1000-8000-00805F9B34FB"
        val AOADATA_UUID = byteArrayOf(
            0xBF.toByte(),
            0x24.toByte(),
            0x1F.toByte(),
            0x16.toByte(),
            0x80.toByte(),
            0x02.toByte(),
            0x00.toByte(),
            0x00.toByte(),
            0x00.toByte(),
            0x00.toByte(),
            0x09.toByte(),
            0x00.toByte(),
            0x03.toByte(),
            0x19.toByte(),
            0xC1.toByte()
        )

        val scanner = BluetoothAdapter.getDefaultAdapter().bluetoothLeScanner

        val settings = ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .build()

        val scanCallback: ScanCallback = object : ScanCallback() {
            override fun onScanResult(callbackType: Int, result: android.bluetooth.le.ScanResult) {
                val scanRecord: ScanRecord = result.scanRecord!!
                if (result.device.address.equals("DE:79:8D:06:0C:AC")){
                    Log.d("aa", result.device.address)
                    val testServicesUUID = "955a1523-0fe2-f5aa-a094-84b8d4f3e8ad"


                    val aa = ParcelUuid.fromString(SERVICE_UUID)
                    Log.d("bb", aa.toString())

                    val serviceData = scanRecord.getServiceData(ParcelUuid.fromString(testServicesUUID))
                    Log.d("ServicesDATA", serviceData.toString())
                    val a = ParcelUuid.fromString(SERVICE_UUID)
                    Log.d("ServiceUUID", a.toString())


//                    val serviceData = scanRecord.getServiceData(ParcelUuid.fromString(SERVICE_UUID))

                }




//                if (scanRecord != null && scanRecord.serviceUuids != null && scanRecord.serviceUuids.contains(
//                        ParcelUuid.fromString(SERVICE_UUID))) {
//                    val serviceData = scanRecord.getServiceData(ParcelUuid.fromString(SERVICE_UUID))
//                    if (serviceData != null && Arrays.equals(
//                            Arrays.copyOfRange(serviceData, 0, 15),
//                            AOADATA_UUID)) {
//                        val aoa: ByteArray = Arrays.copyOfRange(serviceData, 15, 17)
//                        val aod: ByteArray = Arrays.copyOfRange(serviceData, 17, 19)
//                        // Do something with the AoA and AoD information
//                        Log.d("AOA", aoa.toString())
//                        Log.d("AOD", aod.toString())
//                    }
//                }


            }
        }

        scanner.startScan(null, settings, scanCallback)

        // Use the location of the WiFi AP to estimate the location and direction of the WiFi source
//          val wifiLocation = Location("")
//          wifiLocation.setLatitude(result.latitude)
//          wifiLocation.setLongitude(result.longitude)
//          val bearing: Float = myLocation.bearingTo(wifiLocation)
//          Log.d("TAG", "Direction of WiFi signal: $bearing")*/
    }

    private fun dataChart(): LineDataSet {
        val data = LineData()
        val dataValues = intArrayOf(55, 59, 50, 54, 58)


        val entries = ArrayList<Entry>()

        for (index in dataValues.indices) {
            entries.add(Entry(index.toFloat(), dataValues[index].toFloat()))
        }

        val set = LineDataSet(entries, "Request Ots approved")
        set.color = Color.GREEN
        set.lineWidth = 2.5f
//        set.circleColor = Color.GREEN
        set.circleRadius = 5f
        set.fillColor = Color.GREEN
        set.mode = LineDataSet.Mode.CUBIC_BEZIER
        set.setDrawValues(true)
        set.valueTextSize = 10f
        set.valueTextColor = Color.GREEN

        set.axisDependency = YAxis.AxisDependency.LEFT
//        data.addDataSet(set)

        return set
    }


    override fun onResume() {
        super.onResume()
        receiver.startScanning()
        val filter = IntentFilter()
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
        registerReceiver(receiver, filter)
    }

    override fun onStop() {
        super.onStop()
        receiver.stopScanning()
        unregisterReceiver(receiver)
    }


    private fun calculateDistance(rssi: Int, frequency: Int): Double {
        val exp = (27.55 - 20 * Math.log10(frequency.toDouble()) + Math.abs(rssi)) / 20.0
        return Math.pow(10.0, exp)
    }

    companion object {
        const val TAG = "MainActivity"
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun requestAllPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Log.d("SDK", ">=31")
            requestMultiplePermissionsAPI31Above.launch(
                arrayOf(
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            )
        } else {
            Log.d("RUN ELSE", "<=30")
//            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
//            requestBluetoothOn.launch(enableBtIntent)
            requestMultiplePermissionsAPI30Below.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            )
        }
    }

    fun combinedChart(combinedChart: CombinedChart){
        combinedChart.setDrawGridBackground(false)
        combinedChart.description.isEnabled = false
        combinedChart.axisRight.isEnabled = false
        combinedChart.setTouchEnabled(true)
        combinedChart.isDragEnabled = true
        combinedChart.setScaleEnabled(true)

        val data = CombinedData()
        val lineDatas = LineData()
        lineDatas.addDataSet(dataChart() as ILineDataSet)

        data.setData(lineDatas)
        combinedChart.data = data
        combinedChart.invalidate()
    }

    fun lineChart(lineChart: LineChart){
        lineChart.setDrawGridBackground(false)
        lineChart.description.isEnabled = false
        lineChart.axisRight.isEnabled = false
        lineChart.setTouchEnabled(true)
        lineChart.isDragEnabled = true
        lineChart.setScaleEnabled(true)

        val entries = listOf(
            Entry(0f, 1f),
            Entry(1f, 4f),
            Entry(2f, 2f),
            Entry(3f, 5f),
            Entry(4f, 3f),
            Entry(3f, 1f)
        )

        val dataSet = LineDataSet(entries, "Label for dataset")
        dataSet.color = ContextCompat.getColor(this, R.color.purple_500)
        dataSet.valueTextColor = ContextCompat.getColor(this, R.color.teal_700)
        val lineData = LineData(dataSet)

        lineChart.data = lineData
        lineChart.invalidate()
    }
}