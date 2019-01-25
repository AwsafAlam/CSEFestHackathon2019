package io.github.utshaw.myhealth;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.github.utshaw.myhealth.model.SingletonVolley;
import io.github.utshaw.myhealth.remote.ApiUtils;

public class StepRecordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        ArrayList<Record> records = new ArrayList<Record>();
        //records.add(new Record("Donut", "1.6"));

        int next = getSharedPreferences("pedometer", Context.MODE_PRIVATE)
                .getInt("steppoints",0);

        int day_total = 0;
        for(int ix = next - 1; ix >0; ix --) {
            int rate = getSharedPreferences("pedometer", Context.MODE_PRIVATE)
                    .getInt("steprate" + Integer.toString(ix), 0);
            long timePrevious = getSharedPreferences("pedometer", Context.MODE_PRIVATE)
                    .getLong("steptime"+Integer.toString(ix),0);
            Date newDate = new Date(timePrevious);
            SimpleDateFormat spf= new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String date = spf.format(newDate);

            records.add(new Record(date, Integer.toString(rate)));
        }





        RecordAdapter adapter = new RecordAdapter(this, records);
        ListView listView = (ListView) findViewById(R.id.listview_flavor);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void getData() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUtils.BASE_URL, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("ResponseFinal=", response);
                String dataInfo = "";
                if (response != null) {

                } else {
                    Log.e("ServiceHandler", "Couldn't get any data from the url");
                }



            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Server Error, Please try again later", Toast.LENGTH_LONG).show();
            }
        })  {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();


                return params;
            }
        };

        SingletonVolley.getInstance(StepRecordActivity.this).addToRequestQueue(stringRequest);
    }


}
