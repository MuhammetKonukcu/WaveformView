package com.muhammetkonukcu.android

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.muhammetkonukcu.android.R

/***
 * @author MuhammetKonukcu
 * createdAt 10.04.2025
 */

class WaveformView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private var barColor: Int = Color.rgb(21, 171, 255)
    private var barRadius: Float = 6f
    private var barWidth: Float = 9f
    private var barSpacing: Float = 6f
    private var smoothingWindowSize: Int = 2

    init {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.WaveformView, 0, 0)
            try {
                barColor = typedArray.getColor(R.styleable.WaveformView_barColor, barColor)
                barRadius = typedArray.getDimension(R.styleable.WaveformView_barRadius, barRadius)
                barWidth = typedArray.getDimension(R.styleable.WaveformView_barWidth, barWidth)
                barSpacing = typedArray.getDimension(R.styleable.WaveformView_barSpacing, barSpacing)
                smoothingWindowSize = typedArray.getInt(R.styleable.WaveformView_smoothingWindowSize, smoothingWindowSize)
            } finally {
                typedArray.recycle()
            }
        }
    }

    private val paint = Paint().apply {
        color = barColor
        isAntiAlias = true
        style = Paint.Style.FILL
    }

    private val amplitudes = ArrayList<Float>()
    private val spikes = ArrayList<RectF>()

    private val screenHeight = 400f
    private val screenWidth = resources.displayMetrics.widthPixels.toFloat()
    private val maxSpikes = (screenWidth / (barWidth + barSpacing)).toInt()

    fun addAmplitude(amp: Float) {
        val normalizedAmplitude = (amp / 7).coerceAtMost(screenHeight)
        amplitudes.add(normalizedAmplitude)

        val smoothedAmplitude = if (amplitudes.size >= smoothingWindowSize) {
            amplitudes.takeLast(smoothingWindowSize).average().toFloat()
        } else {
            normalizedAmplitude
        }

        val smoothedAmplitudes = amplitudes.takeLast(maxSpikes).toMutableList()
        smoothedAmplitudes[smoothedAmplitudes.lastIndex] = smoothedAmplitude

        spikes.clear()
        smoothedAmplitudes.forEachIndexed { index, amplitude ->
            val left = index * (barWidth + barSpacing)
            val top = screenHeight / 2 - amplitude / 2
            val right = left + barWidth
            val bottom = top + amplitude
            spikes.add(RectF(left, top, right, bottom))
        }

        invalidate()
    }

    fun clear(): List<Float> {
        val ampsCopy = amplitudes.toList()
        amplitudes.clear()
        spikes.clear()
        invalidate()
        return ampsCopy
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        spikes.forEach { rect ->
            canvas.drawRoundRect(rect, barRadius, barRadius, paint)
        }
    }
}

