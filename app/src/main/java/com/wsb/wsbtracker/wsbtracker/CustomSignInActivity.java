package com.wsb.wsbtracker.wsbtracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.JsonSerializer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class CustomSignInActivity extends AppCompatActivity {

    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private Button mSubmitButton;
    private Button mDriveBusButton;
    private TextView mOutputTextView;
    private String mEmail;
    private String mPassword;
    private String mOutput;
    private String mResponse;
    private String mJudgementDriveString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_sign_in);
        mEmailEditText = (EditText)findViewById(R.id.email_id);
        mPasswordEditText = (EditText)findViewById(R.id.password);
        mSubmitButton = (Button)findViewById(R.id.login_button);
        mOutputTextView = (TextView)findViewById(R.id.output);
        mDriveBusButton = (Button)findViewById(R.id.create_bus_button);
        mDriveBusButton.setEnabled(false);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEmail = mEmailEditText.getText().toString();
                mPassword = mPasswordEditText.getText().toString();
                try {
                    mOutput = new SignInHelper().execute(mEmail, mPassword).get();
                } catch (InterruptedException ie) {

                } catch (ExecutionException ee) {

                }
                if (mOutput != null) {
                    String response = null;
                    try {
                        JSONObject json = new JSONObject(mOutput);
                        response = json.getString("Reponse");
                        mResponse = response;
                        Log.i("Reponse", json.toString());
                    } catch (JSONException jse) {
                        Log.e("JSONExeption", jse.getMessage());
                    }
                    if ((response.equals("Unauthorized"))) {
                        mDriveBusButton.setEnabled(false);
                        Toast.makeText(getApplicationContext(), "Invalid User", Toast.LENGTH_SHORT).show();
                    } else {
                        mDriveBusButton.setEnabled(true);
                        //Intent i = DriveAndTrackUIActivity.newIntent(getApplicationContext(),response);
                        //startActivity(i);
                    }
                    mOutputTextView.setText(response);
                }
            }
        });

        mDriveBusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    mJudgementDriveString = new BusDriveHelper().execute(mResponse).get();
                    Log.i("CustomSignInActivity",mJudgementDriveString);
                }
                catch (InterruptedException ie) {

                } catch (ExecutionException ee) {

                }

                if(!mJudgementDriveString.equals(null)){
                    Intent i = DriveAndTrackUIActivity.newIntent(getApplicationContext(),mResponse);
                    startActivity(i);
                }
            }
        });
    }

}
