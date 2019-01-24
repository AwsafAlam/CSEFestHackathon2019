package com.example.android.e_learner.student;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.android.e_learner.R;

public class CallFragment extends Fragment {

    int color;
    Spinner spinner;
    static Activity activity;
    static String statusBarColor;
    


    static CallFragment init(int color, String statusColor, Activity ac) {
        activity = ac;
        statusBarColor = statusColor;
        CallFragment subjectChoiceFragment = new CallFragment();
        // Supply val input as an argument.
        Bundle args = new Bundle();
        args.putInt("color", color);
        subjectChoiceFragment.setArguments(args);
        return subjectChoiceFragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        color = getArguments() != null ? getArguments().getInt("color") : 1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layoutView = inflater.inflate(R.layout.s_fragment_call, container,
                false);
        View tv = layoutView.findViewById(R.id.text);
        layoutView.setBackgroundColor(color);

        FloatingActionButton callFab = layoutView.findViewById(R.id.call_fab);
        callFab.setScaleType(ImageView.ScaleType.CENTER);

        callFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, FireVideo.class);
                startActivity(intent);
            }
        });

        return layoutView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
    }
    


    @Override
    public void setUserVisibleHint(boolean visible)
    {
        super.setUserVisibleHint(visible);
        if (visible && isResumed())
        {
            ((CallingActivity)getActivity()).updateStatusBarColor(statusBarColor);
            getActivity().findViewById(R.id.first).setVisibility(View.VISIBLE);
            onResume();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!getUserVisibleHint()) {
            return;
        }
    }

        //INSERT


}
