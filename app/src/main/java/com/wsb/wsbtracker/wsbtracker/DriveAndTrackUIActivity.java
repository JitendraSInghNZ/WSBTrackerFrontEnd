package com.wsb.wsbtracker.wsbtracker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by jitus_000 on 18/05/2016.
 */
public class DriveAndTrackUIActivity extends SingleFragmentActivity{
    private static final String EXTRA_RESPONSE_CODE = "com.wsb.wsbtracker.wsbtracker.drive_and_track_ui_activity.response_code";


    public static Intent newIntent(Context context, String responseCode){
        Intent i = new Intent(context,DriveAndTrackUIActivity.class);
        i.putExtra(EXTRA_RESPONSE_CODE,responseCode);
        return i;
    }

    @Override
    protected Fragment createFragment() {
        String responseCode = getIntent().getStringExtra(EXTRA_RESPONSE_CODE);
        return DriveAndTrackUIFragment.newInstance(responseCode);
    }
}
