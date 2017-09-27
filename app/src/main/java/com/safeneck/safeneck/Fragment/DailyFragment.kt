package com.safeneck.safeneck.Fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.safeneck.safeneck.R
import com.safeneck.safeneck.Utils.DataManager
import com.safeneck.safeneck.Utils.NetworkHelper
import com.safeneck.safeneck.View.PieChartView
import kotlinx.android.synthetic.main.fragment_daily.view.*
import okhttp3.ResponseBody
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
        elements.add(PieChartView.Element(resources.getColor(R.color.colorVeryGood), 25f))
        elements.add(PieChartView.Element(resources.getColor(R.color.colorGood), 25f))
        elements.add(PieChartView.Element(resources.getColor(R.color.colorCommon), 12f))
        elements.add(PieChartView.Element(resources.getColor(R.color.colorBad), 26f))
        elements.add(PieChartView.Element(resources.getColor(R.color.colorVeryBad), 12f))
        view.daily_pieChart.elements = elements

        if (NetworkHelper.returnNetworkState(context)) {
            val token = dataManager.token
            NetworkHelper.networkInstance.getCircleGraph(token).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                    if (response?.code() == 200) {
                        val json = JSONObject(response.body()!!.string())
                        Log.e("getCircleGraph", "" + json.toString())
                        val status = json.getInt("status")
                        if (status == 200) {
                            Log.e("getCircleGraph", "" + json.toString())
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                }
            })
//            NetworkHelper.networkInstance.checkDay(token).enqueue()
        }
        return view
    }

    companion object {
        fun newInstance(): DailyFragment = DailyFragment()
    }
}
