package com.safeneck.safeneck.Fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.safeneck.safeneck.R
import com.safeneck.safeneck.SettingDialog
import com.safeneck.safeneck.Utils.DataManager
import kotlinx.android.synthetic.main.fragment_option.view.*

class OptionFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_option, container, false)
        val dataManager = DataManager(context)
        view.option_vibrate_set.setOnCheckedChangeListener({ _, b ->
            dataManager.setVibrateOn(b)
        })

        view.option_weekly_goal_count_set.setOnClickListener {
            val dialog = SettingDialog(context, "ex) 27", "주간 목표 알림 개수 설정", "주간 경고 최대수는 700개입니다")
            dialog.onButtonClickListener = object : SettingDialog.OnButtonClickListener {
                override fun onNegativeClick(view: View, edit: String) {
                    Toast.makeText(context, "취소했습니다.", Toast.LENGTH_SHORT).show()
                }

                override fun onPositiveClick(view: View, edit: String) {
                    try {
                        edit.toInt()
                        if (edit.toInt() <= 0 || edit.toInt() >= 700)
                            Toast.makeText(context, "값이 이상합니다", Toast.LENGTH_SHORT).show()
                    } catch (e: NumberFormatException) {
                        Toast.makeText(context, "값이 이상합니다", Toast.LENGTH_SHORT).show()
                    }
                }

            }
            dialog.show()
        }

        view.option_daily_goal_count_set.setOnClickListener {
            val dialog = SettingDialog(context, "ex) 27", "주간 목표 알림 개수 설정", "주간 경고 최대수는 700개입니다")
            dialog.onButtonClickListener = object : SettingDialog.OnButtonClickListener {
                override fun onNegativeClick(view: View, edit: String) {
                    Toast.makeText(context, "취소했습니다.", Toast.LENGTH_SHORT).show()
                }

                override fun onPositiveClick(view: View, edit: String) {
                    try {
                        edit.toInt()
                        if (edit.toInt() <= 0 || edit.toInt() >= 700)
                            Toast.makeText(context, "값이 이상합니다", Toast.LENGTH_SHORT).show()
                    } catch (e: NumberFormatException) {
                        Toast.makeText(context, "값이 이상합니다", Toast.LENGTH_SHORT).show()
                    }
                }

            }
            dialog.show()
        }
        return view
    }

    companion object {
        fun newInstance(): OptionFragment = OptionFragment()
    }
}