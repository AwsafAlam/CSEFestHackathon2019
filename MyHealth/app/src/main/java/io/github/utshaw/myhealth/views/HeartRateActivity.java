package io.github.utshaw.myhealth.views;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.Pair;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cuieney.progress.library.RainbowProgressBar;

import io.github.utshaw.myhealth.DatabaseHR;
import io.github.utshaw.myhealth.ImageProcessing;
import io.github.utshaw.myhealth.model.SingletonVolley;
import io.github.utshaw.myhealth.remote.ApiUtils;


/**
 * This class extends Activity to handle a picture preview, process the preview
 * for a red values and determine a heart beat.
 *
 * @author Justin Wetherell <phishman3579@gmail.com>
 */
public class HeartRateActivity extends Activity {

    private static final String TAG = "HeartRateMonitor";
    private static final AtomicBoolean processing = new AtomicBoolean(false);

    private static SurfaceView preview = null;
    private static SurfaceHolder previewHolder = null;
    private static Camera camera = null;
    private static View image = null;
    private static TextView text = null;

    private static PowerManager.WakeLock wakeLock = null;

    private static int averageIndex = 0;
    private static final int averageArraySize = 4;
    private static final int[] averageArray = new int[averageArraySize];
    private static HeartRateActivity static_context;
    private static RainbowProgressBar pBar;


    public static enum TYPE {
        GREEN, RED
    };

    private static TYPE currentType = TYPE.GREEN;

    public static TYPE getCurrent() {
        return currentType;
    }

    private static int beatsIndex = 0;
    private static final int beatsArraySize = 3;
    private static final int[] beatsArray = new int[beatsArraySize];
    private static double beats = 0;
    private static long startTime = 0;
    private static ImageView heartIcon;

    boolean permit = true;
    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(io.github.utshaw.myhealth.R.layout.measure_bp);
        static_context = this;
        pBar = findViewById(io.github.utshaw.myhealth.R.id.progress1);

        preview = (SurfaceView) findViewById(io.github.utshaw.myhealth.R.id.preview);


