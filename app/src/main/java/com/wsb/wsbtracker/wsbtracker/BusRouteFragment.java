package com.wsb.wsbtracker.wsbtracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by jitus_000 on 7/05/2016.
 */
public class BusRouteFragment extends Fragment{
    private static final String ARG_GOOGLE_SIGN_IN_ID = "sign_in_id";
    private static final String ARG_SCHOOL_CODE = "school_code";
    private static final int BUS_ROUTE_1 = 1;
    private static final int BUS_ROUTE_2 = 2;
    private static final int BUS_ROUTE_3 = 3;

    private int mSchoolCodeInt;
    private String mSignInIdString;
    private int mBusRouteCodeInt;
    private TextView mSignInIdTextView;
    private TextView mSchoolCodeTextView;

    private boolean mRoute1Boolean;
    private boolean mRoute2Boolean;
    private boolean mRoute3Boolean;

    private RadioGroup mRadioGroup;

    private Button mSubmitRouteButton;

    public static BusRouteFragment newInstance(String signInId, int schoolCode){
        Bundle args = new Bundle();
        args.putString(ARG_GOOGLE_SIGN_IN_ID, signInId);
        args.putInt(ARG_SCHOOL_CODE, schoolCode);
        BusRouteFragment busRouteFragment = new BusRouteFragment();
        busRouteFragment.setArguments(args);
        return busRouteFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mSchoolCodeInt = getArguments().getInt(ARG_SCHOOL_CODE);
        mSignInIdString = getArguments().getString(ARG_GOOGLE_SIGN_IN_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInsatanceState){
        View view = inflater.inflate(R.layout.fragment_bus_route,container,false);
        mSignInIdTextView = (TextView)view.findViewById(R.id.logged_in_emai_id_text_view_bus_route);
        mSchoolCodeTextView = (TextView)view.findViewById(R.id.school_code_text_view_bus_route);
        mSignInIdTextView.setText(mSignInIdString);
        mSchoolCodeTextView.setText(String.valueOf(mSchoolCodeInt));
        mRadioGroup = (RadioGroup)view.findViewById(R.id.radio_button_group);
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.route_1_radio_button:
                        Toast.makeText(getActivity(),"Route 1 checked",Toast.LENGTH_SHORT).show();
                        mRoute1Boolean = true;
                        mRoute2Boolean = false;
                        mRoute3Boolean = false;
                        break;
                    case R.id.route_2_radio_button:
                        Toast.makeText(getActivity(),"Route 2 checked",Toast.LENGTH_SHORT).show();
                        mRoute1Boolean = false;
                        mRoute2Boolean = true;
                        mRoute3Boolean = false;
                        break;
                    case R.id.route_3_radio_button:
                        mRoute1Boolean = false;
                        mRoute2Boolean = false;
                        mRoute3Boolean = true;
                        Toast.makeText(getActivity(),"Route 3 checked",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        mSubmitRouteButton = (Button)view.findViewById(R.id.submit_bus_route);
        mSubmitRouteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),String.valueOf(mRoute1Boolean)+" "+String.valueOf(mRoute2Boolean)+" "+String.valueOf(mRoute3Boolean),Toast.LENGTH_SHORT).show();
                Intent intent;
                if(mRoute1Boolean){
                    mBusRouteCodeInt = BUS_ROUTE_1;
                    intent = ChildInfoActivity.newIntent(getActivity(),mSignInIdString,mSchoolCodeInt,mBusRouteCodeInt);
                    startActivity(intent);
                }
                else if(mRoute2Boolean){
                    mBusRouteCodeInt = BUS_ROUTE_2;
                    intent = ChildInfoActivity.newIntent(getActivity(),mSignInIdString,mSchoolCodeInt,mBusRouteCodeInt);
                    startActivity(intent);
                }
                else if(mRoute3Boolean){
                    mBusRouteCodeInt = BUS_ROUTE_3;
                    intent = ChildInfoActivity.newIntent(getActivity(),mSignInIdString,mSchoolCodeInt,mBusRouteCodeInt);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getActivity(),"Please select a bus route",Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }
}
