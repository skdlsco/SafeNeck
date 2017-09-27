package com.safeneck.safeneck

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import kotlinx.android.synthetic.main.dialog_status.*

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
                val datas = ArrayList<String>()
                for (char in data) {
                    stringBuilder.append(char)
                    if (char == '\n') {
                        datas.add(stringBuilder.toString())
                        stringBuilder.delete(0, stringBuilder.length)
                    }
                }
                var handler = Handler()
                handler.post({
                    try {
                        var left = datas[0].toInt()
                        var middle = datas[1].toInt()
                        var right = datas[2].toInt()
                        var max = if (left > middle) left else middle
                        max = if (max > right) max else right
                        when (max) {
                            1 -> dialog_status_img.setImageResource(R.drawable.ic_daily_sogood)
                            2 -> dialog_status_img.setImageResource(R.drawable.ic_daily_good)
                            3 -> dialog_status_img.setImageResource(R.drawable.ic_daily_standard)
                            4 -> dialog_status_img.setImageResource(R.drawable.ic_daily_bad)
                            5 -> dialog_status_img.setImageResource(R.drawable.ic_daily_sobad)
                            else -> dialog_status_img.setImageResource(R.drawable.ic_daily_bad)
                        }
                    } catch (e: NumberFormatException) {

                    } catch (e: NullPointerException) {

                    }
                })
            }
        }

    }

    override fun onStop() {
        super.onStop()
    }

}