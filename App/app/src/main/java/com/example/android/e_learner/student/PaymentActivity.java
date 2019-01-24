package com.example.android.e_learner.student;

import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.android.e_learner.R;
import com.muddzdev.styleabletoast.StyleableToast;

public class PaymentActivity extends AppCompatActivity {

    Button btnPromoSubmit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnPromoSubmit = findViewById(R.id.btn_req_submit);

        btnPromoSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(PaymentActivity.this).create(); //Read Update
                View mView = getLayoutInflater().inflate(R.layout.s_promo_code_dialog, null);


                Button btnApplyPromo = (Button) mView.findViewById(R.id.btn_apply);


                btnApplyPromo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        StyleableToast.makeText(PaymentActivity.this, "Promo Applied!", Toast.LENGTH_LONG, R.style.mytoast).show();


                    }
                });

                alertDialog.setView(mView);
                alertDialog.show();
            }
        });
    }
}
