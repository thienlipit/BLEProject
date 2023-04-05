package com.example.wifipositioning

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
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
import com.github.mikephil.charting.charts.CombinedChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import java.util.*
import kotlin.collections.ArrayList

const val NUMBERPOINT = 20
class MainActivity : AppCompatActivity() {
    var dataListValue: MutableList<Int> = mutableListOf()
    var index = 1f
    var indexCombinedChart = 1f
    lateinit var receiver: WifiScanner
    private var requestBluetoothOn =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                //granted
                Log.d("TAG", "Bluetooth is ON")
            } else {
                //deny
                Log.d("TAG", "Bluetooth is OFF")
            }
        }

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
        val lineChart = findViewById<LineChart>(R.id.lineChart1)


        val timer = Timer()
        val task: TimerTask = Helper {
            Log.d(this::class.java.name, "random: $it")
            if(dataListValue.size < 20){
                dataListValue.add(it)
            } else {
                dataListValue.removeAt(0)
                dataListValue.add(it)
            }
        }

        timer.scheduleAtFixedRate(task, 200, 1000)

        lineChart(lineChart)
        combinedChart(combinedChart)


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

    private fun dataChart(entries: ArrayList<Entry>): LineDataSet {
//        val entries = ArrayList<Entry>()
        if( dataListValue.size > 0){
            val newData = Entry(indexCombinedChart, dataListValue.last().toFloat())
            entries.add(newData)
            index++
            if(dataListValue.size > 19){
                entries.removeFirst()
            }
        }

        val set = LineDataSet(entries, "Request Ots approved")
        set.color = Color.GREEN
        set.lineWidth = 2.5f
        set.circleRadius = 5f
        set.fillColor = Color.GREEN
        set.mode = LineDataSet.Mode.CUBIC_BEZIER
        set.setDrawValues(true)
        set.valueTextSize = 10f
        set.valueTextColor = Color.GREEN

        set.axisDependency = YAxis.AxisDependency.LEFT
        return set


/*        val dataValues = intArrayOf(55, 59, 50, 54, 58)
        val entries = ArrayList<Entry>()
        for (index in dataValues.indices) {
            entries.add(Entry(index.toFloat(), dataValues[index].toFloat()))
        }

        val set = LineDataSet(entries, "Request Ots approved")
        set.color = Color.GREEN
        set.lineWidth = 2.5f
        set.circleRadius = 5f
        set.fillColor = Color.GREEN
        set.mode = LineDataSet.Mode.CUBIC_BEZIER
        set.setDrawValues(true)
        set.valueTextSize = 10f
        set.valueTextColor = Color.GREEN

        set.axisDependency = YAxis.AxisDependency.LEFT
        return set*/
    }

    fun combinedChart(combinedChart: CombinedChart){
        combinedChart.setDrawGridBackground(false)
        combinedChart.description.isEnabled = false
        combinedChart.axisRight.isEnabled = false
        combinedChart.setTouchEnabled(true)
        combinedChart.isDragEnabled = true
        combinedChart.setScaleEnabled(true)

        val lineEntries = arrayListOf<Entry>()
        for (i in 0..9) {
            lineEntries.add(Entry(i.toFloat(), (Math.random() * 100).toFloat()))
        }
        val lineDataSet = LineDataSet(lineEntries, "Line Data")
        lineDataSet.color = Color.BLUE
        lineDataSet.setDrawValues(false)
        // Create a CombinedData object with the line and bar data
        val data = CombinedData()
        data.setData(LineData(lineDataSet))

// Set the data to the chart and update it
        combinedChart.data = data

//        val data = CombinedData()
//        val lineDatas = LineData()
//        val entries = ArrayList<Entry>()
//        lineDatas.addDataSet(dataChart(entries) as ILineDataSet)
//
//        data.setData(lineDatas)
//        combinedChart.data = data
//        val timer11 = Timer()
//        timer11.scheduleAtFixedRate(object : TimerTask() {
//            override fun run() {
//                lineDatas.addDataSet(dataChart(entries) as ILineDataSet)
//                data.setData(lineDatas)
//                combinedChart.data = data
//
//                data.notifyDataChanged()
//                combinedChart.notifyDataSetChanged()
//                combinedChart.invalidate()
//            }
//        }, 0, 1000) // Repeat every 1000ms (1 second)

        combinedChart.invalidate()
    }

    private fun lineChart(lineChart: LineChart){
        lineChart.setDrawGridBackground(false)
        lineChart.description.isEnabled = false
        lineChart.axisRight.isEnabled = false
        lineChart.setTouchEnabled(true)
        lineChart.isDragEnabled = true
        lineChart.setScaleEnabled(true)
        lineChart.xAxis.isEnabled = false

        val dataSet = LineDataSet(null, "Data")
        dataSet.color = Color.RED
        dataSet.setDrawValues(false)
        dataSet.setDrawCircles(false)
        lineChart.data = LineData(dataSet)

        val timer11 = Timer()
        timer11.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                updateDataLineChart(dataSet, lineChart)
            }
        }, 0, 1000) // Repeat every 1000ms (1 second)

        lineChart.invalidate()
    }

    fun updateDataLineChart(dataSet: LineDataSet, lineChart: LineChart){
        if(dataListValue.size > 0){
            val newData = Entry(index, dataListValue.last().toFloat())
            index++
            dataSet.addEntry(newData)
            dataSet.setDrawValues(true)
            lineChart.data = LineData(dataSet)
            dataSet.notifyDataSetChanged()
            lineChart.notifyDataSetChanged()
            lineChart.invalidate()
            if(dataListValue.size > 19){
                dataSet.removeFirst()
                lineChart.data = LineData(dataSet)
                dataSet.notifyDataSetChanged()
                lineChart.notifyDataSetChanged()
                lineChart.invalidate()
            }
        }
    }

}