package io.github.utshaw.myhealth;

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
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

import io.github.utshaw.myhealth.model.Login;
import io.github.utshaw.myhealth.remote.APIService;
import io.github.utshaw.myhealth.remote.ApiUtils;
import io.github.utshaw.myhealth.remote.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private static final int RC_SIGN_IN_REQUEST = 1;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private APIService mAPIService;
    private String mobile, token;


    CardView cardView1, cardView2, cardView3, cardView4, cardView5;

    String mUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        mAPIService = ApiUtils.getAPIService();

        FirebaseApp.initializeApp(this);

        cardView1 = findViewById(R.id.cv_first);
        cardView2 = findViewById(R.id.cv_second);
        cardView3 = findViewById(R.id.cv_third);
        cardView4 = findViewById(R.id.cv_forth);
        cardView5 = findViewById(R.id.cv_bottom);



        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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


    }


    private void onSignedInInitialize(String username) {
        mUserName = username;
//        mobile = mFirebaseAuth.getCurrentUser().getPhoneNumber();
        mobile = "012381931";
        token = "someTokenUt";
        if(!TextUtils.isEmpty(mobile) && !TextUtils.isEmpty(token)) {
            sendPost(mobile, token);
        }
    }

    private void onSignedOutCleanup() {

    }


    public void sendPost(String mobile, String token) {
//        mAPIService.saveLogin(mobile, token).enqueue(new Callback<Login>() {
//            @Override
//            public void onResponse(Call<Login> call, Response<Login> response) {
//
//                if(response.isSuccessful()) {
//                    showResponse(response.body().toString());
//                    Log.i("Utshaw", "post submitted to API." + response.body().toString());
//                }else{
//                    if(response != null){
//                        Log.i("Utshaw", "post unsuccessful to API. response NULL" );
//                    }else {
//                        Log.i("Utshaw", "post unsuccessful to API. response NOT NULL" );
//                    }
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Login> call, Throwable t) {
//                Log.e("Utshaw", "Unable to submit post to API.");
//            }
//        });

        APIService apiService = RetrofitClient.getClient().create(APIService.class);

        Call<Login> call = apiService.saveLogin(mobile,token);
        call.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login>call, Response<Login> response) {
                String movies = response.body().getStatus();
                Log.d("DEBUG", "Number of movies received: " + movies);
            }

            @Override
            public void onFailure(Call<Login>call, Throwable t) {
                // Log error here since request failed
                Log.e("DEBUG", t.toString());
            }
        });
    }

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

}
