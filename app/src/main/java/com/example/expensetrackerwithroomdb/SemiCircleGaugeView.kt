package com.example.expensetrackerwithroomdb

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

class SemiCircleGaugeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {

    private var percentage: Float = 0f
    private val paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = 30f
    }

    private val rect = RectF()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val width = width.toFloat()
        val height = height.toFloat()

        // Define rect dimensions based on the view size
        val radius = Math.min(width, height) / 2 - paint.strokeWidth / 2
        val cx = width / 2
        val cy = height

        rect.set(cx - radius, cy - radius, cx + radius, cy + radius)

        // Draw background arc (semi-circle)
        paint.color = Color.parseColor("#888888") // Gray color
        paint.strokeWidth = 30f
        canvas.drawArc(rect, 180f, 180f, false, paint)

        // Draw filled arc representing the percentage
        paint.color = Color.parseColor("#00FF00") // Green color
        paint.strokeWidth = 30f
        canvas.drawArc(rect, 180f, percentage * 1.8f, false, paint)
    }

    fun setPercentage(percentage: Float) {
        this.percentage = percentage
        invalidate()
    }
}
