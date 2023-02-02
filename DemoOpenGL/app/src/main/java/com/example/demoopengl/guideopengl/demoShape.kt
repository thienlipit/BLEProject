package com.example.demoopengl.guideopengl

import android.opengl.GLSurfaceView
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.demoopengl.R

class demoShape : AppCompatActivity() {
    private lateinit var gLView: GLSurfaceView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_demo_shape)
        gLView = CustomGLSurfaceView(this)
        setContentView(gLView)
    }
}