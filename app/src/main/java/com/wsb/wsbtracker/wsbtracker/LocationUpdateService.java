package com.wsb.wsbtracker.wsbtracker;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.ViewGroup;

import com.firebase.client.Firebase;

/**
 * Created by jitus_000 on 6/05/2016.
 */
public class LocationUpdateService extends Service{
    LocationUpdateTask locationUpdateTask;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        Firebase.setAndroidContext(this);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        locationUpdateTask.cancel(true);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
         locationUpdateTask = new LocationUpdateTask();

        locationUpdateTask.execute();
        return START_NOT_STICKY;
    }

    @Override
    public boolean stopService(Intent intent){
        locationUpdateTask.cancel(true);

        return super.stopService(intent);
    }

    public class LocationUpdateTask extends AsyncTask<Void,Void,Void>{
        LocationUpdateHelper mLocationUpdateHelper = new LocationUpdateHelper(getApplicationContext());
        @Override
        protected Void doInBackground(Void... params){
            mLocationUpdateHelper.setUp();
            return null;
        }
    }

}
