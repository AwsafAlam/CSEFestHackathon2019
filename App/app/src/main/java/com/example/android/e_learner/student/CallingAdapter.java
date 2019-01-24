package com.example.android.e_learner.student;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.Window;
import android.view.WindowManager;

public class CallingAdapter extends FragmentPagerAdapter {

    private Activity activity;

    private static int[]  lst_backgroundcolor = {
            Color.rgb(55,55,55),
            Color.rgb(103,58,183),
            Color.rgb(255,152,0),
            Color.rgb(1,188,212)
    };

    private static String[] lst_status_bar_color = {
              "#2A2A2A", "#512DA8", "#F57C00"
    };

    public CallingAdapter(FragmentManager fragmentManager, Activity activity) {

        super(fragmentManager);
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                updateStatusBarColor("#2A2A2A");
                return LevelFragment.init(lst_backgroundcolor[position%4], lst_status_bar_color[position%4], activity);
            case 1:
//                updateStatusBarColor("#5B4875");
//                updateStatusBarColor("#2A2A2A");
                return SubjectChoiceFragment.init(lst_backgroundcolor[position%4], lst_status_bar_color[position%4],activity);
            case 2:
                return CallFragment.init(lst_backgroundcolor[position%4], lst_status_bar_color[position%4],activity);
            default:// Fragment # 2-9 - Will show list
                return null;
        }
    }

    public void updateStatusBarColor(String color){// Color must be in hexadecimal fromat
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(color));
        }
    }



}
