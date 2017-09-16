package com.safeneck.safeneck.View

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.safeneck.safeneck.R
import kotlinx.android.synthetic.main.view_circlegraph_value.view.*
import org.jetbrains.anko.textColor

class CircleGraphValueView : LinearLayout {
    var titleColor = Color.WHITE
    var title = ""

    constructor(context: Context?) : super(context) {
        initView()
        setView()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initView()
        getAttrs(attrs!!)
        setView()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView()
        getAttrs(attrs!!, defStyleAttr)
        setView()
    }

    private fun getAttrs(attrs: AttributeSet) {
        val typedArray: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleGraphValueView)
        setTypedArray(typedArray)
    }

    private fun getAttrs(attrs: AttributeSet, defStyleAttr: Int) {
        val typedArray: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleGraphValueView, defStyleAttr, 0)
        setTypedArray(typedArray)
    }

    private fun setTypedArray(typedArray: TypedArray) {
        titleColor = typedArray.getColor(R.styleable.CircleGraphValueView_titleColor, Color.WHITE)
        title = typedArray.getString(R.styleable.CircleGraphValueView_status)
        typedArray.recycle()
    }

    private fun initView() {
        val infService = Context.LAYOUT_INFLATER_SERVICE
        val layoutInflater: LayoutInflater = context.getSystemService(infService) as LayoutInflater
        val view: View = layoutInflater.inflate(R.layout.view_circlegraph_value, this, false)
        addView(view)
    }

    private fun setView() {
        circleGraphValue_img.setColorFilter(titleColor)
        circleGraphValue_status.text = title
        circleGraphValue_status.textColor = titleColor
    }

    @SuppressLint("SetTextI18n")
    fun setPercentage(percentage: Float) {
        circleGraphValue_percentage.text = "" + percentage
    }
}