package com.example.android.e_learner.student;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.android.e_learner.MyConstants;
import com.example.android.e_learner.R;

public class DialActivity extends AppCompatActivity {

    FloatingActionButton fabCancel, fabVolumeChanger, fabVideoChanger;
    private String classSubject;
    TextView dialTextView;

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_activity_dial);
        dialTextView = findViewById(R.id.tv_subject);

        classSubject = "";

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
           if(extras != null){
                classSubject = extras.getString(MyConstants.DIAL_BUNDLE_KEY);
            }
        } else {
            classSubject = (String) savedInstanceState.getSerializable(MyConstants.DIAL_BUNDLE_KEY);
        }

        dialTextView.append(classSubject  + " teacher for you");


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        fabVideoChanger = findViewById(R.id.video_change);
        fabVideoChanger.setTag("0");
        fabCancel = findViewById(R.id.dial_cancel);
        fabVolumeChanger = findViewById(R.id.dial_mute);
        fabVolumeChanger.setTag("0"); // 0 means volume is currently normal ; 1 means currently off

        fabCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        fabVideoChanger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fabVideoChanger.getTag().equals("0")){
                    fabVideoChanger.setTag("1"); // 0 means video is currently normal ; 1 means currently off
                    fabVideoChanger.setImageDrawable(ContextCompat.getDrawable(DialActivity.this, R.drawable.ic_videocam_black_24dp));

                }else{
                    fabVideoChanger.setTag("0"); // 0 means video is currently normal ; 1 means currently off
                    fabVideoChanger.setImageDrawable(ContextCompat.getDrawable(DialActivity.this, R.drawable.ic_videocam_off_black_24dp));
                }
            }
        });



        fabVolumeChanger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fabVolumeChanger.getTag().equals("0")){
                    fabVolumeChanger.setTag("1"); // 0 means volume is currently normal ; 1 means currently off
                    fabVolumeChanger.setImageDrawable(ContextCompat.getDrawable(DialActivity.this, R.drawable.ic_mic_white_24dp));

                }else{
                    fabVolumeChanger.setTag("0"); // 0 means volume is currently normal ; 1 means currently off
                    fabVolumeChanger.setImageDrawable(ContextCompat.getDrawable(DialActivity.this, R.drawable.ic_mic_off_white_24dp));
                }
            }
        });



        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(DialActivity.this, FireVideo.class);
                startActivity(intent);
                finish();
            }
        }, 5000); // 4 seconds
    }
}
