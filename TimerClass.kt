package com.muhammetkonukcu.android

import android.os.Handler
import android.os.Looper

/***
 * @author MuhammetKonukcu
 * createdAt 10.04.2025
 */

class TimerClass(listener: OnTimerTickListener) {

    interface OnTimerTickListener{
        fun onTimerTick(duration: String)
    }

    private var handler = Handler(Looper.getMainLooper())
    private var runnable: Runnable

    private var duration = 0L
    private var delay = 100L

    init{
        runnable = Runnable {
            duration += delay
            handler.postDelayed(runnable, delay)
            listener.onTimerTick(format())
        }
    }

    fun start(){
        handler.postDelayed(runnable, delay)
    }

    fun pause(){
        handler.removeCallbacks(runnable)
    }

    fun stop(){
        handler.removeCallbacks(runnable)
        duration = 0L
    }

    fun format(): String {
        val seconds = (duration / 1000) % 60
        val minutes = (duration / (1000 * 60)) % 60

        return "${minutes}:${"%02d".format(seconds)}"
    }

}
