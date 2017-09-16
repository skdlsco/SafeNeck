package com.safeneck.safeneck.Fragment

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.safeneck.safeneck.R
import com.safeneck.safeneck.View.CircleGraphView
import kotlinx.android.synthetic.main.fragment_daily.view.*

class DailyFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view: View = inflater!!.inflate(R.layout.fragment_daily, container, false)
        val elements: ArrayList<CircleGraphView.Element> = ArrayList()
        elements.add(CircleGraphView.Element(resources.getColor(R.color.colorVeryGood), 25f))
        elements.add(CircleGraphView.Element(resources.getColor(R.color.colorGood), 25f))
        elements.add(CircleGraphView.Element(resources.getColor(R.color.colorCommon), 12f))
        elements.add(CircleGraphView.Element(resources.getColor(R.color.colorBad), 26f))
        elements.add(CircleGraphView.Element(resources.getColor(R.color.colorVeryBad), 12f))
        view.daily_circleGraph.elements = elements
        return view
    }

    companion object {
        fun newInstance(): DailyFragment = DailyFragment()
    }
}
