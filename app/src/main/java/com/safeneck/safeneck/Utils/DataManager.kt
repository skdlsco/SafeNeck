package com.safeneck.safeneck.Utils

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by eka on 2017. 9. 25..
 */
class DataManager(context: Context) {
    private var preferences: SharedPreferences = context.getSharedPreferences("Data", Context.MODE_PRIVATE)
    private var editor: SharedPreferences.Editor = preferences.edit()

    fun getVibrateOn(): Boolean = preferences.getBoolean("vibrateOn", false)
    fun setVibrateOn(b: Boolean) {
        editor.putBoolean("vibrateOn", b)
    }
}
