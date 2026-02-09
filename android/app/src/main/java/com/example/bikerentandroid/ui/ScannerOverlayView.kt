package com.example.bikerentandroid.ui


import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class ScannerOverlayView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private val overlayPaint = Paint().apply {
        color = Color.parseColor("#80000000")
    }

    private val clearPaint = Paint().apply {
        xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }

    private val borderPaint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 6f
        color = Color.WHITE
        isAntiAlias = true
    }

    init {
        setLayerType(LAYER_TYPE_HARDWARE, null)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val w = width.toFloat()
        val h = height.toFloat()

        val scanSize = w * 0.65f
        val left = (w - scanSize) / 2
        val top = (h - scanSize) / 2
        val right = left + scanSize
        val bottom = top + scanSize

        canvas.drawRect(0f, 0f, w, h, overlayPaint)
        canvas.drawRect(left, top, right, bottom, clearPaint)
        canvas.drawRect(left, top, right, bottom, borderPaint)
    }
}
