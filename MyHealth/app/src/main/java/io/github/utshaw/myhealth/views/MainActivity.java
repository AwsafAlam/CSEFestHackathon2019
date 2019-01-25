package io.github.utshaw.myhealth.views;

import android.content.Context;
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

import android.widget.TextView;

import android.widget.Button;

import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import com.cuieney.progress.library.RainbowProgressBar;





import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.utshaw.myhealth.CalorieActivity;
import io.github.utshaw.myhealth.DatabaseHR;
import io.github.utshaw.myhealth.R;
import io.github.utshaw.myhealth.RecordActivity;
import io.github.utshaw.myhealth.SensorListener;
import io.github.utshaw.myhealth.StepRecordActivity;
import io.github.utshaw.myhealth.model.SingletonVolley;
import io.github.utshaw.myhealth.remote.ApiUtils;
import io.github.utshaw.myhealth.util.TokenManager;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private static final int RC_SIGN_IN_REQUEST = 1;

    TextView step, km;

    //private String mobile, token;



//    private String mobile, token;
    TokenManager tokenManager;


    CardView cardView1, cardView2, cardView3, cardView4, cardView5;

    String mUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));


       // mAPIService = ApiUtils.getAPIService();


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

                startActivity(new Intent(MainActivity.this, CalorieActivity.class));

            }
        });

        cardView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    startActivity(new Intent(MainActivity.this, CalorieActivity.class));
            }
        });

        cardView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, StepsActivity.class));

            }
                                     });



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
        if(HeartRateActivity.isNetworkAvailable(this))
            sendData();

        int next = getSharedPreferences("pedometer", Context.MODE_PRIVATE)
                .getInt("steppoints",0);
        long time = System.currentTimeMillis();
        int day_total = 0;
        for(int ix = next - 1; ix >0; ix --) {
            int rate = getSharedPreferences("pedometer", Context.MODE_PRIVATE)
                    .getInt("steprate" + Integer.toString(ix), 0);


            long timePrevious = getSharedPreferences("pedometer", Context.MODE_PRIVATE)
                    .getLong("steptime"+Integer.toString(ix),0);
            if(timePrevious + 24*3600*1000 > time){
                day_total+= rate;
            }else
                break;
        }
        int distance_total = (int)(day_total * 0.75);
        step = findViewById(R.id.text);
        step.setText(Integer.toString(day_total));
        km = findViewById(R.id.km);
        if(distance_total < 1000){
            km.setText(Integer.toString(distance_total)+ " meters");
        }
        else{
            Float v = (float)distance_total/1000;
            km.setText(Float.toString(v)+ " Km");
        }

        RainbowProgressBar bar = findViewById(R.id.progress1);
        bar.setProgress(day_total);
    }

    private void sendData(){

        StringBuilder sbTime = new StringBuilder();
        StringBuilder sbRate = new StringBuilder();
        String prefix = "";

        int startFrom = getSharedPreferences("pedometer", Context.MODE_PRIVATE)
                .getInt("stepstart",0);
        int next = getSharedPreferences("pedometer", Context.MODE_PRIVATE)
                .getInt("steppoints",0);

        for(int ix = startFrom; ix < next; ix ++){
            int rate = getSharedPreferences("pedometer", Context.MODE_PRIVATE)
                    .getInt("steprate"+Integer.toString(ix),0);
            long time = getSharedPreferences("pedometer", Context.MODE_PRIVATE)
                    .getLong("steptime"+Integer.toString(ix),0);

            Date newDate = new Date(time);
            SimpleDateFormat spf= new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String date = spf.format(newDate);

            sbTime.append(prefix);
            sbRate.append(prefix);
            prefix = ";";
            sbTime.append(date);
            //sbTime.append((new Date(time)).toString());
            sbRate.append(Integer.toString(rate));
        }

        getSharedPreferences("pedometer", Context.MODE_PRIVATE).edit()
                .putInt("stepstart",next).apply();

        Log.e("HRsend",sbTime.toString() + "   "+sbRate.toString());
        Log.e("HRsend",sbRate.toString());
        uploadData(sbTime.toString(), sbRate.toString());

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
            startActivity(new Intent(MainActivity.this, RecordActivity.class));
        }else if(id == R.id.st_payment){
            startActivity(new Intent(MainActivity.this, StepRecordActivity.class));

        }else if(id == R.id.st_profile){
            startActivity(new Intent(MainActivity.this, StepRecordActivity.class));

        }else if(id == R.id.st_signout){

            tokenManager.deleteToken();
            finish();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            return true;
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
            tokenManager.deleteToken();
            finish();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




    private void uploadData(final String timestamp, final String rate) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUtils.BASE_URL_STEP, new com.android.volley.Response.Listener<String>() {
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
                //Toast.makeText(static_context, "Server Error, Please try again later", Toast.LENGTH_LONG).show();
                //Log.e("ResponseError=", error + "");
                //pbar.setVisibility(View.INVISIBLE);
            }
        })  {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                //oneObject = new JSONObject();
                params.put("time",timestamp);
                params.put("stepRate",rate);
                params.put("stepRate",rate);




                //params.put(TAG_JOIN_DATE, sJoinDate);
                //params.put(TAG_JOIN_DATE_IN_CURRENT_POSITION, sJoinDateInCurPosition);
//                params.put("mobile", mobile);
//                params.put("token", token);
//                Log.e("Service",mobile + "next");


                return params;
            }
        };

        SingletonVolley.getInstance(this).addToRequestQueue(stringRequest);
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
