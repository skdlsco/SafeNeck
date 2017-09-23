package com.safeneck.safeneck.Adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.safeneck.safeneck.Fragment.DailyFragment
import com.safeneck.safeneck.Fragment.MainFragment
import com.safeneck.safeneck.Fragment.OptionFragment

/**
 * Created by eka on 2017. 9. 8..
 */
class MainViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment = when (position) {
        0 -> DailyFragment.newInstance()
        1 -> MainFragment.newInstance()
        2 -> OptionFragment.newInstance()
        else -> MainFragment.newInstance()
    }

    override fun getCount(): Int = 3

}