package com.safeneck.safeneck.Activity

import android.bluetooth.*
import android.content.Context
import android.content.Intent
import android.databinding.BaseObservable
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.View
import android.widget.Toast
import com.safeneck.safeneck.Adapter.MainViewPagerAdapter
import com.safeneck.safeneck.BluetoothService
import com.safeneck.safeneck.R
import com.safeneck.safeneck.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager
    private var mBluetoothAdapter: BluetoothAdapter? = null
    private var isScanning = false
    private var mHandler: Handler = Handler()
    private var mBluetoothGatt: BluetoothGatt? = null
    private var mBtDevice: BluetoothDevice? = null
    private var uuid = "0000ff31-0000-1000-8000-00805f9b34fb"
    private var uuidservice = "0000ff30-0000-1000-8000-00805f9b34fb"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val dataBinding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewPager = findViewById<ViewPager>(R.id.main_viewPager)
        val mainViewPagerAdapter = MainViewPagerAdapter(supportFragmentManager)
        viewPager.adapter = mainViewPagerAdapter

        val bottomBar = BottomBar(viewPager, this)
        dataBinding.bottomBar = bottomBar

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                bottomBar.bottomBarClick(position)
            }

        })
        viewPager.currentItem = 1

//        val btAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
//        if (btAdapter.isEnabled) {
//            val intent = Intent(this@MainActivity, BluetoothService::class.java)
//            startService(intent)
//        } else {
//            val btIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
//            startActivityForResult(btIntent, REQUEST_ENABLE_BT)
//        }
        getBtAdapter()
    }

    private var mLeScanCallback: BluetoothAdapter.LeScanCallback = BluetoothAdapter.LeScanCallback { btDevice, p1, p2 ->
        Log.e("onLeScan1", "" + btDevice?.address)
        Log.e("onLeScan2", "" + btDevice?.name)
        Log.e("onLeScan3", "" + (btDevice?.type == BluetoothDevice.DEVICE_TYPE_LE))
        if (btDevice?.name == "bt_ble") {
            mBtDevice = btDevice
            mHandler = Handler(Looper.getMainLooper())
            mHandler.post {
                if (mBtDevice != null) {
//                    mBluetoothGatt = mBtDevice!!.connectGatt(applicationContext, true, mBluetoothGattCallback)
                    scanLeDevice(false)
                    var intent = Intent(this, BluetoothService::class.java)
                    intent.putExtra("address", btDevice.address)
                    startService(intent)
                }
            }
        }
    }
    private var mBluetoothGattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            super.onConnectionStateChange(gatt, status, newState)
            Log.e("state", "$newState")
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            super.onServicesDiscovered(gatt, status)
            Log.e("stateD", "$status")
        }

        override fun onCharacteristicRead(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?, status: Int) {
            super.onCharacteristicRead(gatt, characteristic, status)
            Log.e("asdfasdf", "asdfasd")
            var format = BluetoothGattCharacteristic.FORMAT_SINT16
            val data = characteristic?.value
            if (data != null && data.isNotEmpty()) {
                val stringBuilder: StringBuilder = StringBuilder(data.size)
                for (byteChar in data)
                    stringBuilder.append(String.format("%02X ", byteChar))
                Log.e("characteristic", "" + stringBuilder)
            }
        }
    }

    private fun scanLeDevice(enable: Boolean) {
        if (enable) {
            isScanning = true
            mBluetoothAdapter?.startLeScan(mLeScanCallback)
        } else {
            mBluetoothAdapter?.stopLeScan(mLeScanCallback)
            isScanning = false
        }
    }


    private fun getBtAdapter() {
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        mBluetoothAdapter = bluetoothManager.adapter
        if (mBluetoothAdapter == null || !mBluetoothAdapter!!.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
        } else {
            scanLeDevice(true)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(applicationContext, "블루투스 ㅇㅋ", Toast.LENGTH_SHORT).show()
//                val intent = Intent(this@MainActivity, BluetoothService::class.java)
//                startService(intent)
            } else {
                Toast.makeText(applicationContext, "블루투스 연결 실패", Toast.LENGTH_SHORT).show()
            }
        }
    }

    class BottomBar(private var viewPager: ViewPager, val context: Context) : BaseObservable() {
        fun isSelected(pos: Int): Int {
            return when (pos) {
                viewPager.currentItem -> View.VISIBLE
                else -> View.GONE
            }
        }

        fun bottomBarClick(pos: Int) {
            viewPager.currentItem = pos
            notifyChange()
        }
    }

    companion object {
        private val REQUEST_ENABLE_BT = 0
        private val SCAN_PERIOD: Long = 3000
    }
}
