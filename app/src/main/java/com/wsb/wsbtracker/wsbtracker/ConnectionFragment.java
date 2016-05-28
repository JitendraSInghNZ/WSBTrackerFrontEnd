package com.wsb.wsbtracker.wsbtracker;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

/**
 * Created by jitus_000 on 6/05/2016.
 */
public class ConnectionFragment extends DialogFragment{

    public Dialog onCreateDialog(Bundle savedInstanceState){
        return new AlertDialog.Builder(getActivity()).setTitle(R.string.internet_connection_unavailable).setPositiveButton(android.R.string.ok,null).create();
    }
}
