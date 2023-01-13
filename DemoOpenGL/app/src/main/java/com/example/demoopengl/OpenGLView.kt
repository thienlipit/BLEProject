package com.example.demoopengl


import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet

class OpenGLView : GLSurfaceView {
    constructor(context: Context?) : super(context) {
        init()
    }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }
        private fun init(){
        setEGLContextClientVersion(2)
        preserveEGLContextOnPause = true
        setRenderer(OpenGLRender())
    }
}