package com.example.customviewanimation.ui

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.content.withStyledAttributes
import com.example.customviewanimation.R
import com.example.customviewanimation.utils.AndroidUtils
import kotlin.math.min
import kotlin.random.Random

class StatsView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) : View(context, attributeSet, defStyleAttr, defStyleRes) {

    var data: List<Float> = emptyList()
        set(value) {
            field = value
            invalidate()
        }

    private var center = PointF()
    private var radius = 0F
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
        strokeWidth = AndroidUtils.dp(context, 5F).toFloat()
    }

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = AndroidUtils.dp(context, 24F).toFloat()
    }

    private var oval = RectF()
    private var colors = emptyList<Int>()

    init {
        context.withStyledAttributes(attributeSet, R.styleable.StatsView) {
            textPaint.textSize = getDimension(R.styleable.StatsView_textSize, textPaint.textSize)
            paint.strokeWidth = getDimension(R.styleable.StatsView_lineWidth, paint.strokeWidth)
            colors = listOf(
                getColor(R.styleable.StatsView_color1, generateRandomColor()),
                getColor(R.styleable.StatsView_color2, generateRandomColor()),
                getColor(R.styleable.StatsView_color3, generateRandomColor()),
                getColor(R.styleable.StatsView_color4, generateRandomColor())
            )
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        radius = min(w, h) / 2F - paint.strokeWidth
        center = PointF(w / 2F, h / 2F)
        oval = RectF(
            center.x - radius,
            center.y - radius,
            center.x + radius,
            center.y + radius
        )
    }

    override fun onDraw(canvas: Canvas) {

        if (data.isEmpty()) {
            return
        }

        var percent = 0F
        var portion : Float
        var startAngle = -90F

        data.forEachIndexed { index, datum ->
            portion = datum / data.sum()
            val angle = portion * 360
            paint.color = colors.getOrElse(index) { generateRandomColor() }
            canvas.drawArc(oval, startAngle, angle, false, paint)
            startAngle += angle
            percent += portion
        }

        paint.color = colors[0]
        canvas.drawArc(oval, startAngle, 1F, false, paint)

        canvas.drawText(
            "%.2f%%".format(percent * 100),
            center.x,
            center.y + textPaint.textSize / 4,
            textPaint,
        )
    }

    private fun generateRandomColor() = Random.nextInt(0xFF000000.toInt(), 0xFFFFFFFF.toInt())
}