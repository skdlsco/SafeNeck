package com.safeneck.safeneck.Fragment

import android.annotation.SuppressLint
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
    private val calendar = Calendar.getInstance()
    private var month = calendar.get(Calendar.MONTH)
    private var year = calendar.get(Calendar.YEAR)
    private var day = calendar.get(Calendar.DAY_OF_MONTH)

    private val texts: IntArray = intArrayOf(R.id.fragment_main_btn_daily, R.id.fragment_main_btn_weekly, R.id.fragment_main_btn_monthly)
    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view: View = inflater!!.inflate(R.layout.fragment_main, container, false)

        setDate(view)
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
            val dialog = DatePickerDialog.newInstance({ _, year, month, day ->
                calendar.set(year, month, day)
                setDate(view)
            }, year, month, day)
            dialog.show(activity.fragmentManager, "DatePickerDialog")
        }

        view.fragment_main_date_left.setOnClickListener {
            calendar.add(Calendar.DATE, -1)
            setDate(view)
            view.fragment_main_date.text = "${year}년 ${month}월 ${day}일"
        }
        view.fragment_main_date_right.setOnClickListener {
            calendar.add(Calendar.DATE, 1)
            setDate(view)
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

    private fun setDate(view: View) {
        month = calendar.get(Calendar.MONTH)
        year = calendar.get(Calendar.YEAR)
        day = calendar.get(Calendar.DAY_OF_MONTH)
        view.fragment_main_date.text = "${year}년 ${month}월 ${day}일"
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

