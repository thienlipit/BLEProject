package com.example.demoopengl.guideopengl

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet

class CustomGLSurfaceView: GLSurfaceView {
    private lateinit var renderer: CustomMyGLRenderer
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
        renderer = CustomMyGLRenderer()
        setRenderer(renderer)

    }
}