package com.example.g2048;



import androidx.fragment.app.Fragment;

public class GameActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new GameFragment();
    }
}
