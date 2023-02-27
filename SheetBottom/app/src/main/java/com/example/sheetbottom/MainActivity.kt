package com.example.sheetbottom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.sheetbottom.databinding.ActivityMainBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //#2 Initializing the BottomSheetBehavior
        bottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.bottomSheet))

//        bottomSheetBehavior.setPeekHeight(0)

        //#3 Listening to State Changes of BottomSheet
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
//                bottomSheetBehavior.setPeekHeight(100)
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
//                bottomSheetBehavior.setPeekHeight(100)
                binding.buttonBottomSheetPersistent.text = when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> "Close Persistent Bottom Sheet"
                    BottomSheetBehavior.STATE_COLLAPSED -> "Open Persistent Bottom Sheet"
                    else -> "Persistent Bottom Sheet"
                }
            }
        })

        //#4 Changing the BottomSheet State on ButtonClick
        binding.buttonBottomSheetPersistent.setOnClickListener {
            bottomSheetBehavior.peekHeight = 100
            val state =
                if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED)
                    BottomSheetBehavior.STATE_COLLAPSED
                else {
//                    bottomSheetBehavior.peekHeight = 100
                    BottomSheetBehavior.STATE_EXPANDED
                }
            bottomSheetBehavior.state = state
        }

    }
}