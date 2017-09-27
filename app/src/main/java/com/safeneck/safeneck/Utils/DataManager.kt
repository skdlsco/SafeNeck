package com.safeneck.safeneck.Utils

import android.content.Context
import android.content.SharedPreferences
import com.safeneck.safeneck.BluetoothService

/**
 * Created by eka on 2017. 9. 25..
 */
class DataManager(context: Context) {
    private var preferences: SharedPreferences = context.getSharedPreferences("Data", Context.MODE_PRIVATE)
    private var editor: SharedPreferences.Editor = preferences.edit()

    var isSleepMode: Boolean
        get() = preferences.getBoolean("sleepMode", false)
        set(b) {
            editor.putBoolean("sleepMode", b)
            editor.apply()
        }


    var isVibrateOn: Boolean
        get() = preferences.getBoolean("vibrateOn", true)
        set(b) {
            editor.putBoolean("vibrateOn", b)
            editor.apply()
        }

    var vibrateTime: Int
        get() = preferences.getInt("vibrateTime", 1)
        set(t) {
            editor.putInt("vibrateTime", vibrateTime)
            editor.apply()
        }
    var reportTime: Int
        get() = preferences.getInt("reportTime", 22)
        set(t) {
            editor.putInt("reportTime", t)
            editor.apply()
        }
    var dailyAward: Int
        get() = preferences.getInt("dailyAward", 1)
        set(award) {
            editor.putInt("dailyAward", award)
            editor.apply()
        }
    var weeklyAward: Int
        get() = preferences.getInt("weeklyAward", 1)
        set(award) {
            editor.putInt("weeklyAward", award)
            editor.apply()
        }

    var token: String
        get() = preferences.getString("token", "")
        set(token) {
            editor.putString("token", token)
            editor.apply()
        }
    var status: Int
        get() = preferences.getInt("status", BluetoothService.Companion.Status.VERY_BAD.ordinal)
        set(status) {
            editor.putInt("status", status)
            editor.apply()
        }
}

