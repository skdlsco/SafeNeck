package com.safeneck.safeneck

import android.app.Notification
import android.app.NotificationManager
import android.app.Service
import android.bluetooth.*
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.util.Log
import java.util.*
import android.os.PowerManager
import com.safeneck.safeneck.Utils.DataManager
import com.safeneck.safeneck.Utils.NetworkHelper
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by eka on 2017. 9. 19..
 */
class BluetoothService : Service() {
    private var mBluetoothAdapter: BluetoothAdapter? = null
    private var mBluetoothGatt: BluetoothGatt? = null
    private var mHandler = Handler()
    //    private var string = ""
    private var address = ""
    //    private var dataIndex = 0
    private var isNotificationOk = true

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {
            address = intent.getStringExtra("address")
        }
        if (mBluetoothAdapter == null && address != "") {
            getAdapter()
        }
        if (mBluetoothGatt == null && mBluetoothAdapter != null && address != "")
            getGatt()

        return Service.START_STICKY
    }


    private fun getAdapter() {
        val mBluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        mBluetoothAdapter = mBluetoothManager.adapter
    }

    private fun getGatt() {
        mBluetoothGatt = mBluetoothAdapter?.getRemoteDevice(address)?.connectGatt(this, true, mBluetoothGattCallback)
        mBluetoothGatt?.discoverServices()
    }

    private fun setNotification() {
        val services = mBluetoothGatt!!.services
        for (service in services!!) {
            Log.e("uuid", "" + service.uuid.toString())
            if (service.uuid.toString() == "0000ffe0-0000-1000-8000-00805f9b34fb") {
                val char = service.getCharacteristic(UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb"))
                mBluetoothGatt?.setCharacteristicNotification(char, true)
            }
        }
    }

    private val mBluetoothGattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            super.onConnectionStateChange(gatt, status, newState)
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                mHandler.post { mBluetoothGatt!!.discoverServices() }
            }
        }

        override fun onCharacteristicChanged(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?) {
            super.onCharacteristicChanged(gatt, characteristic)
            val data = characteristic!!.value
            getData(String(data))
//            if (dataIndex == 0 || dataIndex == 1) {
//                string += String(data)
//                dataIndex++
//            } else {
//                string += String(data)
//                getData(string)
//                dataIndex = 0
//                string = ""
//            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            super.onServicesDiscovered(gatt, status)
            setNotification()
        }
    }

    fun getData(data: String) {
        val stringBuilder = StringBuilder(data.length)

        for (char in data) {
            stringBuilder.append(char)
            if (char == '\n') {
                Log.e("data", stringBuilder.toString())
                stringBuilder.delete(0, stringBuilder.length)
            }
        }
        getData?.getData(data)
        val dataManager = DataManager(this)
        val isSleepMode = dataManager.isSleepMode
        if (isNotificationOk && isSleepMode) {
//            isNotificationOk = false
//            mHandler.postDelayed({
//                isNotificationOk = true
//            }, 7000)
            val vibrate: Long = (dataManager.vibrateTime * 1000).toLong()
            val vibrateArr: LongArray = kotlin.LongArray(3, { it.toLong() * vibrate })
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (dataManager.isVibrateOn) {
                val notification = Notification.Builder(this)
                        .setContentTitle("Safe Neck")
                        .setContentText("자세가 흐트러졌어요")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setTicker("자세가 흐트러 졌어요")
                        .setAutoCancel(true)
                        .setVibrate(vibrateArr)
                        .build()
                notificationManager.notify(0, notification)
            } else {
                val notification = Notification.Builder(this)
                        .setContentTitle("Safe Neck")
                        .setContentText("자세가 흐트러졌어요")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setTicker("자세가 흐트러 졌어요")
                        .setAutoCancel(true)
                        .build()
                notificationManager.notify(0, notification)
            }

            val pm = getSystemService(Context.POWER_SERVICE) as PowerManager
            val wakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG")
            wakeLock.acquire(vibrate + 1000)
        }
    }

    fun addData(data: String) {
        val stringBuilder = StringBuilder(data.length)
        val dataManager = DataManager(this)
        val token = dataManager.token

        for (char in data) {
            stringBuilder.append(char)
            if (char == '\n') {
                Log.e("data", stringBuilder.toString())
                stringBuilder.delete(0, stringBuilder.length)
            }
        }

        NetworkHelper.networkInstance.saveUserNeck(token).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                if (response?.code() == 200) {
                    val json = JSONObject(response.body()!!.string())
                    val status = json.getInt("status")
                    if (status == 200) {

                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
            }

        })
    }

    companion object {
        enum class Status {
            VERY_BAD, BAD, COMMON, GOOD, VERY_GOOD
        }

        var getData: GetData.getData? = null

        class GetData {
            interface getData {
                fun getData(data: String)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mBluetoothGatt?.disconnect()
    }
}