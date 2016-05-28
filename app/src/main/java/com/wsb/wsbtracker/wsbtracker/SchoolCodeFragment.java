package com.wsb.wsbtracker.wsbtracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by jitus_000 on 7/05/2016.
 */
public class SchoolCodeFragment extends Fragment{
    private static final String ARG_GOOGLE_SIGN_IN_ID = "sign_in_id";
    private TextView mLoginIdTextView;
    private Button mSubmitButton;
    private EditText mBusIdEditText;
    private int mSchoolCodeInt;
    private String mSchoolCodeString;
    private String mSignInIdString;

    public static SchoolCodeFragment newInstance(String signInId){
        Bundle args = new Bundle();
        args.putString(ARG_GOOGLE_SIGN_IN_ID, signInId);
        SchoolCodeFragment fragment = new SchoolCodeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mSignInIdString = getArguments().getString(ARG_GOOGLE_SIGN_IN_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_school_code,container,false);
        mLoginIdTextView = (TextView)view.findViewById(R.id.logged_in_emai_id_text_view);
        mLoginIdTextView.setText(mSignInIdString);
        mBusIdEditText = (EditText)view.findViewById(R.id.bus_id_edit_text);
        mBusIdEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mSchoolCodeString = s.toString();

            }

            @Override
            public void afterTextChanged(Editable s) {
                //
            }
        });



        mSubmitButton = (Button)view.findViewById(R.id.submit_button);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if(mSchoolCodeString == null || mSchoolCodeString.equals("")){
                    Toast.makeText(getContext(),"Please Enter School Code",Toast.LENGTH_SHORT).show();
                    return;
                }

                else {
                    mSchoolCodeInt = Integer.valueOf(mSchoolCodeString);
                    //Toast.makeText(getActivity(), String.valueOf(mSchoolCodeInt), Toast.LENGTH_SHORT).show();
                    Intent intent = BusRouteActivity.newIntent(getActivity(),mSignInIdString,mSchoolCodeInt);
                    startActivity(intent);
                }

            }
        });
        return view;
    }
}
