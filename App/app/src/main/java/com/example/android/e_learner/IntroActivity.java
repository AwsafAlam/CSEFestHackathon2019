package com.example.android.e_learner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;


import com.example.android.e_learner.teacher.FireVideo;
import com.example.android.e_learner.student.MainActivity;
import com.muddzdev.styleabletoast.StyleableToast;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static android.content.Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED;
import static com.example.android.e_learner.MyConstants.PREF_KEY_USER_TYPE;
import static com.example.android.e_learner.MyConstants.PREF_VALUE_STUDENT;
import static com.example.android.e_learner.MyConstants.PREF_VALUE_TEACHER;
import static com.example.android.e_learner.MyConstants.PROFILE_PREFERENCES;

public class IntroActivity extends AppCompatActivity {


    private static final int RC_PERMISSION = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

    public void proceedAuthentication(View view) {
        Intent intent = null;

        if(((TextView)view).getText().equals(getString(R.string.student))){
            intent = new Intent(IntroActivity.this, MainActivity.class).addFlags(FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            startActivity(intent);
        }else{
            permissionCheckAndDial();
        }

    }



    private void permissionCheckAndDial(){
        String[] permissions = {
                android.Manifest.permission.RECORD_AUDIO,
                android.Manifest.permission.CAMERA
        };
        int MyVersion = Build.VERSION.SDK_INT;
        if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (!checkIfAlreadyhavePermission(permissions)) {
                requestForSpecificPermission(permissions);
            }else{
                Intent intent = new Intent(IntroActivity.this, FireVideo.class).addFlags(FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                startActivity(intent);
            }
        }
    }

    private boolean checkIfAlreadyhavePermission(String[] permissions) {

        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(IntroActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void requestForSpecificPermission(String[] permissions) {
        ActivityCompat.requestPermissions(this, permissions,   RC_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case RC_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                    Intent intent = new Intent(IntroActivity.this, FireVideo.class).addFlags(FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                    startActivity(intent);

                } else {
                    StyleableToast.makeText(IntroActivity.this, "You must grant permission to call!!", Toast.LENGTH_LONG, R.style.mytoast).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
