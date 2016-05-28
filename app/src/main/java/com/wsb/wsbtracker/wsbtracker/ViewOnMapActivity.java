package com.wsb.wsbtracker.wsbtracker;

import android.support.v4.app.Fragment;

/**
 * Created by jitus_000 on 18/05/2016.
 */
public class ViewOnMapActivity extends SingleFragmentActivity{
    @Override
    protected Fragment createFragment() {
        return new ViewOnMapFragment();
    }
}