        image = findViewById(io.github.utshaw.myhealth.R.id.image);
        text = (TextView) findViewById(io.github.utshaw.myhealth.R.id.text);
        heartIcon = findViewById(io.github.utshaw.myhealth.R.id.heart);

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "DoNotDimScreen");

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // only for gingerbread and newer versions
            if (checkSelfPermission(android.Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.CAMERA},
                        100);
                permit = false;
            }
        }

        if(isNetworkAvailable(static_context))
            sendData();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onResume() {
        super.onResume();
        if(permit) {

            previewHolder = preview.getHolder();
            previewHolder.addCallback(surfaceCallback);
            previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

            wakeLock.acquire();

            camera = Camera.open();

            startTime = System.currentTimeMillis();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onPause() {
        super.onPause();

        try {
            wakeLock.release();

            camera.setPreviewCallback(null);
            camera.stopPreview();
            camera.release();
            camera = null;
        }
        catch (Exception e){}
    }

    private static Camera.PreviewCallback previewCallback = new Camera.PreviewCallback() {

        /**
         * {@inheritDoc}
         */
        @Override
        public void onPreviewFrame(byte[] data, Camera cam) {
            if (data == null) throw new NullPointerException();
            Camera.Size size = cam.getParameters().getPreviewSize();
            if (size == null) throw new NullPointerException();

            if (!processing.compareAndSet(false, true)) return;

            int width = size.width;
            int height = size.height;

            int imgAvg = ImageProcessing.decodeYUV420SPtoRedAvg(data.clone(), height, width);
            // Log.i(TAG, "imgAvg="+imgAvg);
            if (imgAvg == 0 || imgAvg == 255) {
                processing.set(false);
                return;
            }

            int averageArrayAvg = 0;
            int averageArrayCnt = 0;
            for (int i = 0; i < averageArray.length; i++) {
                if (averageArray[i] > 0) {
                    averageArrayAvg += averageArray[i];
                    averageArrayCnt++;
                }
            }

            int rollingAverage = (averageArrayCnt > 0) ? (averageArrayAvg / averageArrayCnt) : 0;
            TYPE newType = currentType;
            if (imgAvg < rollingAverage) {
                newType = TYPE.RED;
                /*static_context.runOnUiThread(new Runnable() {
                    public void run() {
                        Log.d("UI thread", "I am the UI thread with text " + text);
                    }
                });*/
                //heartIcon.setImageDrawable(static_context.getResources().getDrawable(R.drawable.icon));


                if (newType != currentType) {
                    beats++;
                    heartIcon.setImageDrawable(static_context.getResources().getDrawable(io.github.utshaw.myhealth.R.drawable.heart));
                    // Log.d(TAG, "BEAT!! beats="+beats);
                }
            } else if (imgAvg > rollingAverage) {

                newType = TYPE.GREEN;
                if (newType != currentType) {
                    heartIcon.setImageDrawable(static_context.getResources().getDrawable(io.github.utshaw.myhealth.R.drawable.heart_pulse));
                    // Log.d(TAG, "BEAT!! beats="+beats);
                }
            }

            if (averageIndex == averageArraySize) averageIndex = 0;
            averageArray[averageIndex] = imgAvg;
            averageIndex++;

            // Transitioned from one state to another to the same
            if (newType != currentType) {
                currentType = newType;
                //image.postInvalidate();
            }

            long endTime = System.currentTimeMillis();
            double totalTimeInSecs = (endTime - startTime) / 1000d;
            int progress = (int) (totalTimeInSecs*10);
            pBar.setProgress(progress);

            if (totalTimeInSecs >= 10) {
                double bps = (beats / totalTimeInSecs);
                int dpm = (int) (bps * 60d);
                if (dpm < 30 || dpm > 180) {
                    startTime = System.currentTimeMillis();
                    beats = 0;
                    processing.set(false);
                    return;
                }

                // Log.d(TAG,
                // "totalTimeInSecs="+totalTimeInSecs+" beats="+beats);

                if (beatsIndex == beatsArraySize) beatsIndex = 0;
                beatsArray[beatsIndex] = dpm;
                beatsIndex++;

                int beatsArrayAvg = 0;
                int beatsArrayCnt = 0;
                for (int i = 0; i < beatsArray.length; i++) {
                    if (beatsArray[i] > 0) {
                        beatsArrayAvg += beatsArray[i];
                        beatsArrayCnt++;
                    }
                }
                int beatsAvg = (beatsArrayAvg / beatsArrayCnt);
                text.setText(String.valueOf(beatsAvg));
                startTime = System.currentTimeMillis();
                beats = 0;

                camera.setPreviewCallback(null);
                camera.stopPreview();
                camera.release();
                camera = null;

                //DatabaseHR db = DatabaseHR.getInstance(static_context);
                //db.insertNewDay(startTime, beatsAvg);
                int tp = static_context.getSharedPreferences("pedometer", Context.MODE_PRIVATE).getInt("points",0);
                static_context.getSharedPreferences("pedometer", Context.MODE_PRIVATE).edit().putInt("points",tp+1).apply();
                static_context.getSharedPreferences("pedometer", Context.MODE_PRIVATE).edit().putLong("time"+Integer.toString(tp),startTime).apply();
                static_context.getSharedPreferences("pedometer", Context.MODE_PRIVATE).edit().putInt("rate"+Integer.toString(tp),beatsAvg).apply();




                //if(isNetworkAvailable(static_context))
                    //sendData();




            }
            processing.set(false);
        }
    };

    private static SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {

        /**
         * {@inheritDoc}
         */
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            try {
                camera.setPreviewDisplay(previewHolder);
                camera.setPreviewCallback(previewCallback);
            } catch (Throwable t) {
                //Log.e("PreviewDemo-surfaceCallback", "Exception in setPreviewDisplay()", t);
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            try {
                Camera.Parameters parameters = camera.getParameters();
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                Camera.Size size = getSmallestPreviewSize(width, height, parameters);
                if (size != null) {
                    parameters.setPreviewSize(size.width, size.height);
                    Log.d(TAG, "Using width=" + size.width + " height=" + size.height);
                }
                camera.setParameters(parameters);
                camera.startPreview();
            }
            catch (Exception e) {}
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            // Ignore
        }
    };

    private static Camera.Size getSmallestPreviewSize(int width, int height, Camera.Parameters parameters) {
        Camera.Size result = null;

        for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
            if (size.width <= width && size.height <= height) {
                if (result == null) {
                    result = size;
                } else {
                    int resultArea = result.width * result.height;
                    int newArea = size.width * size.height;

                    if (newArea < resultArea) result = size;
                }
            }
        }

        return result;
    }

    @Override

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                permit = true;

                /*Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();

                wakeLock.acquire();

                camera = Camera.open();

                startTime = System.currentTimeMillis();*/

            } else {

                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();

            }

        }

    }//end onRequestPermissionsResult

    private static void uploadData(final String timestamp, final String rate) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUtils.BASE_URL_HR, new com.android.volley.Response.Listener<String>() {
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
                Toast.makeText(static_context, "Server Error, Please try again later", Toast.LENGTH_LONG).show();
                //Log.e("ResponseError=", error + "");
                //pbar.setVisibility(View.INVISIBLE);
            }
        })  {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                //oneObject = new JSONObject();
                params.put("time",timestamp);
                params.put("heartRate",rate);




                //params.put(TAG_JOIN_DATE, sJoinDate);
                //params.put(TAG_JOIN_DATE_IN_CURRENT_POSITION, sJoinDateInCurPosition);
//                params.put("mobile", mobile);
//                params.put("token", token);
//                Log.e("Service",mobile + "next");


                return params;
            }
        };

        SingletonVolley.getInstance(static_context).addToRequestQueue(stringRequest);
    }

    private static void sendData(){
        DatabaseHR db = DatabaseHR.getInstance(static_context);

        boolean something = false;
        boolean more = true;

        StringBuilder sbTime = new StringBuilder();
        StringBuilder sbRate = new StringBuilder();
        String prefix = "";

        int startFrom = static_context.getSharedPreferences("pedometer", Context.MODE_PRIVATE)
                .getInt("start",0);
        int next = static_context.getSharedPreferences("pedometer", Context.MODE_PRIVATE)
                .getInt("points",0);

        for(int ix = startFrom; ix < next; ix ++){
            int rate = static_context.getSharedPreferences("pedometer", Context.MODE_PRIVATE)
                    .getInt("rate"+Integer.toString(ix),0);
            long time = static_context.getSharedPreferences("pedometer", Context.MODE_PRIVATE)
                    .getLong("time"+Integer.toString(ix),0);

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

        static_context.getSharedPreferences("pedometer", Context.MODE_PRIVATE).edit()
                .putInt("start",next).apply();

            Log.e("HRsend",sbTime.toString() + "   "+sbRate.toString());
            Log.e("HRsend",sbRate.toString());
            //uploadData(sbTime.toString(), sbRate.toString());

    }

    public static boolean isNetworkAvailable(Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}

