package com.safeneck.safeneck

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log

/**
 * Created by eka on 2017. 9. 27..
 */
class StatusDialog(context: Context) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_status)

        BluetoothService.getData = object : BluetoothService.Companion.GetData.getData {
            override fun getData(data: String) {
                Log.e("inDialog", data)
                val stringBuilder = StringBuilder(data.length)
                for (char in data) {
                    stringBuilder.append(char)
                    if (char == '\n') {
                        stringBuilder.delete(0, stringBuilder.length)
                    }
                }
            }
        }
    }
}