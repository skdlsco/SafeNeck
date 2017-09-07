package com.safeneck.safeneck.Activity

import android.databinding.BaseObservable
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.safeneck.safeneck.R
import com.safeneck.safeneck.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val bottomBar = BottomBar()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val dataBinding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        dataBinding.bottomBar = bottomBar
        bottomBar.selected = 1
    }

    class BottomBar : BaseObservable() {
        var selected = 0

        fun isSelected(pos: Int): Int {
            return when (pos) {
                selected -> View.VISIBLE
                else -> View.GONE
            }
        }

        fun bottomBarClick(pos: Int) {
            selected = pos
            notifyChange()
        }
    }
}
