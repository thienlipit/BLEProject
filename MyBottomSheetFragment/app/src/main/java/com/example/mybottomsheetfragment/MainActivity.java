package com.example.mybottomsheetfragment;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyBottomSheetFragment myBottomSheetFragment = new MyBottomSheetFragment();
        myBottomSheetFragment.show(getSupportFragmentManager(), "AAA");

//        MyBottomSheetFragment fragment = new MyBottomSheetFragment();
//        fragment.show(getSupportFragmentManager(), "TAG");
    }
}