package com.safeneck.safeneck

import android.app.Service
import android.bluetooth.*
import android.content.Context
import android.content.Intent
import android.os.Handler
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
    private var mBluetoothAdapter: BluetoothAdapter? = null
    private var mBluetoothGatt: BluetoothGatt? = null
    private var mHandler = Handler()
    private var string = ""
    private var address = ""
    private var isDataFFront = true

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
            Log.e("uuid", "" + service.uuid.toString() + "\n" + service.characteristics.size)
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

            if (isDataFFront) {
                string = String(data)
                isDataFFront = false
            } else {
                string += String(data)
                isDataFFront = true
                getData(string)
            }
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
    }


    override fun onDestroy() {
        super.onDestroy()
        mBluetoothGatt?.disconnect()
    }
}