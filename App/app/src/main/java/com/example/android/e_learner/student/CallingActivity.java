package com.example.android.e_learner.student;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;

import com.example.android.e_learner.R;

public class CallingActivity extends FragmentActivity {
    static final int ITEMS = 10;
    CallingAdapter mAdapter;
    ViewPager mPager;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_fragment_calling_activity);
        mAdapter = new CallingAdapter(getSupportFragmentManager(), CallingActivity.this);
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);

        FloatingActionButton fabBack = (FloatingActionButton) findViewById(R.id.first);

        fabBack.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                mPager.setCurrentItem(mPager.getCurrentItem() - 1);
            }
        });

        FloatingActionButton fabnext = (FloatingActionButton) findViewById(R.id.last);
        fabnext.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                mPager.setCurrentItem(mPager.getCurrentItem() + 1);
            }
        });

    }


    public void updateStatusBarColor(String color){// Color must be in hexadecimal fromat
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(color));
        }
    }





}