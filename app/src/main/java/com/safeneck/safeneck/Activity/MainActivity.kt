package com.safeneck.safeneck.Activity

import android.content.Context
import android.databinding.BaseObservable
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.View
import com.safeneck.safeneck.MainViewPagerAdapter
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

        fun getResource(pos: Int): Int {
            when (pos) {
                0 -> if (isSelected(pos) == 0)
                    context.resources.getDrawable(R.drawable.ic_report_on)
                else context.resources.getDrawable(R.drawable.ic_report_off)

                1 -> if (isSelected(pos) == 0)
                    context.resources.getDrawable(R.drawable.ic_main_on)
                else context.resources.getDrawable(R.drawable.ic_main_off)

                2 -> if (isSelected(pos) == 0)
                    context.resources.getDrawable(R.drawable.ic_option_on)
                else context.resources.getDrawable(R.drawable.ic_option_off)
            }
            return pos
        }
    }

}
