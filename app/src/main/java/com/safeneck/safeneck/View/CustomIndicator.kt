package com.safeneck.safeneck.View

import android.view.Gravity
import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import com.safeneck.safeneck.R


/**
 * Created by eka on 2017. 9. 28..
 */
class CustomIndicator : LinearLayout {
    var mContext: Context? = null
    private val margin = 24
    private val select_dot = R.drawable.indicator_dot
    private val deselect_dot = R.drawable.indicator_dot
    private var dots: ArrayList<ImageView>? = null

    constructor(context: Context) : super(context) {
        mContext = context
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        mContext = context
    }

    fun CreatIndicator(count: Int, indicator_size: Int) {
        var i = 0
        dots = ArrayList<ImageView>()
        val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT)
        params.gravity = Gravity.CENTER
        params.height = indicator_size
        params.width = indicator_size
        params.setMargins(margin, margin, margin, margin)
        while (i < count) {
            dots!!.add(ImageView(mContext))
            dots!![i].setImageResource(deselect_dot)
            dots!![i].layoutParams = params
            dots!![i].alpha = 0.5f
            this.addView(dots!![i])
            i++
        }
        select(0)
    }

    fun select(position: Int) {
        var i = 0
        while (i < dots!!.size) {
            if (i == position) {
                dots!![i].setImageResource(select_dot)
                dots!![i].alpha = 1f
            } else {
                dots!![i].setImageResource(deselect_dot)
                dots!![i].alpha = 0.5f
            }
            i++
        }
    }
}