package io.github.utshaw.myhealth;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class BasicProfileActivity extends AppCompatActivity {

    EditText eTxtName, eTxtAge, eTxtWeight, eTxtHeight;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_profile);

        btn = findViewById(R.id.btn);
        eTxtName = findViewById(R.id.name);
        eTxtAge = findViewById(R.id.age);
        eTxtWeight = findViewById(R.id.weight);
        eTxtHeight = findViewById(R.id.height);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BasicProfileActivity.this, MainActivity.class));
            }
        });
    }
}
