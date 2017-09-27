package com.safeneck.safeneck.Fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.safeneck.safeneck.Models.Alarm
import com.safeneck.safeneck.R
import com.safeneck.safeneck.Utils.DataManager
import com.safeneck.safeneck.Utils.NetworkHelper
import com.safeneck.safeneck.View.BarChartView
import com.safeneck.safeneck.View.LetterSpacingTextView
import kotlinx.android.synthetic.main.fragment_main.view.*
import org.jetbrains.anko.support.v4.find
import org.jetbrains.anko.textColor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.util.Log
import com.safeneck.safeneck.ReportService
import okhttp3.ResponseBody
import org.json.JSONObject
import kotlin.collections.ArrayList


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

        val dataManager = DataManager(context)
        setDate(view)

        val today = ArrayList<BarChartView.Elements>()
        val weekly = ArrayList<BarChartView.Elements>()
        val monthly = ArrayList<BarChartView.Elements>()

        view.fragment_main_barChart.elements = today

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

        view.fragment_main_btn_daily.setOnClickListener {
            view.fragment_main_barChart.elements = today
            view.fragment_main_barChart.requestLayout()
        }

        view.fragment_main_btn_weekly.setOnClickListener {
            view.fragment_main_barChart.elements = weekly
            view.fragment_main_barChart.requestLayout()
        }

        view.fragment_main_btn_monthly.setOnClickListener {
            view.fragment_main_barChart.elements = monthly
            view.fragment_main_barChart.requestLayout()
        }
        if (NetworkHelper.returnNetworkState(context)) {
            val token = dataManager.token
            NetworkHelper.networkInstance.getData(token).enqueue(object : Callback<Alarm> {
                override fun onResponse(call: Call<Alarm>?, response: Response<Alarm>?) {
                    if (response?.code() == 200) {
                        if (response.body()?.status == 200) {
                            val todayAlarm = response.body()?.data!![0].todayAlarm
                            val weekAlarm = response.body()?.data!![0].weekAlarm

                            view.fragment_main_daily_count.text = todayAlarm
                            view.fragment_main_weekly_count.text = weekAlarm
                        }
                    }
                }

                override fun onFailure(call: Call<Alarm>?, t: Throwable?) {
                }
            })

            NetworkHelper.networkInstance.getSettingList(token).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                    if (response?.code() == 200) {
                        val json = JSONObject(response.body()!!.string())
                        val status = json.getInt("status")
                        if (status == 200) {
                            val data = json.getJSONArray("data").getJSONObject(0)
                            dataManager.weeklyAward = data.getInt("weeklyAward")
                            dataManager.dailyAward = data.getInt("dailyAward")
//                            dataManager.vibrateTime = data.getInt("time")
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {

                }
            })

            NetworkHelper.networkInstance.getBarGraph(token).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                    if (response?.code() == 200) {
                        val json = JSONObject(response.body()!!.string())
                        val status = json.getInt("status")
                        if (status == 200) {

                            val todayArray = json.getJSONArray("today")
                            var i = 0
                            while (todayArray.isNull(i)) {
                                today.add(BarChartView.Elements(todayArray.getInt(i), "$i 시"))
                                i++
                            }

                            val weeklyArray = json.getJSONArray("week")
                            i = 0
                            while (weeklyArray.isNull(i)) {
                                val day = when (i) {
                                    0 -> "월요일"
                                    1 -> "화요일"
                                    2 -> "수요일"
                                    3 -> "목요일"
                                    4 -> "금요일"
                                    5 -> "토요일"
                                    6 -> "일요일"
                                    else -> "월요일"
                                }
                                weekly.add(BarChartView.Elements(weeklyArray.getInt(i), day))
                                i++
                            }

                            val monthArray = json.getJSONArray("month")
                            i = 0
                            while (monthArray.isNull(i)) {
                                monthly.add(BarChartView.Elements(monthArray.getInt(i), "$i 일"))
                                i++
                            }

                        }
                    }
                }

                override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {

                }
            })
        }

        val calendar = Calendar.getInstance()
        if (dataManager.vibrateTime <= calendar.get(Calendar.HOUR_OF_DAY))
            calendar.add(Calendar.DATE, 1)
        calendar.set(Calendar.HOUR_OF_DAY, dataManager.vibrateTime)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        val intent = Intent(context, ReportService::class.java)
        val alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager
        val pendingIntent = PendingIntent.getService(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)

        setAwardText(view)
        return view
    }

    companion object {
        fun newInstance(): MainFragment = MainFragment()
    }

    private fun setAwardText(view: View) {
        val dataManager = DataManager(context)
        val dailyAward = dataManager.dailyAward
        view.fragment_main_daily_goal?.text = "일간 알림 개수 목표치는 ${dailyAward}회입니다"

        val weeklyAward = dataManager.weeklyAward
        view.fragment_main_weekly_goal?.text = "주간 알림 개수 목표치는 ${weeklyAward}회입니다"
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

