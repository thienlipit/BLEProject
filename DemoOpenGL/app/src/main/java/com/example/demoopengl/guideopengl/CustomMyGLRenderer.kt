package com.example.demoopengl.guideopengl

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.util.Log
import com.example.demoopengl.guideopengl.guideshape.*
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import kotlin.math.cos
import kotlin.math.sin
import kotlin.properties.Delegates

class CustomMyGLRenderer(var context: Context): GLSurfaceView.Renderer {
    var zoom = 6f

    private lateinit var mTriangle: Triangle
    private lateinit var mSquare: Square2
    private lateinit var mLoadMap: LoadMap

    private lateinit var newSquare: Square
    private lateinit var circle: Circle
    private lateinit var girdline: Girdline

    @Volatile
    var angle: Float = 0f
    override fun onSurfaceCreated(unused: GL10, config: EGLConfig) {
        // Set the background frame color
        GLES20.glClearColor(0.4f, 0.4f, 0.4f, 1.0f)

        // initialize a triangle
        mTriangle = Triangle()

        mLoadMap = LoadMap(context)
        // initialize a square
        mSquare = Square2()

        newSquare = Square()

        circle = Circle()
        girdline = Girdline()

    }

    private val rotationMatrix = FloatArray(16)
    override fun onDrawFrame(unused: GL10) {
        Log.d("aaa", zoom.toString())
        val ratio: Float = 1080f / 2022f
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1f, 1f, zoom, 1000f)
        // Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

        //-----------start define a camera view
        // Set the camera position (View matrix)
        Matrix.setLookAtM(viewMatrix, 0,
            0f, 0f, 10f,
            0f, 0f, 0f,
            0f, 1.0f, 0.0f)

        // Calculate the projection and view transformation
        Matrix.multiplyMM(vPMatrix, 0, projectionMatrix, 0, viewMatrix, 0)

        val scratch = FloatArray(16) //for MOTION
        // Create a rotation transformation for the triangle
//        val time = SystemClock.uptimeMillis() % 4000L
//        val angle = 0.090f * time.toInt()
        Matrix.setRotateM(rotationMatrix, 0, angle, 0f, 0f, -1.0f)
        Matrix.multiplyMM(scratch, 0, vPMatrix, 0, rotationMatrix, 0) //for motion
        //-----------end define camera view

//        mTriangle.draw()
//        mSquare.draw(scratch)
//        mStar.draw()

//        mTriangle.draw(vPMatrix) //without motion
//        mTriangle.draw(scratch)


//        mLoadMap.draw(scratch)
//        mTriangle.draw(scratch)

//        newSquare.draw(unused)
        circle.draw(scratch)
//        var Kq = demoShape().count
//        Log.d("TAG", Kq.toString())

//        girdline.draw(scratch, 3, 0.01f)


    }

    // Define a projection
    // vPMatrix is an abbreviation for "Model View Projection Matrix"
    private val vPMatrix = FloatArray(16)
    private val projectionMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        // Sets the current view port to the new size.
        Log.d("width + height", width.toString()+ "  " + height.toString())
        GLES20.glViewport(0, 0, width, height)

        //----start define projection
        val ratio: Float = width.toFloat() / height.toFloat()

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1f, 1f, zoom, 1000f)
        //----end define projection

        Log.d("Test", zoom.toString())

    }
}