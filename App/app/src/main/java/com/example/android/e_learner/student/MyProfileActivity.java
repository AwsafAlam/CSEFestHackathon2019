package com.example.android.e_learner.student;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.e_learner.R;

public class MyProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_activity_my_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
