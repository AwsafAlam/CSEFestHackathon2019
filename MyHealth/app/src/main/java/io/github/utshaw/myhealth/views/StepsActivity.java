package io.github.utshaw.myhealth.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import io.github.utshaw.myhealth.BuildConfig;
import io.github.utshaw.myhealth.Database;
import io.github.utshaw.myhealth.R;
import io.github.utshaw.myhealth.SensorListener;
import io.github.utshaw.myhealth.util.API26Wrapper;
import io.github.utshaw.myhealth.util.Logger;
import io.github.utshaw.myhealth.util.Util;

public class StepsActivity extends AppCompatActivity implements SensorEventListener {
    private int todayOffset, total_start, goal, since_boot, total_days;
    Database db;
    private static TextView text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);

        text = findViewById(R.id.detail);

        if (Build.VERSION.SDK_INT >= 26) {
            API26Wrapper.startForegroundService(this,
                    new Intent(this, SensorListener.class));
        } else {
            startService(new Intent(this, SensorListener.class));
        }
        db = Database.getInstance(this);

        SharedPreferences prefs =
                getSharedPreferences("healthApp", Context.MODE_PRIVATE);

        since_boot = db.getCurrentSteps();
        int saved = db.getDaysWithoutToday();
        updateUI(since_boot, saved);


        // register a sensorlistener to live update the UI if a step is taken
        SensorManager sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = sm.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (sensor == null) {
            new AlertDialog.Builder(this).setTitle("No sensor")
                    .setMessage("Hardware step sensor not found")
                    .setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(final DialogInterface dialogInterface) {
                            //finish();
                        }
                    }).setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).create().show();
        } else {
            sm.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI, 0);
        }


        //since_boot -= pauseDifference;

        total_start = db.getTotalWithoutToday();
        total_days = db.getDays();

        db.close();

        //stepsDistanceChanged();
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            SensorManager sm =
                    (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            sm.unregisterListener(this);
        } catch (Exception e) {
            if (BuildConfig.DEBUG) Logger.log(e);
        }
        Database db = Database.getInstance(this);
        db.saveCurrentSteps(since_boot);
        db.close();
    }

    @Override
    public void onAccuracyChanged(final Sensor sensor, int accuracy) {
        // won't happen
    }

    @Override
    public void onSensorChanged(final SensorEvent event) {
        /*if (BuildConfig.DEBUG) Logger.log(
                "UI - sensorChanged | todayOffset: " + todayOffset + " since boot: " +
                        event.values[0]);
        if (event.values[0] > Integer.MAX_VALUE || event.values[0] == 0) {
            return;
        }
        if (todayOffset == Integer.MIN_VALUE) {
            // no values for today
            // we dont know when the reboot was, so set todays steps to 0 by
            // initializing them with -STEPS_SINCE_BOOT
            todayOffset = -(int) event.values[0];
            Database db = Database.getInstance(getActivity());
            db.insertNewDay(Util.getToday(), (int) event.values[0]);
            db.close();
        }
        since_boot = (int) event.values[0];
        updatePie();*/
        since_boot = (int) event.values[0];

        int saved = db.getDaysWithoutToday();

        updateUI(since_boot, saved);
    }

    public void updateUI(int step, int saved){

        int pauseDifference = step -
                getSharedPreferences("pedometer", Context.MODE_PRIVATE)
                        .getInt("pauseCount", step);
        text.setText(Integer.toString(step) + " / " +Integer.toString(saved));
    }
}
