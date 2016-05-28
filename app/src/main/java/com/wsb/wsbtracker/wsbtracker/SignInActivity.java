package com.wsb.wsbtracker.wsbtracker;

import android.support.v4.app.Fragment;

/**
 * Created by Jitendra Singh 18/04/16
 * Fragment hosting activity
 */
public class SignInActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return SignInFragment.newInstance();
    }
}
