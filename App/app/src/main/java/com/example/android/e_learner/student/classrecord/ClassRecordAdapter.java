package com.example.android.e_learner.student.classrecord;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.android.e_learner.MyConstants;
import com.example.android.e_learner.R;


import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ClassRecordAdapter  extends RecyclerView.Adapter<ClassRecordAdapter.ViewHolder>{

    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<ClassRecord> listItems = new ArrayList<>();

    private Context mContext;


    public ClassRecordAdapter(ArrayList<ClassRecord> listItems, Context mContext) {
        this.listItems = listItems;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.s_class_record_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        Log.d(TAG, "onBindViewHolder: called");

//        Glide.with(mContext)
//                .asBitmap()
//                .load(listItems.get(position).getImageUrl())
//                .into(viewHolder.image);


        String className = listItems.get(position).getClassName();
        if(className.equals(MyConstants.CHEMISTRY)){
            viewHolder.imageView.setImageResource(R.drawable.ic_chemistry_mid);
        }else if(className.equals(MyConstants.PHYSICS)){
            viewHolder.imageView.setImageResource(R.drawable.ic_magnet_mid);
        }else {
            viewHolder.imageView.setImageResource(R.drawable.ic_math_mid);
        }

        viewHolder.classTeacher.setText(listItems.get(position).getTeacherName());
        viewHolder.classDuration.setText(listItems.get(position).getDuration() + " min");
        viewHolder.classRating.setRating(listItems.get(position).getClassRating());


        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView classTeacher, classDuration;
        RatingBar classRating;
        LinearLayout parentLayout;
        CircleImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            classTeacher = itemView.findViewById(R.id.class_teacher);
            classDuration  = itemView.findViewById(R.id.class_duration);
            classRating = itemView.findViewById(R.id.class_rating);
            parentLayout = itemView.findViewById(R.id.parent_layout);
            imageView = itemView.findViewById(R.id.class_image);

        }





    }



}