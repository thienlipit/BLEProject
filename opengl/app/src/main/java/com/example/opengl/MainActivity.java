package com.example.opengl;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.opengl.bouncycube.BouncyCubeActivity;
import com.example.opengl.databinding.ActivityMainBinding;
import com.example.opengl.polygon.Polygon;

public class MainActivity extends Activity {
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Button btn1 = binding.btn1;
        Button btn2 = binding.btn2;
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent intent = new Intent(getApplicationContext(), Polygon.class);
            startActivity(intent);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BouncyCubeActivity.class);
                startActivity(intent);
            }
        });

    }
}