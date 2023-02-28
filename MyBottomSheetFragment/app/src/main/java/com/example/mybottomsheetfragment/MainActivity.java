package com.example.mybottomsheetfragment;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button btnDemo;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyBottomSheetFragment myBottomSheetFragment = new MyBottomSheetFragment();
        myBottomSheetFragment.show(getSupportFragmentManager(), "AAA");

        btnDemo = findViewById(R.id.btn_demo);
        btnDemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TAG", "click btn");
                myBottomSheetFragment.show(getSupportFragmentManager(), "AAA");
            }
        });
    }
}