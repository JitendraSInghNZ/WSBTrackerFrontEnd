package com.wsb.wsbtracker.wsbtracker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class BusRouteActivity extends SingleFragmentActivity {
    private static final String EXTRA_GOOGLE_SIGN_IN_ID = "com.wsb.wsbtracker.wsbtracker.google_sign_in_id";
    private static final String EXTRA_SCHOOL_CODE = "com.wsb.wsbtracker.wsbtracker.school_code";

    public static Intent newIntent(Context packageContext, String googleSignInId, int schoolCode){
        Intent intent = new Intent(packageContext, BusRouteActivity.class);
        intent.putExtra(EXTRA_GOOGLE_SIGN_IN_ID,googleSignInId).putExtra(EXTRA_SCHOOL_CODE,schoolCode);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        String signInId = getIntent().getStringExtra(EXTRA_GOOGLE_SIGN_IN_ID);
        int schoolCode = getIntent().getIntExtra(EXTRA_SCHOOL_CODE,1);
        return BusRouteFragment.newInstance(signInId,schoolCode);
    }
}
