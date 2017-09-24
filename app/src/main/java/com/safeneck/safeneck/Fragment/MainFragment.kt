package com.safeneck.safeneck.Fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.safeneck.safeneck.R
import com.safeneck.safeneck.View.BarChartView
import com.safeneck.safeneck.View.LetterSpacingTextView
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import kotlinx.android.synthetic.main.fragment_main.view.*
import org.jetbrains.anko.support.v4.find
import org.jetbrains.anko.textColor
import java.util.*

class MainFragment : Fragment() {

    private val texts: IntArray = intArrayOf(R.id.fragment_main_btn_daily, R.id.fragment_main_btn_weekly, R.id.fragment_main_btn_monthly)
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view: View = inflater!!.inflate(R.layout.fragment_main, container, false)
        view.fragment_main_btn_daily.isSelected = true
        view.fragment_main_btn_daily.setOnClickListener {
            setTextsProperty(0)
        }

        view.fragment_main_btn_weekly.setOnClickListener {
            setTextsProperty(1)
        }

        view.fragment_main_btn_monthly.setOnClickListener {
            setTextsProperty(2)
        }

        view.fragment_main_date.setOnClickListener {
            val calendar = Calendar.getInstance()
            val dialog = DatePickerDialog.newInstance({ _, _, _, _ ->

            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
            dialog.show(activity.fragmentManager, "DatePickerDialog")
        }
        val elements = ArrayList<BarChartView.Elements>()
        elements.add(BarChartView.Elements(2, "12시"))
        elements.add(BarChartView.Elements(4, "13시"))
        elements.add(BarChartView.Elements(5, "14시"))
        elements.add(BarChartView.Elements(7, "15시"))
        elements.add(BarChartView.Elements(8, "16시"))
        elements.add(BarChartView.Elements(1, "17시"))
        elements.add(BarChartView.Elements(9, "18시"))
        elements.add(BarChartView.Elements(11, "19시"))
        elements.add(BarChartView.Elements(3, "20시"))
        view.fragment_main_barChart.elements = elements
        return view
    }

    companion object {
        fun newInstance(): MainFragment = MainFragment()
    }

    private fun setTextsProperty(pos: Int) {
        for (i in 0 until texts.size) {
            find<LetterSpacingTextView>(texts[i]).let {
                if (i == pos) {
                    it.textColor = resources.getColor(R.color.colorWhite)
                    it.isSelected = true
                } else {
                    it.textColor = resources.getColor(R.color.colorPrimaryDark)
                    it.isSelected = false
                }
            }
        }
    }
}

