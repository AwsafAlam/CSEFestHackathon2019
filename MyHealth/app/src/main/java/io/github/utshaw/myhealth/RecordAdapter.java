/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.utshaw.myhealth;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import java.util.ArrayList;


public class RecordAdapter extends ArrayAdapter<Record> {

    private static final String LOG_TAG = RecordAdapter.class.getSimpleName();


    private Activity context;


    public RecordAdapter(Activity context, ArrayList<Record> androidFlavors) {

        super(context, 0, androidFlavors);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item_post, parent, false);
        }

        Record currentAndroidFlavor = getItem(position);

        TextView nameTextView = (TextView) listItemView.findViewById(R.id.rate);
        nameTextView.setText(currentAndroidFlavor.getRate());

        TextView numberTextView = (TextView) listItemView.findViewById(R.id.time);
        numberTextView.setText(currentAndroidFlavor.getTime());


        return listItemView;
    }

}
