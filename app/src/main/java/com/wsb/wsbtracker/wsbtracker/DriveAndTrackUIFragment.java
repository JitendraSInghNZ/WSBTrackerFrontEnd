package com.wsb.wsbtracker.wsbtracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by jitus_000 on 18/05/2016.
 */
public class DriveAndTrackUIFragment extends Fragment{

    private static final String ARG_RESPONSE_CODE = "response_code";
    private TextView mResponseCodeTextView;
    private String mResponseCodeString;
    private Button mStartDrivingButton;
    private Button mStartTrackingButton;
    private Button mCoordinateButton;
    private boolean mToogleDriveBoolean;
    private Intent service;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //service = new Intent(getContext(),LocationUpdateService.class);
        mResponseCodeString = getArguments().getString(ARG_RESPONSE_CODE);
        LocationUpdateHelper.RESPONSE_CODE = mResponseCodeString;
        mToogleDriveBoolean = false;
    }

    public static DriveAndTrackUIFragment newInstance(String responseCode){
        Bundle args = new Bundle();
        args.putString(ARG_RESPONSE_CODE, responseCode);
        DriveAndTrackUIFragment fragment = new DriveAndTrackUIFragment();
        fragment.setArguments(args);
        return fragment;
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_drive_and_track_ui,container,false);
        mResponseCodeTextView = (TextView)view.findViewById(R.id.response_code_text_view);
        mResponseCodeTextView.setText(mResponseCodeString);
        mStartDrivingButton = (Button)view.findViewById(R.id.start_driving_button);
        mStartDrivingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mToogleDriveBoolean == false) {
                    LocationUpdateHelper.sStartDriving = true;
                    getContext().startService(new Intent(getContext(), LocationUpdateService.class));
                    mToogleDriveBoolean = true;

                    Toast.makeText(getContext(), String.valueOf(mToogleDriveBoolean), Toast.LENGTH_SHORT).show();
                } else if (mToogleDriveBoolean == true) {
                    LocationUpdateHelper.sStartDriving = false;
                    getContext().stopService(new Intent(getContext(), LocationUpdateService.class));
                    mToogleDriveBoolean = false;
                    Toast.makeText(getContext(), String.valueOf(mToogleDriveBoolean), Toast.LENGTH_SHORT).show();
                }
            }
        });
        mStartTrackingButton = (Button)view.findViewById(R.id.view_bus_location_button);
        mStartTrackingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(getActivity(),GMapFollowActivity.class);
                Intent intent = GMapFollowActivity.newIntent(getActivity(),mResponseCodeString);
                startActivity(intent);
            }
        });
        mCoordinateButton = (Button)view.findViewById(R.id.coordinate_task_button);
        return view;
    }
}
