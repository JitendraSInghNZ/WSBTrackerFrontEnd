package com.wsb.wsbtracker.wsbtracker.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.wsb.wsbtracker.wsbtracker.R;
import com.wsb.wsbtracker.wsbtracker.constants.RegistrationConstants;
import com.wsb.wsbtracker.wsbtracker.constants.StoredRegistrationConstant;

/**
 * Created by jitus_000 on 1/06/2016.
 */
public class RegistrationIntentService extends IntentService{
    private static final String TAG = "RegIntentService";

    public RegistrationIntentService(){
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        try{
            InstanceID instanceID = InstanceID.getInstance(this);
            String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            Log.i(TAG,"GCM Registration Token: "+token);
            final SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(RegistrationConstants.SENT_TOKEN_TO_SERVER, true);
            editor.putString(RegistrationConstants.TOKEN, token);
            editor.apply();
            StoredRegistrationConstant.TOKEN_ID = token;
        }
        catch(Exception e){
            Log.d(TAG, "Failed to complete token refresh", e);
            sharedPreferences.edit().putBoolean(RegistrationConstants.
                    SENT_TOKEN_TO_SERVER, false).apply();
        }
        Intent registrationComplete = new Intent(RegistrationConstants.REGISTRATION_COMPLETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }
}
