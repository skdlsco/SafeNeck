package com.safeneck.safeneck.Fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.safeneck.safeneck.R
import kotlinx.android.synthetic.main.fragment_option.view.*

class OptionFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_option, container, false)
        return view
    }

    companion object {
        fun newInstance(): OptionFragment = OptionFragment()
    }
}