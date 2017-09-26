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
import com.safeneck.safeneck.Models.Setting
import com.safeneck.safeneck.R
import com.safeneck.safeneck.Utils.DataManager
import com.safeneck.safeneck.Utils.NetworkHelper
import com.safeneck.safeneck.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager
    private var mBluetoothAdapter: BluetoothAdapter? = null
    private var isScanning = false
    private var mHandler: Handler = Handler()
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

        val dataManager = DataManager(this)
        if (NetworkHelper.returnNetworkState(this)) {
            NetworkHelper.networkInstance.getSettingList(dataManager.token).enqueue(object : Callback<Setting> {
                override fun onResponse(call: Call<Setting>?, response: Response<Setting>?) {
                    if (response?.code() == 200) {
                        if (response.body()?.status == 200) {
                            dataManager.dailyAward = response.body()?.data?.dailyAward?.toInt()!!
                            dataManager.weeklyAward = response.body()?.data?.weeklyAward?.toInt()!!
                        }
                    }
                }

                override fun onFailure(call: Call<Setting>?, t: Throwable?) {
                }

            })
        }
        getBtAdapter()
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
            mHandler.postDelayed({
                mBluetoothAdapter?.stopLeScan(mLeScanCallback)
            }, SCAN_PERIOD)
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
