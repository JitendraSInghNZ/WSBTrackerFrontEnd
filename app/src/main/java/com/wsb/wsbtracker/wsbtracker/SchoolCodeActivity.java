package com.wsb.wsbtracker.wsbtracker;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

/**
 * Created by jitus_000 on 7/05/2016.
 */
public class SchoolCodeActivity extends SingleFragmentActivity{
    private static final String EXTRA_GOOGLE_SIGN_IN_ID = "com.wsb.wsbtracker.wsbtracker.google_sign_in_id";


    public static Intent newIntent(Context packageContext, String googleSignInId){
        Intent intent = new Intent(packageContext, SchoolCodeActivity.class);
        intent.putExtra(EXTRA_GOOGLE_SIGN_IN_ID,googleSignInId);
        return intent;
    }


    @Override
    protected Fragment createFragment(){
        String googleSignInId = getIntent().getStringExtra(EXTRA_GOOGLE_SIGN_IN_ID);
        return SchoolCodeFragment.newInstance(googleSignInId);
    }
}
