package com.safeneck.safeneck.Fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import com.safeneck.safeneck.Models.Setting
import com.safeneck.safeneck.R
import com.safeneck.safeneck.SettingDialog
import com.safeneck.safeneck.StatusDialog
import com.safeneck.safeneck.Utils.DataManager
import com.safeneck.safeneck.Utils.NetworkHelper
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_option.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OptionFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_option, container, false)
        val dataManager = DataManager(context)

        view.option_vibrate_set.isChecked = dataManager.isVibrateOn
        view.option_sleep_mode.isChecked = dataManager.isSleepMode

        view.option_vibrate_set.setOnCheckedChangeListener { _, b ->
            dataManager.isVibrateOn = b
        }

        view.option_sleep_mode.setOnCheckedChangeListener { button, b ->
            dataManager.isSleepMode = b
        }

        view.option_weekly_goal_count_set.setOnClickListener {
            val dialog = SettingDialog(context, "ex) 27", "주간 목표 알림 개수 설정", "주간 경고 최대수는 700개입니다")
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.onButtonClickListener = object : SettingDialog.OnButtonClickListener {
                override fun onNegativeClick(view: View, edit: String) {
                    Toast.makeText(context, "취소했습니다.", Toast.LENGTH_SHORT).show()
                }

                override fun onPositiveClick(view: View, edit: String) {
                    try {
                        if (edit.toInt() <= 0 || edit.toInt() >= 700)
                            Toast.makeText(context, "값이 이상합니다", Toast.LENGTH_SHORT).show()
                        else if (NetworkHelper.returnNetworkState(context)) {
                            NetworkHelper.networkInstance.setWeeklyAward(dataManager.token, edit).enqueue(object : Callback<Setting> {
                                override fun onFailure(call: Call<Setting>?, t: Throwable?) {
                                }

                                override fun onResponse(call: Call<Setting>?, response: Response<Setting>?) {
                                    if (response?.code() == 200) {
                                        if (response.body()?.status == 200) {
                                            dataManager.weeklyAward = edit.toInt()
                                        }
                                    }
                                }
                            })
                        }
                    } catch (e: NumberFormatException) {
                        Toast.makeText(context, "값이 이상합니다", Toast.LENGTH_SHORT).show()
                    }
                }

            }
            dialog.show()
        }

        view.option_daily_goal_count_set.setOnClickListener {
            val dialog = SettingDialog(context, "ex) 27", "하루 목표 알림 개수 설정", "하루 경고 최대수는 100개입니다")
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.onButtonClickListener = object : SettingDialog.OnButtonClickListener {
                override fun onNegativeClick(view: View, edit: String) {
                    Toast.makeText(context, "취소했습니다.", Toast.LENGTH_SHORT).show()
                }

                override fun onPositiveClick(view: View, edit: String) {
                    try {
                        edit.toInt()
                        if (edit.toInt() <= 0 || edit.toInt() >= 100) {
                            Toast.makeText(context, "값이 이상합니다", Toast.LENGTH_SHORT).show()

                        } else if (NetworkHelper.returnNetworkState(context)) {
                            NetworkHelper.networkInstance.setDailyAward(dataManager.token, edit).enqueue(object : Callback<Setting> {
                                override fun onFailure(call: Call<Setting>?, t: Throwable?) {
                                }

                                override fun onResponse(call: Call<Setting>?, response: Response<Setting>?) {
                                    if (response?.code() == 200) {
                                        if (response.body()?.status == 200) {
                                            dataManager.dailyAward = edit.toInt()
                                        }
                                    }
                                }
                            })
                        }
                    } catch (e: NumberFormatException) {
                        Toast.makeText(context, "값이 이상합니다", Toast.LENGTH_SHORT).show()
                    }
                }

            }
            dialog.show()
        }
        view.option_vibrate_time_set.setOnClickListener {
            val dialog = SettingDialog(context, "ex) 7", "진동 시간 설정", "1초 ~ 10초로 설정 가능합니다")
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.onButtonClickListener = object : SettingDialog.OnButtonClickListener {
                override fun onNegativeClick(view: View, edit: String) {
                    Toast.makeText(context, "취소했습니다.", Toast.LENGTH_SHORT).show()
                }

                override fun onPositiveClick(view: View, edit: String) {
                    try {
                        edit.toInt()
                        if (edit.toInt() <= 1 || edit.toInt() >= 10)
                            Toast.makeText(context, "값이 이상합니다", Toast.LENGTH_SHORT).show()
                        else
                            dataManager.vibrateTime = edit.toInt()
                    } catch (e: NumberFormatException) {
                        Toast.makeText(context, "값이 이상합니다", Toast.LENGTH_SHORT).show()
                    }
                }

            }
            dialog.show()
        }

        view.option_report_time_set.setOnClickListener {
            val dialog = SettingDialog(context, "ex) 7", "리포트 알림 시간 설정", "1시 ~ 24시로 설정가능합니다.")
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.onButtonClickListener = object : SettingDialog.OnButtonClickListener {
                override fun onNegativeClick(view: View, edit: String) {
                    Toast.makeText(context, "취소했습니다.", Toast.LENGTH_SHORT).show()
                }

                override fun onPositiveClick(view: View, edit: String) {
                    try {
                        if (edit.toInt() < 1 || edit.toInt() > 24)
                            Toast.makeText(context, "값이 이상합니다", Toast.LENGTH_SHORT).show()
                        else if (NetworkHelper.returnNetworkState(context)) {
                            NetworkHelper.networkInstance.setReportTime(dataManager.token, edit).enqueue(object : Callback<Setting> {
                                override fun onFailure(call: Call<Setting>?, t: Throwable?) {
                                }

                                override fun onResponse(call: Call<Setting>?, response: Response<Setting>?) {
                                    if (response?.code() == 200) {
                                        if (response.body()?.status == 200) {
                                            dataManager.vibrateTime = edit.toInt()
                                        }
                                    }
                                }
                            })
                        }
                    } catch (e: NumberFormatException) {
                        Toast.makeText(context, "값이 이상합니다", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            dialog.show()
        }
        view.option_status_live.setOnClickListener {
            val statusDialog = StatusDialog(context)
            statusDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            statusDialog.show()
        }
        return view
    }

    companion object {
        fun newInstance(): OptionFragment = OptionFragment()
    }
}