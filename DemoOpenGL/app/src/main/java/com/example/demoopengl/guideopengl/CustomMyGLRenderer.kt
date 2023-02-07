package com.example.demoopengl.guideopengl

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.util.Log
import com.example.demoopengl.guideopengl.guideshape.LoadMap
import com.example.demoopengl.guideopengl.guideshape.Square
import com.example.demoopengl.guideopengl.guideshape.Square2
import com.example.demoopengl.guideopengl.guideshape.Triangle
import java.lang.Float.toString
import java.util.Arrays.deepToString
import java.util.Arrays.toString
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import kotlin.Unit.toString

class CustomMyGLRenderer(var context: Context): GLSurfaceView.Renderer {
    private lateinit var mTriangle: Triangle
    private lateinit var mSquare: Square2
    private lateinit var mLoadMap: LoadMap

    private lateinit var newSquare: Square

    @Volatile
    var angle: Float = 0f
    override fun onSurfaceCreated(unused: GL10, config: EGLConfig) {
        // Set the background frame color
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f)

        // initialize a triangle
        mTriangle = Triangle()

        mLoadMap = LoadMap(context)
        // initialize a square
        mSquare = Square2()

        newSquare = Square()

    }


    private val rotationMatrix = FloatArray(16)
    override fun onDrawFrame(unused: GL10) {
        // Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

        //-----------start define a camera view
        // Set the camera position (View matrix)
        Matrix.setLookAtM(viewMatrix, 0,
            0f, 0f, 5f,
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

        //mTriangle.draw()
//        mSquare.draw()
//        mStar.draw()


//        mTriangle.draw(vPMatrix) //without motion
//        mTriangle.draw(scratch)


        //mLoadMap.draw(scratch)
//        mTriangle.draw(scratch)

        newSquare.draw(unused)

    }


    // Define a projection
    // vPMatrix is an abbreviation for "Model View Projection Matrix"
    private val vPMatrix = FloatArray(16)
    private val projectionMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        // Sets the current view port to the new size.
        GLES20.glViewport(0, 0, width, height)

//        gl.glMatrixMode(GL10.GL_PROJECTION)
        for (i in 0..15){
            Log.d("Proj $i", projectionMatrix[i].toString())
        }

        //----start define projection
        val ratio: Float = width.toFloat() / height.toFloat()

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1f, 1f, 5f, 10f)
        //----end define projection

    }
}