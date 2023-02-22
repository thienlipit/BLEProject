package com.example.demoopengl.guideopengl

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.demoopengl.databinding.ActivityDemoShapeBinding


class demoShape : AppCompatActivity() {

    var count = 6f
    lateinit var bingding: ActivityDemoShapeBinding
//    private lateinit var gLView: GLSurfaceView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_demo_shape)
        bingding = ActivityDemoShapeBinding.inflate(layoutInflater)
        val view = bingding.root
//        gLView = CustomGLSurfaceView(this)
        setContentView(view)

        bingding.apply {
            btnIncrease.setOnClickListener {
                count += 1f
                tvResult.text = count.toString()

//                customGLSurfaceView.renderer.zoom = count
                customGLSurfaceView.renderer.zoom = count
//                Log.d("Main", customGLSurfaceView.zoomCustomGLSurfaceView.toString())

            }

            btnDecrease.setOnClickListener {
                count -= 1f
                tvResult.text = count.toString()

                customGLSurfaceView.renderer.zoom = count
//                customGLSurfaceView.zoomCustomGLSurfaceView = count
            }


        }

        Log.d("Main_Zoom", count.toString())



    }

}