package com.example.g2048;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public abstract class SingleFragmentActivity extends AppCompatActivity {
    protected abstract Fragment createFragment();
    private final String Tag = "Fragment";
    private String FragmentName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(Tag,FragmentName + " onCreate()");
        setContentView(R.layout.activity_game);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);


        if (fragment == null) {
            fragment = createFragment();
            FragmentName = fragment.getClass().getName();
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(Tag,FragmentName + " onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(Tag,FragmentName + " onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(Tag,FragmentName + " onDestroy()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(Tag,FragmentName + " onResume()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(Tag,FragmentName + " onStart()");
    }
}
