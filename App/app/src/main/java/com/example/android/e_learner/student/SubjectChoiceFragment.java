package com.example.android.e_learner.student;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.android.e_learner.R;

import java.util.ArrayList;
import java.util.List;

public class SubjectChoiceFragment extends Fragment {

    int color;
    Spinner spinner;
    static Activity activity;
    static String statusBarColor;
    


    static SubjectChoiceFragment init(int color, String statusColor, Activity ac) {
        activity = ac;
        statusBarColor = statusColor;
        SubjectChoiceFragment subjectChoiceFragment = new SubjectChoiceFragment();
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
        View layoutView = inflater.inflate(R.layout.s_fragment_subject_choice, container,
                false);
        View tv = layoutView.findViewById(R.id.text);
        layoutView.setBackgroundColor(color);

        return layoutView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        spinner = view.findViewById(R.id.subject_choice_spinner);
        List<String> list = new ArrayList<String>();
        list.add("Physics");
        list.add("Chemistry");
        list.add("Mathematics");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, R.layout.s_spinner_subject_choice,list);
        spinner.setAdapter(adapter);


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
