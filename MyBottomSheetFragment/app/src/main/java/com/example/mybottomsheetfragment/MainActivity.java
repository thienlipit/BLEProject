package com.example.mybottomsheetfragment;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private FragmentController fragmentController;
    private Button btnDemo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentController = new FragmentController(this);

        MyBottomSheetFragment myBottomSheetFragment = new MyBottomSheetFragment();

        fragmentController.show(myBottomSheetFragment, "aaa", R.id.view_more_info);

        btnDemo = findViewById(R.id.btn_demo);
        btnDemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TAG", "click btn");
                myBottomSheetFragment.getBottomSheetBehavior().setPeekHeight(450);
                fragmentController.show(myBottomSheetFragment, "aaa", R.id.view_more_info);

            }
        });
    }
}