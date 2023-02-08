package com.example.demoopengl.guideopengl.guideshape

import android.opengl.GLES20
import android.util.Log
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer

//var squareCoordsLine = floatArrayOf(
//    -0.05f,  0.05f, 0.0f,      // top left
//    -0.05f, -0.05f, 0.0f,      // bottom left
//    0.05f, -0.05f, 0.0f,      // bottom right
//    0.05f,  0.05f, 0.0f       // top right
//)
var squareCoordsLine = floatArrayOf(
    0f,  0f, 0.0f,      // top left
    0f, 0f, 0.0f,      // bottom left
    0f, 0f, 0.0f,      // bottom right
    0f,  0f, 0.0f       // top right
)
class Girdline {
    private val vertexShaderCode =
        "uniform mat4 uMVPMatrix;" +
                "attribute vec4 vPosition;" +
                "void main() {" +
                "  gl_Position = uMVPMatrix * vPosition;" +
                "}"

    private val fragmentShaderCode =
        "precision mediump float;" +
                "uniform vec4 vColor;" +
                "void main() {" +
                "  gl_FragColor = vColor;" +
                "}"

    //    private val drawOrder = shortArrayOf(0, 1, 2, 0, 2, 3) // order to draw vertices
    private val drawOrder = shortArrayOf(0, 1, 2, 0, 2, 3) // order to draw vertices

    // initialize vertex byte buffer for shape coordinates
    private val vertexBuffer: FloatBuffer =
        // (# of coordinate values * 4 bytes per float)
        ByteBuffer.allocateDirect(squareCoordsLine.size * 4).run {
            order(ByteOrder.nativeOrder())
            asFloatBuffer().apply {
                put(squareCoordsLine)
                position(0)
            }
        }

    // initialize byte buffer for the draw list
    private val drawListBuffer: ShortBuffer =
        // (# of coordinate values * 2 bytes per short)
        ByteBuffer.allocateDirect(drawOrder.size * 2).run {
            order(ByteOrder.nativeOrder())
            asShortBuffer().apply {
                put(drawOrder)
                position(0)
            }
        }

    // Set color with red, green, blue and alpha (opacity) values
    var color = floatArrayOf(0.113f, 0.431f, 0.929f, 1.0f)


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
        return b
    }

    private var mProgram: Int

    init {
        val vertexShader: Int = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShader: Int = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)
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
    private var vPMatrixHandle: Int = 0

    private val vertexCount: Int = triangleCoords.size / COORDS_PER_VERTEX
    private val vertexStride: Int = COORDS_PER_VERTEX * 4 // 4 bytes per vertex


    fun draw(mvpMatrix: FloatArray, size: Int, lineWidth: Float) {
        // Add program to OpenGL ES environment
        GLES20.glUseProgram(mProgram)
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor")

        //GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount)
        GLES20.glEnableVertexAttribArray(positionHandle)

        vPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix")

        // Pass the projection and view transformation to the shader
        GLES20.glUniformMatrix4fv(vPMatrixHandle, 1, false, mvpMatrix, 0)


        /*float[] vertices = new float[]{
            x, y, 0f,
            x, y - h, 0.0f,
            x + w, y - h, 0.0f,
            x + w, y, 0.0f
        };
        float x = -size / 2f + i, y = size / 2f;
        vert: x, y, lineWidth, size
        */
        (0..size).forEach { index ->
            val x = (-size/2f + index)/2f
            val y = (size/2f)
            var add = index/10f
            color = floatArrayOf(1f, 1f, 1f, 1.0f)

            triangleCoords = floatArrayOf(
                x ,  y, 0.0f,      // top left
                x , y - size.toFloat() /2f, 0.0f,      // bottom left
                x + lineWidth , y - size.toFloat() /2f, 0.0f,      // bottom right
                x + lineWidth ,  y, 0.0f       // top right
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

            GLES20.glDrawElements(
                GLES20.GL_TRIANGLES, drawOrder.size,
                GLES20.GL_UNSIGNED_SHORT, drawListBuffer)

        }

//        (0..loopIndex).forEach { index ->
//            var add = index/10f
//            color = floatArrayOf(1f, 1f, 1f, 1.0f)
//
//             triangleCoords = floatArrayOf(
//                -0.5f  ,  0.5f - add, 0.0f,      // top left
//                -0.5f, 0.498f - add, 0.0f,      // bottom left
//                0.5f , 0.498f - add, 0.0f,      // bottom right
//                0.5f ,  0.5f - add, 0.0f       // top right
//            )
//
//            val b = getVertexBuffer(triangleCoords)
//
//            GLES20.glVertexAttribPointer(
//                positionHandle,
//                COORDS_PER_VERTEX,
//                GLES20.GL_FLOAT,
//                false,
//                vertexStride,
//                b
//            )
//
//            GLES20.glUniform4fv(mColorHandle, 1, color, 0)
//
//            GLES20.glDrawElements(
//                GLES20.GL_TRIANGLES, drawOrder.size,
//                GLES20.GL_UNSIGNED_SHORT, drawListBuffer)
//
//        }
        GLES20.glDisableVertexAttribArray(positionHandle)

    }
}