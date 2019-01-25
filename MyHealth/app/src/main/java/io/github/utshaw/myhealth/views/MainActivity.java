package io.github.utshaw.myhealth.views;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.utshaw.myhealth.R;
import io.github.utshaw.myhealth.SensorListener;
import io.github.utshaw.myhealth.model.SingletonVolley;
import io.github.utshaw.myhealth.remote.ApiUtils;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private static final int RC_SIGN_IN_REQUEST = 1;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

//    private String mobile, token;


    CardView cardView1, cardView2, cardView3, cardView4, cardView5;

    String mUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // mAPIService = ApiUtils.getAPIService();

        FirebaseApp.initializeApp(this);

        cardView1 = findViewById(R.id.cv_first);
        cardView2 = findViewById(R.id.cv_second);
        cardView3 = findViewById(R.id.cv_third);
        cardView4 = findViewById(R.id.cv_forth);
        cardView5 = findViewById(R.id.cv_bottom);



        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(MainActivity.this, StepCount.class));
                startActivity(new Intent(MainActivity.this, StepsActivity.class));
            }
        });

        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, HeartRateActivity.class));
            }
        });

        cardView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

        cardView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        cardView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mFirebaseAuth = FirebaseAuth.getInstance();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    //user signed in

                    onSignedInInitialize(user.getDisplayName());


                }else{
                    // user is signed out

                    onSignedOutCleanup();
                    List<AuthUI.IdpConfig> providers = Arrays.asList(
                            new AuthUI.IdpConfig.PhoneBuilder().build());

// Create and launch sign-in intent
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setAvailableProviders(providers)
                                    .build(),
                            RC_SIGN_IN_REQUEST);
                }
            }
        };


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        startService(new Intent(this, SensorListener.class));


    }


    private void onSignedInInitialize(String username) {
//        mUserName = username;
//        mobile = mFirebaseAuth.getCurrentUser().getPhoneNumber();
//        //token = mFirebaseAuth.getAccessToken(true);
//        token = FirebaseInstanceId.getInstance().getToken();
//        Log.e("Service", mobile);
//        //if(!TextUtils.isEmpty(mobile) && !TextUtils.isEmpty(token)) {
//            //sendPost(mobile, token);
//        //}
//        uploadData();

    }

    private void onSignedOutCleanup() { }


    public void showResponse(String response) {

        Toast.makeText(this, response, Toast.LENGTH_SHORT).show();

    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.st_class) {
//            startActivity(new Intent(MainActivity.this, CallingActivity.class));
        }else if(id == R.id.st_payment){

        }else if(id == R.id.st_profile){

        }else if(id == R.id.st_signout){
            signOutFromFirebase();

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


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


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_signout) {
            signOutFromFirebase();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void signOutFromFirebase(){
        AuthUI.getInstance().signOut(this);
    }


    @Override
    protected void onPause() {
        super.onPause();
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    private void uploadData() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUtils.BASE_URL, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //Toast.makeText(getApplicationContext(),"Response="+response,Toast.LENGTH_SHORT).show();
                Log.e("ResponseFinal=", response);
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
                } else {
                    Log.e("ServiceHandler", "Couldn't get any data from the url");
                }

                /*if (sDataError.equals("YES")) {
                    Toast.makeText(getApplicationContext(), "Problem Occurred. Please try again Later", Toast.LENGTH_SHORT).show();
                } else {

                    Log.e("Hello Masud,","You are successful");
                    JSONObject updatedJson = new JSONObject();

                    try {
                        if(dataInfo.length()>15)
                            oneObject.put("profilePic","http://joybanglabd.org/uploads/"+dataInfo);
                        else
                            oneObject.put("profilePic",sProfilepic);
                        JSONArray oneArray = new JSONArray();
                        oneArray.put(oneObject);

                        updatedJson.put("clientDetalstInfo", (Object) oneArray);
                    }catch(JSONException e){

                    }
                    Log.e("updatedJson",updatedJson.toString());

                    if(updatedJson.toString().length()>0) {
                        sharedpreferences = getSharedPreferences(PublicVariableLink.sharedStorage,
                                Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("ezComUsersInfo", updatedJson.toString());
                        editor.commit();
                        Intent intent = new Intent(EmployeeDetailEdit.this, EmployeeDetail.class);
                        startActivity(intent);
                    }

                }*/

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Server Error, Please try again later", Toast.LENGTH_LONG).show();
                //Log.e("ResponseError=", error + "");
                //pbar.setVisibility(View.INVISIBLE);
            }
        })  {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                //oneObject = new JSONObject();



                //params.put(TAG_JOIN_DATE, sJoinDate);
                //params.put(TAG_JOIN_DATE_IN_CURRENT_POSITION, sJoinDateInCurPosition);
//                params.put("mobile", mobile);
//                params.put("token", token);
//                Log.e("Service",mobile + "next");


                return params;
            }
        };

        SingletonVolley.getInstance(MainActivity.this).addToRequestQueue(stringRequest);
    }

}
