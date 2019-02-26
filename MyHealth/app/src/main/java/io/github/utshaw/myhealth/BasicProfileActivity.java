package io.github.utshaw.myhealth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.utshaw.myhealth.model.SingletonVolley;
import io.github.utshaw.myhealth.views.MainActivity;

public class BasicProfileActivity extends AppCompatActivity {

    EditText eTxtName, eTxtAge, eTxtWeight, eTxtHeight;
    Button btn;
    private static final int RC_SIGN_IN_REQUEST = 1;

    private String mobile, token;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;


    String mUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_profile);

        btn = findViewById(R.id.login);
        eTxtName = findViewById(R.id.name);
        eTxtAge = findViewById(R.id.age);
        eTxtWeight = findViewById(R.id.weight);
        eTxtHeight = findViewById(R.id.height);

        final String name, age, weight, height;
        name = eTxtName.getText().toString();
        age = eTxtAge.getText().toString();
        weight = eTxtWeight.getText().toString();
        height = eTxtHeight.getText().toString();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(age) && !TextUtils.isEmpty(weight) && !TextUtils.isEmpty(height)){
                    uploadData();

                    prefs = getSharedPreferences("body", MODE_PRIVATE);
                    editor = prefs.edit();
                    editor.putString("NAME", name).commit();
                    editor.putString("AGE", age).commit();
                    editor.putString("WEIGHT", weight).commit();
                    editor.putString("HEIGHT", height).commit();


                }
                startActivity(new Intent(BasicProfileActivity.this, MainActivity.class));
                Toast.makeText(BasicProfileActivity.this, "Data uploaded", Toast.LENGTH_SHORT).show();
            }
        });




        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }


    private void onSignedInInitialize(String username) {
        mUserName = username;
        token = "";
        Log.e("Service", mobile);
        //if(!TextUtils.isEmpty(mobile) && !TextUtils.isEmpty(token)) {
        //sendPost(mobile, token);
        //}
        uploadData();

    }

    private void onSignedOutCleanup() { }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN_REQUEST){
            if(resultCode == RESULT_OK){
                Toast.makeText(this, "Signed In", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Sign in canceled", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void signOutFromFirebase(){
        AuthUI.getInstance().signOut(this);
    }




    private void uploadData() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://api.kolpobd.com/v1/index.php/updateuser", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("ApiCall=", response);
                String dataInfo = "";
                //pbar.setVisibility(View.INVISIBLE);
                if (response != null) {
                    /*try {
                        JSONObject jsonObj = new JSONObject(response);
                        jSongArray = jsonObj.getJSONArray(TAG_EMPLOYEE);
                        JSONObject oneObject = jSongArray.getJSONObject(0);
                        sDataError = oneObject.getString(TAG_DATA_ERROR)
                                .trim();
                        dataInfo = oneObject.getString("dataInfo")
                                .trim();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }*/
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
                params.put("token", "someTokenUt");
                params.put("username", eTxtName.getText().toString());
                params.put("age", eTxtAge.getText().toString());
                params.put("height", eTxtHeight.getText().toString());
                params.put("weight", eTxtWeight.getText().toString());
                Log.e("ApiCall",eTxtName.getText().toString() + "next");


                return params;
            }
        };

        SingletonVolley.getInstance(BasicProfileActivity.this).addToRequestQueue(stringRequest);
    }


}
