package com.example.demoopengl

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.demoopengl.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var openGLV: OpenGLView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        openGLV = binding.openGLView


    }

    override fun onResume() {
        super.onResume()
        openGLV.onResume()
    }

    override fun onPause() {
        super.onPause()
        openGLV.onPause()
    }
}