package com.safeneck.safeneck

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import app.akexorcist.bluetotohspp.library.BluetoothSPP
import app.akexorcist.bluetotohspp.library.BluetoothState
import java.util.*

/**
 * Created by eka on 2017. 9. 19..
 */
class BluetoothService : Service() {
    lateinit var bluetooth: BluetoothSPP
    var isEnableReceive = true
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        bluetooth = BluetoothSPP(this)
        bluetooth.setOnDataReceivedListener { data, message ->
            if (isEnableReceive) {
                Log.d("received", message)
                isEnableReceive = false
                val timer = Timer()
                timer.schedule(object : TimerTask() {
                    override fun run() {
                        isEnableReceive = true
                    }

                }, 5000)
            }
        }
        bluetooth.setAutoConnectionListener(object : BluetoothSPP.AutoConnectionListener {
            override fun onAutoConnectionStarted() {
                Log.i("onAutoConnectionStarted", "" + bluetooth.pairedDeviceName)
            }

            override fun onNewConnection(name: String?, address: String?) {
            }
        })
        bluetooth.setBluetoothConnectionListener(object : BluetoothSPP.BluetoothConnectionListener {
            override fun onDeviceDisconnected() {
            }

            override fun onDeviceConnected(name: String?, address: String?) {
            }

            override fun onDeviceConnectionFailed() {
                Toast.makeText(applicationContext, "블루투스 오류", Toast.LENGTH_SHORT).show()
                stopSelf()
            }

        })
        bluetooth.setupService()
        bluetooth.startService(BluetoothState.DEVICE_OTHER)
        bluetooth.autoConnect("emi")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return Service.START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        bluetooth.stopService()
    }
}