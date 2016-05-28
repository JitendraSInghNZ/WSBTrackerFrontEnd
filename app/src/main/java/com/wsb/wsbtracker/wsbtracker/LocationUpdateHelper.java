package com.wsb.wsbtracker.wsbtracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.http.HttpResponseCache;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.firebase.client.Firebase;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by jitus_000 on 6/05/2016.
 */
public class LocationUpdateHelper implements ConnectionCallbacks,OnConnectionFailedListener, LocationListener{
    private static final String TAG = "location-updates-sample";
    private JSONObject mJSONObject = new JSONObject();
    private Context mContext;
    public LocationUpdateHelper(Context context){
        mContext = context;
    }
    static boolean sStartDriving;
    /**
     *The desired interval for location updates. Inexact. Updates may be more or less frequent
     */
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    /**
     *The fastest rate for active location updates. Exact. Updates will never be more frequent than this value
     */
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECOND = UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    //keys for storing activity state in bundle
    static String RESPONSE_CODE;

    /**
     *Tracks the status of location updates request. Value changes when the user presses the start updates button and stop updates button
     */
    static boolean  mActivityReturn = true;
    protected Boolean mRequestingLocationUpdates;
    protected final static String REQUESTING_LOCATION_UPDATES_KEY = "requesting-location-updates-key";
    protected final static  String LOCATION_KEY = "location-key";
    protected final static String LAST_UPDATED_TIME_STRING_KEY = "last-updated-time-string-key";

    /**
     * Provides an entry point to Google Play Services
     */

    /**
     * Provides an entry point to Google Play Services
     */

    protected GoogleApiClient mGoogleApiClient;


    /**
     *Stores parameteres for request to the FusedLocationProviderAPI.
     */

    protected LocationRequest mLocationRequest;

    /**
     *Represents a geographical location
     */
    LocationTask mLocationTask = new LocationTask();
    protected Location mCurrentLocation;

    public void setUp(){

        buildGoogleApiClient();
        mGoogleApiClient.connect();
        sStartDriving = true;
        if(mGoogleApiClient.isConnected()){
            startLocationUpdates();
        }
    }


    protected synchronized void  buildGoogleApiClient(){
        Log.i(TAG, "Building GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(mContext).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        createLocationRequest();
    }

    protected void createLocationRequest(){
        mLocationRequest = new LocationRequest();
        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECOND);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void startLocationUpdates(){
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }


    protected void stopLocationUpdates(){
        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.

        // The final argument to {@code requestLocationUpdates()} is a LocationListener

        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        mGoogleApiClient.disconnect();
    }


    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "connected to GoogleApiClient");
        // If the initial location was never previously requested, we use
        // FusedLocationApi.getLastLocation() to get it. If it was previously requested, we store
        // its value in the Bundle and check for it in onCreate(). We
        // do not request it again unless the user specifically requests location updates by pressing
        // the Start Updates button.
        //
        // Because we cache the value of the initial location in the Bundle, it means that if the
        // user launches the activity,
        // moves to a new location, and then changes the device orientation, the original location
        // is displayed as the activity is re-created.

        if(mCurrentLocation == null){
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            //sendLocationData();
            if(mLocationTask == null){
                mLocationTask = new LocationTask();
            }
            mLocationTask.execute(RESPONSE_CODE);
            mLocationTask = null;
        }
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        if(sStartDriving) {
            mCurrentLocation = location;
            if (mLocationTask == null) {
                mLocationTask = new LocationTask();
            }
            String locationResponse;
            try {
                locationResponse = mLocationTask.execute(RESPONSE_CODE).get();
                Log.i("LocationResponse", locationResponse);
            } catch (InterruptedException ie) {

            } catch (ExecutionException ee) {

            }

            mLocationTask = null;
        }
        else if(!sStartDriving){
            stopLocationUpdates();
        }
    }



    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorMessage() = " + connectionResult.getErrorMessage());
    }

    public class LocationTask extends AsyncTask<String,Void,String>{
        String finalOutput;
        @Override
        protected String doInBackground(String... params) {
            BufferedReader reader= null;
            String JSONResponse = null;
            StringBuilder stringBuilder = new StringBuilder();
            HttpsURLConnection httpsURLConnection = null;
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("Latitude",mCurrentLocation.getLatitude());
                jsonObject.put("Longitude",mCurrentLocation.getLongitude());
                jsonObject.put("Token",params[0]);
            }
            catch (JSONException jse){

            }
            try {
                URL url = new URL("https://wsbtracker.appspot.com/position/log");
                httpsURLConnection = (HttpsURLConnection)url.openConnection();
                httpsURLConnection.setConnectTimeout(10000);
                httpsURLConnection.setRequestMethod("POST");
                httpsURLConnection.setDoOutput(true);
                httpsURLConnection.setDoInput(true);
                httpsURLConnection.setRequestProperty("Content-Type","application/json");
                httpsURLConnection.setRequestProperty("Accept","application/json");
                httpsURLConnection.setRequestProperty("Accept-Encoding", "gzip");
                httpsURLConnection.connect();
                OutputStream outputStream = httpsURLConnection.getOutputStream();
                byte[] outputInBytes = jsonObject.toString().getBytes("UTF-8");
                outputStream.write(outputInBytes);
                outputStream.close();
                reader = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream()));
                while ((JSONResponse = reader.readLine()) != null){
                    stringBuilder.append(JSONResponse);
                }
                finalOutput = stringBuilder.toString();
            }
            catch (MalformedURLException mue){

            }
            catch (IOException ioe){

            }
            finally {
                if(httpsURLConnection != null){
                    httpsURLConnection.disconnect();
                }
                if(reader != null){
                    try {
                        reader.close();
                    }
                    catch (IOException ioe){
                        Log.e("Stream"," closing stream error");
                    }
                }
            }
            Log.i("finaloutput",finalOutput);
            return finalOutput;
        }
    }
}
