package io.github.utshaw.myhealth.views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.github.utshaw.myhealth.BasicProfileActivity;
import io.github.utshaw.myhealth.R;
import io.github.utshaw.myhealth.model.SingletonVolley;
import io.github.utshaw.myhealth.util.TokenManager;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static android.content.Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED;

public class LoginActivity extends AppCompatActivity {

    EditText emailTxt, passTxt;
    Button login;
    TokenManager tokenManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailTxt = findViewById(R.id.email);
        passTxt =findViewById(R.id.pass);
        login = findViewById(R.id.login);
        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));
        if(tokenManager.getToken() != null){
            //TODO: Check token with server
            startActivity(new Intent(LoginActivity.this , MainActivity.class));
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(emailTxt.getText().toString()) && !TextUtils.isEmpty(passTxt.getText().toString()))
                    uploadData();
                else
                    Toast.makeText(LoginActivity.this, "Empty field", Toast.LENGTH_SHORT).show();
                
            }
        });
    }

    private void uploadData() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://api.kolpobd.com/v1/index.php/userloginEmail", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("Utshaw", response);
                String dataInfo = "";
                //pbar.setVisibility(View.INVISIBLE);
                //TODO: save token here
                if (response != null) {
                    try {
                        JSONObject jsonObj = new JSONObject(response);

                        String token = jsonObj.getString("token");
                        String status = jsonObj.getString("status");
                        if(status.equals("error")){
                            Toast.makeText(LoginActivity.this, "Wrong username or password", Toast.LENGTH_SHORT).show();

                        }else if(status.equals("login")){

                            Toast.makeText(LoginActivity.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
                            tokenManager.saveToken(token);
                            startActivity(new Intent(LoginActivity.this,MainActivity.class).addFlags(FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_RESET_TASK_IF_NEEDED));
                        }else{ // register

                            Toast.makeText(LoginActivity.this, "You are new user. Complete your profile", Toast.LENGTH_SHORT).show();
                            tokenManager.saveToken(token);
                            startActivity(new Intent(LoginActivity.this,BasicProfileActivity.class).addFlags(FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_RESET_TASK_IF_NEEDED));
                        }
                        Log.d("ApiCalltok ", "Token : "+token);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Log.e("ApiCall", "Couldn't get any data from the url");
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Server Error, Please try again later", Toast.LENGTH_LONG).show();
                Log.e("ApiCall=", error + "");
                //pbar.setVisibility(View.INVISIBLE);
            }
        })  {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                //oneObject = new JSONObject();

                //params.put(TAG_JOIN_DATE, sJoinDate);
                //params.put(TAG_JOIN_DATE_IN_CURRENT_POSITION, sJoinDateInCurPosition);
                //params.put("token", "newuser");
                params.put("email", emailTxt.getText().toString());
                params.put("password", passTxt.getText().toString());
                params.put("api",  getResources().getString(R.string.API_SECURITY_KEY));


                Log.e("ApiCall",emailTxt.getText().toString() + " -next");


                return params;
            }
        };

        SingletonVolley.getInstance(LoginActivity.this).addToRequestQueue(stringRequest);
    }

    public void click(View view) {
        startActivity(new Intent(LoginActivity.this, BasicProfileActivity.class));
    }
}
