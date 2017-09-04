package com.safeneck.safeneck

import android.content.Context
import android.content.res.TypedArray
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ScaleXSpan
import android.util.AttributeSet
import android.widget.TextView

/**
 * Created by eka on 2017. 9. 4..
 */
class LetterSpacingTextView : TextView {
    private lateinit var originalText: CharSequence

    private var mLetterSpacing: Float = 0f

    constructor(context: Context?) : super(context) {
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        getAttrs(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        getAttrs(attrs, defStyleAttr)
    }

    override fun getLetterSpacing(): Float {
        return mLetterSpacing
    }

    override fun setLetterSpacing(letterSpacing: Float) {
        mLetterSpacing = letterSpacing
    }

    private fun getAttrs(attrs: AttributeSet?) {
        val typeArray: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.LetterSpacingTextView)
        setTypeArray(typeArray)
    }

    private fun getAttrs(attrs: AttributeSet?, defStyleAttr: Int) {
        val typeArray: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.LetterSpacingTextView, defStyleAttr, 0)
        setTypeArray(typeArray)
    }

    private fun setTypeArray(typedArray: TypedArray) {
        mLetterSpacing = typedArray.getFloat(R.styleable.LetterSpacingTextView_letterSpacing, 0f)
        typedArray.recycle()
        applyLetterSpacing()
    }

    private fun applyLetterSpacing() {
        val builder = StringBuilder()
        for (i in 0 until originalText.length) {
            val c = "" + originalText[i]
            builder.append(c)
            if (i + 1 < originalText.length) {
                builder.append("\u00A0")
            }
        }

        val finalText = SpannableString(builder.toString())
        if (builder.toString().length > 1) {
            for (i in 1 until builder.toString().length step 2)
                finalText.setSpan(ScaleXSpan(mLetterSpacing), i, i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        super.setText(finalText, BufferType.SPANNABLE)
    }

    override fun setText(text: CharSequence?, type: BufferType?) {
        originalText = text ?: ""
        applyLetterSpacing()
    }

    override fun getText(): CharSequence {
        return originalText
    }
}