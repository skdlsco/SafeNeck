package com.safeneck.safeneck.Adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.safeneck.safeneck.Fragment.Tutorial2Fragment
import com.safeneck.safeneck.Fragment.Tutorial3Fragment
import com.safeneck.safeneck.Fragment.TutorialFragment

/**
 * Created by eka on 2017. 9. 28..
 */
class TutorialAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment = when (position) {
        0 -> TutorialFragment.newInstance()
        1 -> Tutorial2Fragment.newInstance()
        2 -> Tutorial3Fragment.newInstance()
        else -> {
            TutorialFragment.newInstance()
        }
    }

    override fun getCount(): Int = 3
}