package io.github.utshaw.myhealth;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.github.utshaw.myhealth.model.SingletonVolley;
import io.github.utshaw.myhealth.remote.ApiUtils;
import io.github.utshaw.myhealth.views.MainActivity;

public class RecordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        ArrayList<Record> records = new ArrayList<Record>();
        records.add(new Record("Donut", "1.6"));



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

        SingletonVolley.getInstance(RecordActivity.this).addToRequestQueue(stringRequest);
    }


}
