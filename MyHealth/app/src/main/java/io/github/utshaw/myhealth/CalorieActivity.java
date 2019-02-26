package io.github.utshaw.myhealth;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class CalorieActivity extends AppCompatActivity {

    TextView tView;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calorie);
        tView = findViewById(R.id.cal);
        tView.setText("");

        prefs = getSharedPreferences("body", MODE_PRIVATE);
        editor = prefs.edit();
        String name =  prefs.getString("NAME", "");
        String age = prefs.getString("AGE", "24");
        String weight = prefs.getString("WEIGHT", "75");
        String height = prefs.getString("HEIGHT", "172");


        double bMR = (9.99 * Double.parseDouble(weight)) + (6.25 * Double.parseDouble(height)) - (4.92 * Double.parseDouble(age));
        tView.setText(bMR+"");


    }
}
