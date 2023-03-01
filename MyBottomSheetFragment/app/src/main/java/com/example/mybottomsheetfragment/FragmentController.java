package com.example.mybottomsheetfragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.lang.ref.WeakReference;

public class FragmentController {
    private WeakReference<AppCompatActivity> activity;
    private FragmentManager fragmentManager;

    public FragmentController(AppCompatActivity activity) {
        this.activity = new WeakReference<>(activity);
        this.fragmentManager = activity
                .getSupportFragmentManager();
    }

    public void show(Fragment fragment, String tag, int target) {
        if (fragment == null) {
            clear();
            return;
        }

        FragmentTransaction transaction = fragmentManager
                .beginTransaction();

        transaction
                .replace(target, fragment, tag)
                .commit();
    }

    public void clear() {
        FragmentTransaction transaction = fragmentManager
                .beginTransaction();
        for(Fragment fragment: fragmentManager.getFragments()) {
            transaction.remove(fragment);
        }

        transaction.commit();
    }
}