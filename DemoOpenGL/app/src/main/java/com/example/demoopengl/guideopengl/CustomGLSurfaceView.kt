package com.example.demoopengl.guideopengl

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent


private const val TOUCH_SCALE_FACTOR: Float = 180.0f / 320f
private var previousX: Float = 0f
private var previousY: Float = 0f

class CustomGLSurfaceView: GLSurfaceView {
     lateinit var renderer: CustomMyGLRenderer
    constructor(context: Context?) : super(context) {
        init()
        renderMode = RENDERMODE_WHEN_DIRTY
    }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }
    private fun init(){
        setEGLContextClientVersion(2)
        preserveEGLContextOnPause = true
        renderer = CustomMyGLRenderer(context)
        setRenderer(renderer)

    }

    /*override fun onTouchEvent(e: MotionEvent): Boolean {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.

        val x: Float = e.x
        val y: Float = e.y
        val rawx: Float = e.rawX
        val rawy: Float = e.rawY
        Log.e("x", x.toString())
        Log.e("y", y.toString())
        Log.e("rawX", rawx.toString())
        Log.e("rawY", rawy.toString())
        Log.e("action", e.action.toString())
        Log.e("actionMasked", e.actionMasked.toString())

        when (e.action) {
            MotionEvent.ACTION_MOVE -> {

                var dx: Float = x - previousX
                var dy: Float = y - previousY

                // reverse direction of rotation above the mid-line
                if (y > height / 2) {
                    dx *= -1
                }

                // reverse direction of rotation to left of the mid-line
                if (x < width / 2) {
                    dy *= -1
                }

                renderer.angle += (dx + dy) * TOUCH_SCALE_FACTOR
                requestRender()
            }
        }

        previousX = x
        previousY = y
        return true
    }*/
}