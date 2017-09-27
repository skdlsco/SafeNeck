package com.safeneck.safeneck.Fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.safeneck.safeneck.R
import kotlinx.android.synthetic.main.fragment_tutorial.view.*

class TutorialFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_tutorial, container, false)
        view.fragment_tutorial_img.setImageResource(R.drawable.ic_report)

        return view
    }

    companion object {
        fun newInstance(): TutorialFragment = TutorialFragment()
    }
}
