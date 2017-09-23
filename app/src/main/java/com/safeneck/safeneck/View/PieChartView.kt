package com.safeneck.safeneck.View

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.safeneck.safeneck.R

class PieChartView : View {
    var elements: ArrayList<Element> = ArrayList()

    var innerCircleColor = Color.WHITE
    var outerStrokeColor = Color.WHITE
    var innerStrokeColor = Color.WHITE
    var outerStrokeWidth = 0f
    var innerStrokeWidth = 0f
    var outerRadius = 0f
    var innerRadius = 0f

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        getAttrs(attrs!!)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        getAttrs(attrs!!, defStyleAttr)
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        var startAngle = 180f
        val outerPaint = Paint()
        outerPaint.isAntiAlias = true
        outerPaint.style = Paint.Style.FILL

        val outerCircle = RectF()
        outerCircle.set(width / 2 - outerRadius, height / 2 - outerRadius,
                width / 2 + outerRadius, height / 2 + outerRadius)

        canvas?.let {
            val width = canvas.width
            val height = canvas.height

            for (element in elements) {
                outerPaint.color = element.color
                val sweepAngle = 360f / (100f / element.percentage)
                canvas.drawArc(outerCircle, startAngle, sweepAngle + 0.1f, true, outerPaint)
                startAngle += sweepAngle
            }

            val innerPaint = Paint()
            innerPaint.style = Paint.Style.FILL
            innerPaint.color = innerCircleColor
            canvas.drawCircle(width / 2f, height / 2f, innerRadius, innerPaint)
        }
        super.onDraw(canvas)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(widthMeasureSpec, (outerRadius * 2).toInt() + paddingTop + paddingBottom)
    }

    private fun getAttrs(attrs: AttributeSet, defStyleAttr: Int) {
        val typeArray: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleGraphView, defStyleAttr, 0)
        setTypeArray(typeArray)
    }

    private fun getAttrs(attrs: AttributeSet) {
        val typedArray: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleGraphView)
        setTypeArray(typedArray)
    }

    private fun setTypeArray(typedArray: TypedArray) {
        innerCircleColor = typedArray.getColor(R.styleable.CircleGraphView_innerCircleColor, Color.WHITE)

        innerRadius = typedArray.getDimension(R.styleable.CircleGraphView_innerCircleRadius, 0f)
        outerRadius = typedArray.getDimension(R.styleable.CircleGraphView_outerCircleRadius, innerRadius)

        innerStrokeColor = typedArray.getColor(R.styleable.CircleGraphView_innerStrokeColor, Color.WHITE)
        outerStrokeColor = typedArray.getColor(R.styleable.CircleGraphView_outerStrokeColor, Color.WHITE)

        innerStrokeWidth = typedArray.getDimension(R.styleable.CircleGraphView_innerStrokeWidth, 0f)
        outerStrokeWidth = typedArray.getDimension(R.styleable.CircleGraphView_outerStrokeWidth, 0f)

        typedArray.recycle()
    }

    class Element(var color: Int, var percentage: Float)
}
