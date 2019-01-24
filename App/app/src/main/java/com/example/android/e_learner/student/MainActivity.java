package com.example.android.e_learner.student;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;


import com.example.android.e_learner.MyConstants;
import com.example.android.e_learner.R;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.muddzdev.styleabletoast.StyleableToast;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, RatingDialogListener {

    private static final int RC_SIGN_IN_REQUEST = 1, RC_PERMISSION = 101;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    FirebaseAuth auth;
    DatabaseReference databaseStudentRef;

    String mUserName;
    private Button btnCall;
    CardView cvMyProfile, cvChem, cvPhys, cvMath, cvReview;
    String subjectChosenForCall;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_activity_main);
        auth = FirebaseAuth.getInstance();



        cvMyProfile = findViewById(R.id.cv_profile);
        cvChem = findViewById(R.id.cv_chem);
        cvPhys = findViewById(R.id.cv_phys);
        cvMath = findViewById(R.id.cv_math);
        cvReview = findViewById(R.id.cv_review);


        cvMyProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, MyProfileActivity.class));
            }
        });


        cvChem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subjectChosenForCall = MyConstants.CHEMISTRY;
                permissionCheckAndDial();
            }
        });


        cvPhys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subjectChosenForCall = MyConstants.PHYSICS;
                permissionCheckAndDial();
            }
        });


        cvMath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subjectChosenForCall = MyConstants.MATHEMATICS;
                permissionCheckAndDial();
            }
        });

        cvReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        btnCall = findViewById(R.id.btn_call);

        databaseStudentRef = FirebaseDatabase.getInstance().getReference("students");




        checkStudent();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //Firebase-Auth
        mFirebaseAuth = FirebaseAuth.getInstance();




//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            Intent intent = new Intent(MainActivity.this, DialActivity.class);
//            startActivity(intent);
//
//            }
//        });



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);





        /* Remove Viewpager
        TabLayout tabLayout  = (TabLayout) findViewById(R.id.sliding_tabs);
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                switch (position)
                {
                    case 0:
                        return new MondayFragment();
                    case 1:
                        return new TuesdayFragment();
                    default:
                        return new WednesdayFragment();
                }
            }

            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position)
                {
                    case 0:
                        return "Monday";
                    case 1:
                        return "Tuesday";
                    default:
                        return "Wednesday";
                }
            }
        };
        viewPager.setAdapter(fragmentPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
remove viewpager*/



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

    private void onSignedOutCleanup() {

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
                Intent intent = new Intent(MainActivity.this, DialActivity.class);
                intent.putExtra(MyConstants.DIAL_BUNDLE_KEY, subjectChosenForCall);
                startActivity(intent);
            }
        }
    }

    private boolean checkIfAlreadyhavePermission(String[] permissions) {

        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {
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

                    Intent intent = new Intent(MainActivity.this, DialActivity.class);
                    intent.putExtra(MyConstants.DIAL_BUNDLE_KEY, subjectChosenForCall);//CC
                    startActivity(intent);

                } else {
                    StyleableToast.makeText(MainActivity.this, "You must grant permission to call!!", Toast.LENGTH_LONG, R.style.mytoast).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void onSignedInInitialize(String username) {
        mUserName = username;
    }

    private void checkStudent() {
        databaseStudentRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {




                if(dataSnapshot.hasChild(auth.getCurrentUser().getPhoneNumber().toString())){
                    Toast.makeText(MainActivity.this, "Found", Toast.LENGTH_SHORT).show();
                }else{
                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create(); //Read Update
                    View mView = getLayoutInflater().inflate(R.layout.s_account_creation_dialog, null);
                    Button makeCallButton = (Button) mView.findViewById(R.id.btn_call);
                    makeCallButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(MainActivity.this, "Button clicked", Toast.LENGTH_SHORT).show();
                        }
                    });
                    
                    alertDialog.setView(mView);
                    alertDialog.show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


                Toast.makeText(MainActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void showDialog() {
        new AppRatingDialog.Builder()
                .setPositiveButtonText("Submit")
                .setNegativeButtonText("Cancel")
                .setNeutralButtonText("Later")
                .setNoteDescriptions(Arrays.asList("Very Bad", "Not good", "Quite ok", "Very Good", "Excellent !!!"))
                .setDefaultRating(4)
                .setTitle("Give review for this class")
                .setDescription("Please select some stars and give your feedback")
                .setCommentInputEnabled(true)
                .setDefaultComment("A great class indeed!")
                .setStarColor(R.color.colorPrimary)
                .setNoteDescriptionTextColor(R.color.colorAccent)
                .setTitleTextColor(R.color.colorPrimary)
                .setDescriptionTextColor(R.color.colorAccent)
                .setHint("Please write your comment here ...")
                .setHintTextColor(R.color.colorPrimary)
                .setCommentTextColor(R.color.colorPrimaryDark)
                .setCommentBackgroundColor(R.color.font_color)
                .setWindowAnimation(R.style.MyDialogFadeAnimation)
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .create(MainActivity.this)
                .show();
    }





    private void checkStudentAndCall() {
        databaseStudentRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {




                if(dataSnapshot.hasChild(auth.getCurrentUser().getPhoneNumber().toString())){
                    Intent intent = new Intent(MainActivity.this, DialActivity.class);
                    startActivity(intent);


                }else{
                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create(); //Read Update
                    View mView = getLayoutInflater().inflate(R.layout.s_account_creation_dialog, null);
                    Button makeCallButton = (Button) mView.findViewById(R.id.btn_call);
                    makeCallButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(MainActivity.this, "Button clicked", Toast.LENGTH_SHORT).show();
                        }
                    });

                    alertDialog.setView(mView);
                    alertDialog.show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


                Toast.makeText(MainActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

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
        getMenuInflater().inflate(R.menu.s_main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.st_class) {
//            startActivity(new Intent(MainActivity.this, CallingActivity.class));
            startActivity(new Intent(MainActivity.this, ClassHistoryActivity.class));
        }else if(id == R.id.st_payment){
            startActivity(new Intent(MainActivity.this, PaymentActivity.class));
        }else if(id == R.id.st_profile){
            startActivity(new Intent(MainActivity.this, MyProfileActivity.class));
        }else if(id == R.id.st_signout){
            signOutFromFirebase();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onPositiveButtonClicked(int i, String s) {

    }

    @Override
    public void onNegativeButtonClicked() {

    }

    @Override
    public void onNeutralButtonClicked() {

    }
}
