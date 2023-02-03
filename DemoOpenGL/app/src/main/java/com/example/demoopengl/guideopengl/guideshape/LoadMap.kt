package com.example.demoopengl.guideopengl.guideshape

import android.content.Context
import android.graphics.BitmapFactory
import android.opengl.GLES20
import android.opengl.GLUtils
import android.util.Log
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer

private val QUADRANT_COORDINATES = floatArrayOf(
    //x,    y
    -0.5f, 0.5f,
    -0.5f, -0.5f,
    0.5f, -0.5f,
    0.5f, 0.5f,
)

private val TEXTURE_COORDINATES = floatArrayOf(
    //x,    y
    0.0f, 1.0f,
    0.0f, 0.0f,
    1.0f, 0.0f,
    1.0f, 1.0f,
)
private val DRAW_ORDER = shortArrayOf(0, 1, 2, 0, 2, 3)
private val vPMatrix = FloatArray(16)
private val projectionMatrix = FloatArray(16)
private val viewMatrix = FloatArray(16)

private var quadPositionHandle = -1
private var texPositionHandle = -1
private var textureUniformHandle: Int = -1
private var viewProjectionMatrixHandle: Int = -1
private var program: Int = -1
private val textureUnit = IntArray(1)
private const val COORDINATES_PER_VERTEX = 2
private const val VERTEX_STRIDE: Int = COORDINATES_PER_VERTEX * 4


class LoadMap(context: Context) {
    private val vertexShaderCode =
        "uniform mat4 uVPMatrix;" +
        "attribute vec4 a_Position;" +
        "attribute vec2 a_TexCoord;" +
        "varying vec2 v_TexCoord;" +
                "void main(void) {" +
                "  gl_Position = uVPMatrix * a_Position;" +
                "  v_TexCoord = vec2(a_TexCoord.x, (1.0 - (a_TexCoord.y)));" +
                "}"

    private val fragmentShaderCode =
        "precision highp float;" +
                "uniform sampler2D u_Texture;" +
                "varying vec2 v_TexCoord;" +
                "void main(void) {" +
                "  gl_FragColor = texture2D(u_Texture, v_TexCoord);" +
                "}"

//    private val vertexShaderCode =
//        "attribute vec4 vPosition;" +
//                "void main() {" +
//                "  gl_Position = vPosition;" +
//                "}"
//
//    private val fragmentShaderCode =
//        "precision mediump float;" +
//                "uniform vec4 vColor;" +
//                "void main() {" +
//                "  gl_FragColor = vColor;" +
//                "}"

    // Set color with red, green, blue and alpha (opacity) values
    val color = floatArrayOf(0.63671875f, 0.76953125f, 0.22265625f, 1.0f)

    private var quadrantCoordinatesBuffer: FloatBuffer =
        // (number of coordinate values * 4 bytes per float)
        ByteBuffer.allocateDirect(QUADRANT_COORDINATES.size * 4).run {
            // use the device hardware's native byte order
            order(ByteOrder.nativeOrder())

            // create a floating point buffer from the ByteBuffer
            asFloatBuffer().apply {
                // add the coordinates to the FloatBuffer
                put(QUADRANT_COORDINATES)
                // set the buffer to read the first coordinate
                position(0)
            }
        }

    private var textureCoordinatesBuffer: FloatBuffer =
        // (number of coordinate values * 4 bytes per float)
        ByteBuffer.allocateDirect(TEXTURE_COORDINATES.size * 4).run {
            // use the device hardware's native byte order
            order(ByteOrder.nativeOrder())

            // create a floating point buffer from the ByteBuffer
            asFloatBuffer().apply {
                // add the coordinates to the FloatBuffer
                put(TEXTURE_COORDINATES)
                // set the buffer to read the first coordinate
                position(0)
            }
        }

    /**
     * Convert short array to short buffer
     */
    private val drawOrderBuffer: ShortBuffer = ByteBuffer.allocateDirect(DRAW_ORDER.size * 2).run {
        order(ByteOrder.nativeOrder())
        asShortBuffer().apply {
            put(DRAW_ORDER)
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

    init {

        val vertexShader: Int = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShader: Int = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)

        Log.d("ASBD", vertexShader.toString())
        Log.d("ASBD11", fragmentShader.toString())
        // create empty OpenGL ES Program
        program = GLES20.glCreateProgram().also {

            // add the vertex shader to program
            GLES20.glAttachShader(it, vertexShader)

            // add the fragment shader to program
            GLES20.glAttachShader(it, fragmentShader)

            // creates OpenGL ES program executables
            GLES20.glLinkProgram(it)
        }

        //Quadrant position handler
        quadPositionHandle = GLES20.glGetAttribLocation(program, "a_Position")

        //Texture position handler
        texPositionHandle = GLES20.glGetAttribLocation(program, "a_TexCoord")

        //Texture uniform handler
        textureUniformHandle = GLES20.glGetUniformLocation(program, "u_Texture")

        //View projection transformation matrix handler
        viewProjectionMatrixHandle = GLES20.glGetUniformLocation(program, "uVPMatrix")

        //Enable blend
        GLES20.glEnable(GLES20.GL_BLEND)
        //Uses to prevent transparent area to turn in black
        GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA)

        // Read the texture.
        val textureBitmap =
            BitmapFactory.decodeStream(context.assets.open("models/sample1.jpg"))

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glGenTextures(textureUnit.size, textureUnit, 0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureUnit[0])

        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, textureBitmap, 0)
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D)

        textureBitmap.recycle()
    }

    fun draw(mvpMatrix: FloatArray) {
        try {
            Log.d("program", program.toString())
            GLES20.glUseProgram(program)

            // Attach the object texture.
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureUnit[0])
            GLES20.glUniform1i(textureUniformHandle, 0)

            // Pass the projection and view transformation to the shader
            GLES20.glUniformMatrix4fv(viewProjectionMatrixHandle, 1, false, mvpMatrix, 0)

            //Pass quadrant position to shader
            GLES20.glVertexAttribPointer(
                quadPositionHandle,
                COORDINATES_PER_VERTEX,
                GLES20.GL_FLOAT,
                false,
                VERTEX_STRIDE,
                quadrantCoordinatesBuffer
            )

            //Pass texture position to shader
            GLES20.glVertexAttribPointer(
                texPositionHandle,
                COORDINATES_PER_VERTEX,
                GLES20.GL_FLOAT,
                false,
                VERTEX_STRIDE,
                textureCoordinatesBuffer
            )

            // Enable attribute handlers
            GLES20.glEnableVertexAttribArray(quadPositionHandle)
            GLES20.glEnableVertexAttribArray(texPositionHandle)

            //Draw shape
            GLES20.glDrawElements(
                GLES20.GL_TRIANGLES,
                DRAW_ORDER.size,
                GLES20.GL_UNSIGNED_SHORT,
                drawOrderBuffer
            )

            // Disable vertex arrays
            GLES20.glDisableVertexAttribArray(quadPositionHandle)
            GLES20.glDisableVertexAttribArray(texPositionHandle)

//            ShaderUtil.checkGLError(TAG, "After draw")
        } catch (t: Throwable) {
            // Avoid crashing the application due to unhandled exceptions.
        }
    }
}