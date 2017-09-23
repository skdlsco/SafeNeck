package com.safeneck.safeneck.Activity

import android.content.Context
import android.content.Intent
import android.databinding.BaseObservable
import android.databinding.DataBindingUtil
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.View
import android.widget.Toast
import com.safeneck.safeneck.BluetoothService
import com.safeneck.safeneck.Adapter.MainViewPagerAdapter
import com.safeneck.safeneck.R
import com.safeneck.safeneck.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager
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

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
                val intent = Intent(this@MainActivity, BluetoothService::class.java)
                startService(intent)
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
    }
}
