package com.safeneck.safeneck.Fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.safeneck.safeneck.Activity.LoginActivity

import com.safeneck.safeneck.R
import kotlinx.android.synthetic.main.fragment_tutorial.view.*
import org.jetbrains.anko.support.v4.startActivity

class Tutorial3Fragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_tutorial3, container, false)
        view.fragment_tutorial_img.setImageResource(R.drawable.ic_option)
        view.setOnClickListener {
            startActivity<LoginActivity>()
        }
        return view
    }

    companion object {
        fun newInstance(): Tutorial3Fragment = Tutorial3Fragment()
    }
}
