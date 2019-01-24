package com.example.android.e_learner.student;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.android.e_learner.MyConstants;
import com.example.android.e_learner.R;
import com.example.android.e_learner.student.classrecord.ClassRecord;
import com.example.android.e_learner.student.classrecord.ClassRecordAdapter;

import java.util.ArrayList;

public class ClassHistoryActivity extends AppCompatActivity {


    ClassRecordAdapter adapter;
    private RecyclerView recyclerView;
    private ArrayList<ClassRecord> listItems ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_history);
        Toolbar toolbar = findViewById(R.id.toolbarid);
        setSupportActionBar(toolbar);

//this line shows back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listItems = new ArrayList<>();

        initRecyclerView();
        loadRecyclerViewData();



    }

    private void loadRecyclerViewData() {
        listItems.add(new ClassRecord(MyConstants.CHEMISTRY, "Farhan Utshaw", "25", 4.5f));
        listItems.add(new ClassRecord(MyConstants.PHYSICS, "Abdur Rouf", "30", 5f));
        listItems.add(new ClassRecord(MyConstants.MATHEMATICS, "Abdur Rouf", "45", 4.0f));
        adapter = new ClassRecordAdapter(listItems, getApplicationContext());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void initRecyclerView(){

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


}
