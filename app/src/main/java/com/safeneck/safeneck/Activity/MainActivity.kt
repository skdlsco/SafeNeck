package com.safeneck.safeneck.Activity

import android.Manifest
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
import android.support.v4.app.ActivityCompat
import android.content.pm.PackageManager


class MainActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager
    private var mBluetoothAdapter: BluetoothAdapter? = null
    private var mHandler: Handler = Handler()
    private var mBtDevice: BluetoothDevice? = null
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

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), REQUEST_ENABLE_LOCATION)
        } else {
            getBtAdapter()
        }
    }

    private var mLeScanCallback: BluetoothAdapter.LeScanCallback = BluetoothAdapter.LeScanCallback { btDevice, p1, p2 ->
        Log.e("onLeScan1", "" + btDevice?.address)
        Log.e("onLeScan2", "" + btDevice?.name)
        Log.e("onLeScan3", "" + (btDevice?.type == BluetoothDevice.DEVICE_TYPE_LE))
        if (btDevice?.name == "STAC_BT") {
            mBtDevice = btDevice
            mHandler = Handler(Looper.getMainLooper())
            mHandler.post {
                if (mBtDevice != null) {
                    scanLeDevice(false)
                    val intent = Intent(this, BluetoothService::class.java)
                    intent.putExtra("address", btDevice.address)
                    startService(intent)
                }
            }
        }
    }

    private fun scanLeDevice(enable: Boolean) {
        if (enable) {
            mBluetoothAdapter?.startLeScan(mLeScanCallback)
            mHandler.postDelayed({
                mBluetoothAdapter?.stopLeScan(mLeScanCallback)
            }, SCAN_PERIOD)
        } else {
            mBluetoothAdapter?.stopLeScan(mLeScanCallback)
        }
    }


    private fun getBtAdapter() {
        val thread = Thread(Runnable {

            val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
            mBluetoothAdapter = bluetoothManager.adapter
            if (mBluetoothAdapter == null || !mBluetoothAdapter!!.isEnabled) {
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
            } else {

                scanLeDevice(true)
            }
        })
        thread.start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
                getBtAdapter()
            } else {
                Toast.makeText(applicationContext, "블루투스 연결 실패", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_ENABLE_LOCATION -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED || grantResults[1] == PackageManager.PERMISSION_GRANTED)
                    getBtAdapter()
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
        private val REQUEST_ENABLE_LOCATION = 1
        private val REQUEST_ENABLE_BT = 0
        private val SCAN_PERIOD: Long = 3000
    }
}
