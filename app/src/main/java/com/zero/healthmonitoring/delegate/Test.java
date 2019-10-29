package com.zero.healthmonitoring.delegate;

import android.app.Activity;
import android.app.Application;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class Test extends AppCompatActivity {

    private final static int PERMISSION_RESULT_CODE = 0x100;

    private FragmentStatePagerAdapter adapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
        @Override
        public Fragment getItem(int position) {
            return null;
        }

        @Override
        public int getCount() {
            return 0;
        }
    };

}
