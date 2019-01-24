package com.example.android.e_learner.student;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;

import com.example.android.e_learner.R;

import java.util.Random;

import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;

public class FireVideo extends AppCompatActivity  {

    //RtcEngine is the main class for agora
    private RtcEngine mRtcEngine;
    FloatingActionButton fabCancel, fabVolumeChanger, fabVideoChanger;

    //IRtcEngineEventHandler will inform the client when someone joins
    // or leaves the channel or when a video stream is received and a bunch
    //of other events
    private IRtcEngineEventHandler mRtcEventHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_activity_fire_video);

        fabVideoChanger = findViewById(R.id.video_change);
        fabVideoChanger.setTag("0");
        fabCancel = findViewById(R.id.dial_cancel);
        fabVolumeChanger = findViewById(R.id.dial_mute);
        fabVolumeChanger.setTag("0"); // 0 means volume is currently normal ; 1 means currently off

        fabCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                leaveChannel();
                finish();
            }
        });


        fabVideoChanger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fabVideoChanger.getTag().equals("0")){
                    fabVideoChanger.setTag("1"); // 0 means video is currently normal ; 1 means currently off
                    fabVideoChanger.setImageDrawable(ContextCompat.getDrawable(FireVideo.this, R.drawable.ic_videocam_black_24dp));

                }else{
                    fabVideoChanger.setTag("0"); // 0 means video is currently normal ; 1 means currently off
                    fabVideoChanger.setImageDrawable(ContextCompat.getDrawable(FireVideo.this, R.drawable.ic_videocam_off_black_24dp));
                }
            }
        });



        fabVolumeChanger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fabVolumeChanger.getTag().equals("0")){
                    fabVolumeChanger.setTag("1"); // 0 means volume is currently normal ; 1 means currently off
                    fabVolumeChanger.setImageDrawable(ContextCompat.getDrawable(FireVideo.this, R.drawable.ic_mic_white_24dp));

                }else{
                    fabVolumeChanger.setTag("0"); // 0 means volume is currently normal ; 1 means currently off
                    fabVolumeChanger.setImageDrawable(ContextCompat.getDrawable(FireVideo.this, R.drawable.ic_mic_off_white_24dp));
                }
            }
        });

        mRtcEventHandler = new IRtcEngineEventHandler() {


            //This event is called when we receive a video stream. And we gonna get
            // the uid and stream, then display it in our FrameLayout
            @Override
            public void onFirstRemoteVideoDecoded(final int uid, int width, int height, int elapsed) {
                Log.i("uid video",uid+"");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setupRemoteVideo(uid);
                    }
                });
            }


        };
        initializeAgoraEngine();

    }

    @Override
    protected void onPause() {
        super.onPause();
        leaveChannel();
    }

    private void initializeAgoraEngine() {
        try {
            mRtcEngine = RtcEngine.create(getBaseContext(), getString(R.string.agora_app_id), mRtcEventHandler);
            joinChannel();
            setupLocalVideo();
            setupVideoProfile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Video quality or speed
    private void setupVideoProfile() {
        mRtcEngine.enableVideo();
        mRtcEngine.setVideoProfile(Constants.VIDEO_PROFILE_360P, false);
    }

    //assign local front camera to FrameLayout
    private void setupLocalVideo() {
        FrameLayout container = (FrameLayout) findViewById(R.id.local_video_view_container);
        SurfaceView surfaceView = RtcEngine.CreateRendererView(getBaseContext());
        surfaceView.setZOrderMediaOverlay(true);
        container.addView(surfaceView);
        mRtcEngine.setupLocalVideo(new VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_ADAPTIVE, 0));
    }

    private void joinChannel() {
        // 4th param is UID which should be unique for each user
        mRtcEngine.joinChannel(null, "aye", "Extra Optional Data", new Random().nextInt(10000000)+1); // if you do not specify the uid, Agora will assign one.
    }

    private void setupRemoteVideo(int uid) {
        FrameLayout container = (FrameLayout) findViewById(R.id.remote_video_view_container);

        if (container.getChildCount() >= 1) {
            return;
        }

        SurfaceView surfaceView = RtcEngine.CreateRendererView(getBaseContext());
        container.addView(surfaceView);
        mRtcEngine.setupRemoteVideo(new VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_ADAPTIVE, uid));
        surfaceView.setTag(uid);

    }

    private void leaveChannel() {
        mRtcEngine.leaveChannel();
    }
}
