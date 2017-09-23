package com.safeneck.safeneck.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.safeneck.safeneck.R
import kotlinx.android.synthetic.main.spinner_item.view.*

/**
 * Created by eka on 2017. 9. 11..
 */
class SpinnerAgeAdapter(private val items: ArrayList<String>, val context: Context) : BaseAdapter() {

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = LayoutInflater.from(context).inflate(R.layout.spinner_item, null)
        val text = "" + getItem(position) + "대"
        view.text.text = text
        return view
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
        var v: View? = view
        if (v == null) {
            v = LayoutInflater.from(context).inflate(R.layout.spinner_seleted_item, null)
        }

        val text = "" + getItem(position) + "대"
        v!!.text.text = text
        return v
    }

    override fun getItem(position: Int): String = items[position]

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int = items.size

}