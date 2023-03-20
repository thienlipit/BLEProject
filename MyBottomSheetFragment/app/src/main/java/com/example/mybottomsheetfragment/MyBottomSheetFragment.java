package com.example.mybottomsheetfragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class MyBottomSheetFragment extends BottomSheetDialogFragment {
    private BottomSheetBehavior bottomSheetBehavior;
    private Button btnLogin;
    private ImageView header_Arrow_Image;
//    private FragmentController fragmentController;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_bottom_sheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnLogin = view.findViewById(R.id.btnLogin);
        header_Arrow_Image = view.findViewById(R.id.bottom_sheet_arrow);

        bottomSheetBehavior = BottomSheetBehavior.from(view.findViewById(R.id.bottom_sheet_layout));

        bottomSheetBehavior.setPeekHeight(450);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Dialog", "clicked");
                bottomSheetBehavior.setPeekHeight(450);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("KK", "login btn clicked");
            }
        });
//        getActivity().getFragmentManager().beginTransaction().remove(this).commit();

        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if( newState == BottomSheetBehavior.STATE_HIDDEN){
                    bottomSheetBehavior.setPeekHeight(450);
                    dismiss();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                Log.d("aaaa", "run at onSlide");
                header_Arrow_Image.setRotation(slideOffset * 180);
            }
        });
    }

    public BottomSheetBehavior getBottomSheetBehavior() {
        return bottomSheetBehavior;
    }
}