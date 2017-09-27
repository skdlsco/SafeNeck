package com.safeneck.safeneck.Fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.safeneck.safeneck.R
import com.safeneck.safeneck.Utils.DataManager
import com.safeneck.safeneck.Utils.NetworkHelper
import com.safeneck.safeneck.View.PieChartView
import kotlinx.android.synthetic.main.fragment_daily.view.*
import okhttp3.ResponseBody
import org.jetbrains.anko.textColor
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DailyFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view: View = inflater!!.inflate(R.layout.fragment_daily, container, false)
        val dataManager = DataManager(context)
        val elements: ArrayList<PieChartView.Element> = ArrayList()

        if (NetworkHelper.returnNetworkState(context)) {
            val token = dataManager.token
            NetworkHelper.networkInstance.getCircleGraph(token).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                    if (response?.code() == 200) {
                        val json = JSONObject(response.body()!!.string())
                        Log.e("getCircleGraph", "" + json.toString())
                        val status = json.getInt("status")

                        if (status == 200) {
                            val data = json.getJSONObject("data")
                            val fine = data.getInt("fine")
                            val caution = data.getInt("caution")
                            val warning = data.getInt("warning")
                            val bad = data.getInt("bad")
                            val verybad = data.getInt("verybad")
                            val sum = (fine + caution + warning + bad + verybad).toFloat()

                            elements.add(PieChartView.Element(resources.getColor(R.color.colorVeryGood), fine / sum * 100))
                            view.daily_pieChartValue_veryGood.setPercentage(fine / sum * 100)
                            elements.add(PieChartView.Element(resources.getColor(R.color.colorGood), caution / sum * 100))
                            view.daily_pieChartValue_good.setPercentage(caution / sum * 100)
                            elements.add(PieChartView.Element(resources.getColor(R.color.colorCommon), warning / sum * 100))
                            view.daily_pieChartValue_common.setPercentage(warning / sum * 100)
                            elements.add(PieChartView.Element(resources.getColor(R.color.colorBad), bad / sum * 100))
                            view.daily_pieChartValue_bad.setPercentage(bad / sum * 100)
                            elements.add(PieChartView.Element(resources.getColor(R.color.colorVeryBad), verybad / sum * 100))
                            view.daily_pieChartValue_veryBad.setPercentage(verybad / sum * 100)


                            view.daily_pieChart.elements = elements
                            view.daily_pieChart.requestLayout()
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                }
            })
            NetworkHelper.networkInstance.checkDay(token).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                    if (response?.code() == 200) {
                        val json = JSONObject(response.body()!!.string())
                        val status = json.getInt("status")
                        if (status == 200) {
                            setToday(view, json.getInt("today"))
                            setYesterday(view, json.getInt("yesterday"))
                            setDoubleday(view, json.getInt("doubleday"))

                        }
                    }
                }

                override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                    Log.e("asdfdsdf", "" + t.toString() + "\n" + t!!.message)
                }
            })
        }
        return view
    }

    fun setToday(view: View, status: Int) {
        when (status) {
            1 -> {
                view.daily_status_left_img.setImageResource(R.drawable.ic_daily_sogood)
                view.daily_status_left_text_status.text = "정상"
                view.daily_status_left_text_status.textColor = context.resources.getColor(R.color.colorVeryGood)
            }
            2 -> {
                view.daily_status_left_img.setImageResource(R.drawable.ic_daily_good)
                view.daily_status_left_text_status.text = "주의"
                view.daily_status_left_text_status.textColor = context.resources.getColor(R.color.colorGood)
            }
            3 -> {
                view.daily_status_left_img.setImageResource(R.drawable.ic_daily_standard)
                view.daily_status_left_text_status.text = "경고"
                view.daily_status_left_text_status.textColor = context.resources.getColor(R.color.colorCommon)
            }
            4 -> {
                view.daily_status_left_img.setImageResource(R.drawable.ic_daily_bad)
                view.daily_status_left_text_status.text = "나쁨"
                view.daily_status_left_text_status.textColor = context.resources.getColor(R.color.colorBad)
            }
            5 -> {
                view.daily_status_left_img.setImageResource(R.drawable.ic_daily_sobad)
                view.daily_status_left_text_status.text = "매우 나쁨"
                view.daily_status_left_text_status.textColor = context.resources.getColor(R.color.colorVeryBad)
            }
        }
    }

    fun setYesterday(view: View, status: Int) {
        when (status) {
            1 -> {
                view.daily_status_center_img.setImageResource(R.drawable.ic_daily_sogood)
                view.daily_status_center_text_status.text = "정상"
                view.daily_status_center_text_status.textColor = context.resources.getColor(R.color.colorVeryGood)
            }
            2 -> {
                view.daily_status_center_img.setImageResource(R.drawable.ic_daily_good)
                view.daily_status_center_text_status.text = "주의"
                view.daily_status_center_text_status.textColor = context.resources.getColor(R.color.colorVeryGood)
            }
            3 -> {
                view.daily_status_center_img.setImageResource(R.drawable.ic_daily_standard)
                view.daily_status_center_text_status.text = "경고"
                view.daily_status_center_text_status.textColor = context.resources.getColor(R.color.colorVeryGood)
            }
            4 -> {
                view.daily_status_center_img.setImageResource(R.drawable.ic_daily_bad)
                view.daily_status_center_text_status.text = "나쁨"
                view.daily_status_center_text_status.textColor = context.resources.getColor(R.color.colorBad)
            }
            5 -> {
                view.daily_status_center_img.setImageResource(R.drawable.ic_daily_sobad)
                view.daily_status_center_text_status.text = "매우 나쁨"
                view.daily_status_center_text_status.textColor = context.resources.getColor(R.color.colorVeryBad)
            }
        }
    }

    fun setDoubleday(view: View, status: Int) {
        when (status) {
            1 -> {
                view.daily_status_right_img.setImageResource(R.drawable.ic_daily_sogood)
                view.daily_status_right_text_status.text = "정상"
                view.daily_status_right_text_status.textColor = context.resources.getColor(R.color.colorVeryGood)
            }
            2 -> {
                view.daily_status_right_img.setImageResource(R.drawable.ic_daily_good)
                view.daily_status_right_text_status.text = "주의"
                view.daily_status_right_text_status.textColor = context.resources.getColor(R.color.colorGood)
            }
            3 -> {
                view.daily_status_right_img.setImageResource(R.drawable.ic_daily_standard)
                view.daily_status_right_text_status.text = "경고"
                view.daily_status_right_text_status.textColor = context.resources.getColor(R.color.colorCommon)
            }
            4 -> {
                view.daily_status_right_img.setImageResource(R.drawable.ic_daily_bad)
                view.daily_status_right_text_status.text = "나쁨"
                view.daily_status_right_text_status.textColor = context.resources.getColor(R.color.colorBad)
            }
            5 -> {
                view.daily_status_right_img.setImageResource(R.drawable.ic_daily_sobad)
                view.daily_status_right_text_status.text = "매우 나쁨"
                view.daily_status_right_text_status.textColor = context.resources.getColor(R.color.colorVeryBad)
            }
        }
    }

    companion object {
        fun newInstance(): DailyFragment = DailyFragment()
    }
}
