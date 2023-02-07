package com.example.demoopengl.guideopengl.guideshape

import android.opengl.GLES20
import android.util.Log
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

// number of coordinates per vertex in this array
const val COORDS_PER_VERTEX = 3
var triangleCoords = floatArrayOf(     // in counterclockwise order:
    0.0f, 0.622008459f, 0.0f,      // top
    -0.5f, -0.311004243f, 0.0f,    // bottom left
    0.5f, -0.311004243f, 0.0f      // bottom right
)

class Triangle {

   /* private val vertexShaderCode =
        "attribute vec4 vPosition;" +
                "void main() {" +
                "  gl_Position = vPosition;" +
                "}"*/

    //--start code Apply projection and camera transformations
    private val vertexShaderCode =
    // This matrix member variable provides a hook to manipulate
        // the coordinates of the objects that use this vertex shader
        "uniform mat4 uMVPMatrix;" +
                "attribute vec4 vPosition;" +
                "void main() {" +
                // the matrix must be included as a modifier of gl_Position
                // Note that the uMVPMatrix factor *must be first* in order
                // for the matrix multiplication product to be correct.
                "  gl_Position = uMVPMatrix * vPosition;" +
                "}"

    // Use to access and set the view transformation
    private var vPMatrixHandle: Int = 0
    //--end code Apply projection and camera transformations

    private val fragmentShaderCode =
        "precision mediump float;" +
                "uniform vec4 vColor;" +
                "void main() {" +
                "  gl_FragColor = vColor;" +
                "}"

    // Set color with red, green, blue and alpha (opacity) values
    var color = floatArrayOf(0.63671875f, 0.76953125f, 0.22265625f, 1.0f)

    private var vertexBuffer: FloatBuffer =
        // (number of coordinate values * 4 bytes per float)
        ByteBuffer.allocateDirect(triangleCoords.size * 4).run {
            // use the device hardware's native byte order
            order(ByteOrder.nativeOrder())

            // create a floating point buffer from the ByteBuffer
            asFloatBuffer().apply {
                // add the coordinates to the FloatBuffer
                put(triangleCoords)
                // set the buffer to read the first coordinate
                position(0)
            }
        }

    fun loadShader(type: Int, shaderCode: String): Int {

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        return GLES20.glCreateShader(type).also { shader ->

            // add the source code to the shader and compile it
            GLES20.glShaderSource(shader, shaderCode)
            GLES20.glCompileShader(shader)
        }
    }

    fun getVertexBuffer(coords: FloatArray): FloatBuffer {
        val b = ByteBuffer.allocateDirect(coords.size * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .put(coords)
        b.position(0)
        return b;

    }

    private var mProgram: Int

    init {
        val vertexShader: Int = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShader: Int = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)

//        Log.d("vertex", vertexShader.toString())
//        Log.d("fragment", fragmentShader.toString())
        // create empty OpenGL ES Program
        mProgram = GLES20.glCreateProgram().also {

            // add the vertex shader to program
            GLES20.glAttachShader(it, vertexShader)

            // add the fragment shader to program
            GLES20.glAttachShader(it, fragmentShader)

            // creates OpenGL ES program executables
            GLES20.glLinkProgram(it)
        }


    }

    private var positionHandle: Int = 0
    private var mColorHandle: Int = 0

    private val vertexCount: Int = triangleCoords.size / COORDS_PER_VERTEX
    private val vertexStride: Int = COORDS_PER_VERTEX * 4 // 4 bytes per vertex

    fun draw() {
        // Add program to OpenGL ES environment
        GLES20.glUseProgram(mProgram)

        // get handle to vertex shader's vPosition member
        positionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition").also {

            // Enable a handle to the triangle vertices
            GLES20.glEnableVertexAttribArray(it)

            // Prepare the triangle coordinate data
            GLES20.glVertexAttribPointer(
                it,
                COORDS_PER_VERTEX,
                GLES20.GL_FLOAT,
                false,
                vertexStride,
                vertexBuffer
            )

            // get handle to fragment shader's vColor member
            mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor").also { colorHandle ->

                // Set color for drawing the triangle
                GLES20.glUniform4fv(colorHandle, 1, color, 0)
            }

            // Draw the triangle
            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount)
            // Disable vertex array
            GLES20.glDisableVertexAttribArray(it)
        }
    }

    fun draw(mvpMatrix: FloatArray) { // pass in the calculated transformation matrix
        // Add program to OpenGL ES environment
        GLES20.glUseProgram(mProgram)

        // get handle to vertex shader's vPosition member
//        positionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition").also {
//
//            // Enable a handle to the triangle vertices
//            GLES20.glEnableVertexAttribArray(it)
//
//            // Prepare the triangle coordinate data
//            GLES20.glVertexAttribPointer(
//                it,
//                COORDS_PER_VERTEX,
//                GLES20.GL_FLOAT,
//                false,
//                vertexStride,
//                vertexBuffer
//            )
//
//            // get handle to fragment shader's vColor member
//            mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor").also { colorHandle ->
//
//                // Set color for drawing the triangle
//                GLES20.glUniform4fv(colorHandle, 1, color, 0)
//            }
//
//            //----start code for apply projection and camera view
//            // get handle to shape's transformation matrix
//            vPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix")
//
//            // Pass the projection and view transformation to the shader
//            GLES20.glUniformMatrix4fv(vPMatrixHandle, 1, false, mvpMatrix, 0)
//
//            // Draw the triangle
//            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount)
//
//            // Disable vertex array
//            GLES20.glDisableVertexAttribArray(positionHandle)
//            //----end code for apply projection and camera view
//        }


        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor")
        // Draw the triangle
        //GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount)
        GLES20.glEnableVertexAttribArray(positionHandle)

        vPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix")

            // Pass the projection and view transformation to the shader
        GLES20.glUniformMatrix4fv(vPMatrixHandle, 1, false, mvpMatrix, 0)

        (1..3).forEach { index ->
            color = floatArrayOf(0.2f * index, 0.76953125f, 0.22265625f, 1.0f)
            var triangleCoords = floatArrayOf(     // in counterclockwise order:
                0.0f, 1f, 0.0f,     // top
                -0.5f + (index / 0.2f ) , -0.311004243f, 0.0f,    // bottom left
                -0.5f + (index / 0.2f ) + index + 0.2f , -0.311004243f, 0.0f     // bottom right
            )
            val b = getVertexBuffer(triangleCoords)
            GLES20.glVertexAttribPointer(
                positionHandle,
                COORDS_PER_VERTEX,
                GLES20.GL_FLOAT,
                false,
                vertexStride,
                b
            )
            GLES20.glUniform4fv(mColorHandle, 1, color, 0)
            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount)
        }

        GLES20.glDisableVertexAttribArray(positionHandle)
    }
}