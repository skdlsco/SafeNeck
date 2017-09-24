package com.safeneck.safeneck

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.Html
import android.view.View
import kotlinx.android.synthetic.main.dialog_setting.*

/**
 * Created by eka on 2017. 9. 25..
 */
class SettingDialog(context: Context, var hint: String, var title: String, var content: String) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_setting)
        dialog_setting_edit.hint = Html.fromHtml("<i>${hint} </i>")
        dialog_setting_title.text = title
        dialog_setting_content.text = content
        dialog_setting_btn_positive.setOnClickListener {
            onButtonClickListener?.onPositiveClick(it, dialog_setting_edit.text.toString())
            dismiss()
        }
        dialog_setting_btn_negative.setOnClickListener {
            onButtonClickListener?.onNegativeClick(it, dialog_setting_edit.text.toString())
            dismiss()
        }
    }

    var onButtonClickListener: OnButtonClickListener? = null

    interface OnButtonClickListener {
        fun onNegativeClick(view: View, edit: String)
        fun onPositiveClick(view: View, edit: String)
    }
}