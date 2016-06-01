package com.wsb.wsbtracker.wsbtracker;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.HandlerThread;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import java.util.logging.Handler;
import java.util.logging.LogRecord;


public class GMapFollowActivity extends AppCompatActivity implements OnMapReadyCallback{
    private static final String EXTRA_TOKEN_STRING = "com.wsb.wsbtracker.wsbtracker.GMapFollowActivity.token";
    private String mResponseString;

    public static Intent newIntent(Context context, String token){
        Intent intent = new Intent(context,GMapFollowActivity.class);
        intent.putExtra(EXTRA_TOKEN_STRING,token);
        return intent;
    }

    private GoogleMap mGoogleMap;
    private PolylineOptions mPolylineOptions;
    private Marker mMarker;
    private MarkerOptions mMarkerOptions;
    private LatLng mLatLng;
    private String mJsonString;
    private int mInterval = 5000;
    private android.os.Handler mHandler;
    private HandlerThread mHandlerThread;
    //private GMapFollowTask mGMapFollowTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gmap_follow);
        mResponseString = getIntent().getStringExtra(EXTRA_TOKEN_STRING);
        MapFragment mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mHandlerThread = new HandlerThread("HandlerThread");
        mHandlerThread.start();
        mHandler = new android.os.Handler(mHandlerThread.getLooper());
        mHandler.postDelayed(mPositionExtractor,mInterval);

    }

    Runnable mPositionExtractor = new Runnable(){
        GMapFollowTask gMapFollowTask;
        @Override
        public void run() {
            if(gMapFollowTask == null){
                gMapFollowTask = new GMapFollowTask();
            }
            try{
                    mJsonString = gMapFollowTask.execute(mResponseString).get();
               try{
                   JSONObject jsonObject = new JSONObject(mJsonString);
                   double mLatituteDouble = jsonObject.getDouble("Latitude");
                   double mLongitutudeDouble = jsonObject.getDouble("Longitude");
                   mLatLng = new LatLng(mLatituteDouble,mLongitutudeDouble);
                   Log.i("GMapFollowActivity", mLatituteDouble+" "+mLongitutudeDouble);
                    }
               catch (JSONException jse){
                   Log.e("JSONException","error");
               }

                    Toast.makeText(GMapFollowActivity.this, mJsonString, Toast.LENGTH_SHORT).show();
                }

            catch (InterruptedException ie) {

            } catch (ExecutionException ee) {

            }
            finally {
                mHandler.postDelayed(this,mInterval);
                gMapFollowTask = null;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updatePolyline();
                        updateCamera();
                        updateMarker();
                    }
                });
            }
        }
    };

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        //mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        initializePolyline();
    }


    private void initializePolyline() {
        mGoogleMap.clear();
        mPolylineOptions = new PolylineOptions();
        mPolylineOptions.color(Color.BLUE).width(10);
        mGoogleMap.addPolyline(mPolylineOptions);

        mMarkerOptions = new MarkerOptions();
    }

    private void updatePolyline() {
        mPolylineOptions.add(mLatLng);
        mGoogleMap.clear();
        mGoogleMap.addPolyline(mPolylineOptions);
    }

    private void updateCamera() {
        mGoogleMap
                .animateCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 20));
    }

    private void updateMarker() {
        mMarker = mGoogleMap.addMarker(mMarkerOptions.position(mLatLng));
    }

    @Override
    protected void onStop(){
        super.onStop();
        mHandler.removeCallbacks(mPositionExtractor);
        mHandlerThread.quitSafely();
    }
}
