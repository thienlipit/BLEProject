package com.example.demoopengl

import android.opengl.GLES31
import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class OpenGLRender: GLSurfaceView.Renderer {
    private lateinit var mTriangle: Triangle
    private lateinit var mSquare: Square
    override fun onSurfaceCreated(p0: GL10?, p1: EGLConfig?) {
        // Set the background frame color
        val (r, g, b) = getColorRGB(255,255, 255)
        GLES31.glClearColor(r, g, b, 1f)

        // initialize a triangle
        mTriangle = Triangle()
        // initialize a square
        mSquare = Square()
    }

    override fun onSurfaceChanged(unused: GL10?, width: Int, height: Int) {
        GLES31.glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(p0: GL10?) {
        // Redraw background color
        GLES31.glClear(GLES31.GL_COLOR_BUFFER_BIT)
        mTriangle.draw()
    }

    private fun getColorRGB(red:Int, green: Int, blue: Int): Triple<Float, Float, Float> {
        return Triple( red/255f, green/255f, blue/255f)
    }


}