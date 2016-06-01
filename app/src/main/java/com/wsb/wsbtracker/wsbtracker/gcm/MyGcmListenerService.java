package com.wsb.wsbtracker.wsbtracker.gcm;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.wsb.wsbtracker.wsbtracker.R;

/**
 * Created by jitus_000 on 1/06/2016.
 */
public class MyGcmListenerService extends GcmListenerService{
    private static final String TAG = "MyGcmListenerService";
    public static final int MESSAGE_NOTIFICATION_ID = 435345;

    @Override
    public void onMessageReceived(String from, Bundle data){
        String message = data.getString("message");
        Log.i(TAG,"From : "+from);
        Log.i(TAG,"Message : "+message);
    }

    private void sendNotification(String title, String body){
        Context context = getBaseContext();
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context).setSmallIcon(R.mipmap.logo).setContentTitle(title).setContentText(body);
        NotificationManager mNotificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(MESSAGE_NOTIFICATION_ID,mBuilder.build());
    }
}
