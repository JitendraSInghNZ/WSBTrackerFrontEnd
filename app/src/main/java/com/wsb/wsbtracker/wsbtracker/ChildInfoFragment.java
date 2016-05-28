package com.wsb.wsbtracker.wsbtracker;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by jitus_000 on 8/05/2016.
 */
public class ChildInfoFragment extends Fragment{

    private static final String ARG_GOOGLE_SIGN_IN_ID = "sign_in_id";
    private static final String ARG_SCHOOL_CODE = "school_code";
    private static final String ARG_BUS_ROUTE_ID = "bus_route_id";

    private TextView mSignInIdTextView;
    private TextView mSchoolCodeTextView;
    private TextView mBusRouteCodeTextView;

    private int mSchoolCodeInt;
    private String mSignInIdString;
    private int mBusRouteCodeInt;

    public static final ChildInfoFragment newInstance(String signInId, int schoolCode, int busRouteId){
        Bundle args = new Bundle();
        args.putString(ARG_GOOGLE_SIGN_IN_ID, signInId);
        args.putInt(ARG_SCHOOL_CODE, schoolCode);
        args.putInt(ARG_BUS_ROUTE_ID, busRouteId);

        ChildInfoFragment childInfoFragment = new ChildInfoFragment();
        childInfoFragment.setArguments(args);
        return childInfoFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mSchoolCodeInt = getArguments().getInt(ARG_SCHOOL_CODE);
        mSignInIdString = getArguments().getString(ARG_GOOGLE_SIGN_IN_ID);
        mBusRouteCodeInt = getArguments().getInt(ARG_BUS_ROUTE_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_child_info,container,false);
        mSignInIdTextView = (TextView)view.findViewById(R.id.logged_in_email_id_text_view_child_info);
        mSignInIdTextView.setText(mSignInIdString);
        mBusRouteCodeTextView = (TextView)view.findViewById(R.id.bus_route_code_text_view_child_info);
        mBusRouteCodeTextView.setText(String.valueOf(mBusRouteCodeInt));
        mSchoolCodeTextView = (TextView)view.findViewById(R.id.school_code_text_view_child_info);
        mSchoolCodeTextView.setText(String.valueOf(mSchoolCodeInt));
        return view;
    }
}
