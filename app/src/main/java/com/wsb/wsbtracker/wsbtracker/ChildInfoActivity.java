package com.wsb.wsbtracker.wsbtracker;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

/**
 * Created by jitus_000 on 8/05/2016.
 */
public class ChildInfoActivity extends SingleFragmentActivity{

    private static final String EXTRA_GOOGLE_SIGN_IN_ID = "com.wsb.wsbtracker.wsbtracker.google_sign_in_id";
    private static final String EXTRA_SCHOOL_CODE = "com.wsb.wsbtracker.wsbtracker.school_code";
    private static final String EXTRA_BUS_ROUTE_CODE = "com.wsb.wsbtracker.wsbtracker.bus_route_code";

    public static Intent newIntent(Context packageContext, String googleSignInId, int schoolCode, int busRouteCode){
        Intent intent = new Intent(packageContext,ChildInfoActivity.class);
        return intent.putExtra(EXTRA_GOOGLE_SIGN_IN_ID,googleSignInId).putExtra(EXTRA_SCHOOL_CODE,schoolCode).putExtra(EXTRA_BUS_ROUTE_CODE,busRouteCode);
    }

    @Override
    protected Fragment createFragment() {
        String signInId = getIntent().getStringExtra(EXTRA_GOOGLE_SIGN_IN_ID);
        int schoolCode = getIntent().getIntExtra(EXTRA_SCHOOL_CODE, 1);
        int busRouteCode = getIntent().getIntExtra(EXTRA_BUS_ROUTE_CODE,1);
        ChildInfoFragment childInfoFragment = ChildInfoFragment.newInstance(signInId,schoolCode,busRouteCode);
        return childInfoFragment;
    }
}
