package com.safeneck.safeneck.View

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.text.Layout
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.safeneck.safeneck.R
import com.safeneck.safeneck.R.color.*
import kotlinx.android.synthetic.main.view_login.view.*
import org.jetbrains.anko.db.IntParser

/**
 * Created by eka on 2017. 9. 5..
 */
class LoginImageView : LinearLayout {

    constructor(context: Context?) : super(context) {
        initView()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initView()
        getAttrs(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView()
        getAttrs(attrs, defStyleAttr)
    }

    private fun getAttrs(attrs: AttributeSet?) {
        val typeArray: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.LoginImageView)
        setTypeArray(typeArray)
    }

    private fun getAttrs(attrs: AttributeSet?, defStyleAttr: Int) {
        val typeArray: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.LoginImageView, defStyleAttr, 0)
        setTypeArray(typeArray)
    }

    private fun initView() {
        val infService = Context.LAYOUT_INFLATER_SERVICE
        val layoutInflater: LayoutInflater = context.getSystemService(infService) as LayoutInflater
        val view: View = layoutInflater.inflate(R.layout.view_login, this, false)
        addView(view)
    }

    @SuppressLint("ResourceAsColor")
    private fun setTypeArray(typedArray: TypedArray) {

        val symbolRes = typedArray.getResourceId(R.styleable.LoginImageView_symbolRes, 0)
        view_login_symbol.setImageResource(symbolRes)

        val strokeColor = typedArray.getColor(R.styleable.LoginImageView_strokeColor, colorPrimaryDark)
        val strokeWidth = typedArray.getDimension(R.styleable.LoginImageView_strokeWidth, 2f)
        val drawable: GradientDrawable = view_login_container.background as GradientDrawable
        drawable.setStroke(strokeWidth.toInt(), strokeColor)
        view_login_container.background = drawable

        typedArray.recycle()
    }
}