package com.example.demoopengl

import android.content.Context
import android.graphics.BitmapFactory
import android.opengl.GLES20
import android.opengl.GLES20.*
import android.opengl.GLSurfaceView
import android.opengl.GLUtils
import android.opengl.Matrix
import com.example.demoopengl.shape.Square
import com.example.demoopengl.shape.Triangle
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


class MyGLRenderer: GLSurfaceView.Renderer {

    private var context: Context? = null

    private var uColorLocation = 0
    // vPMatrix is an abbreviation for "Model View Projection Matrix"
    private val vPMatrix = FloatArray(16)
    private val projectionMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)
    private val rotationMatrix = FloatArray(16)

    var translateX = 0f
    var translatey = 0f
    @Volatile
    var angle: Float = 0f

    private lateinit var mTriangle: Triangle
    private lateinit var mSquare: Square
    override fun onSurfaceCreated(p0: GL10?, p1: EGLConfig?) {
        // Set the background frame color
        val (r, g, b) = getColorRGB(0,0, 0)
        GLES20.glClearColor(r, g, b, 1f)

        // initialize a triangle
        mTriangle = Triangle()
        // initialize a square

        mSquare = Square()



//        val vertexShaderSource: String? = TextResourceReader().readTextFileFromResource(eglGetCurrentContext(), R.raw.simple_vertex_shader)
//        val fragmentShaderSource: String? =
//            context?.let { TextResourceReader().readTextFileFromResource(it, R.raw.simple_fragment_shader) }
//
//
//        val vertexShader: Int = ShaderHelper().compileVertexShader(vertexShaderSource)
//        val fragmentShader: Int = ShaderHelper().compileFragmentShader(fragmentShaderSource)
//        val program = ShaderHelper().linkProgram(vertexShader, fragmentShader);
//
//        uColorLocation = glGetUniformLocation(program, "u_Color");
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)

        val ratio: Float = width.toFloat() / height.toFloat()
        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
//        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 7f)
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1f, 1f, 1f, 1000f)

    }

    override fun onDrawFrame(gl: GL10?) {
        // Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

        // Set the camera position (View matrix)
        Matrix.setLookAtM(viewMatrix, 0,
            translateX, translatey, 1f,
            translateX, translatey, 0f,
            0f, 1.0f, 0.0f)

        // Calculate the projection and view transformation
        Matrix.multiplyMM(vPMatrix, 0, projectionMatrix, 0, viewMatrix, 0)

        val scratch = FloatArray(16)
        // Create a rotation transformation for the triangle
//        val time = SystemClock.uptimeMillis() % 4000L
//        val angle = 0.090f * time.toInt()
        Matrix.setRotateM(rotationMatrix, 0, angle, 0f, 0f, -1.0f)

        // Combine the rotation matrix with the projection and camera view
        // Note that the vPMatrix factor *must be first* in order
        // for the matrix multiplication product to be correct.
        Matrix.multiplyMM(scratch, 0, vPMatrix, 0, rotationMatrix, 0)

        // Draw triangle

        mSquare.draw(scratch)
//        mTriangle.draw(scratch)

//        mSquare.draw(vPMatrix)
//        mTriangle.draw(vPMatrix)

    }


    private fun getColorRGB(red:Int, green: Int, blue: Int): Triple<Float, Float, Float> {
        return Triple( red/255f, green/255f, blue/255f)
    }
}