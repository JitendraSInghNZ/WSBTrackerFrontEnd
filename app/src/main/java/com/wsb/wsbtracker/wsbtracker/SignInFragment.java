package com.wsb.wsbtracker.wsbtracker;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

/**
 * Created by Jitendra_Singh on 18/04/2016.
 */
public class SignInFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener{

    public static SignInFragment newInstance(){
        return new SignInFragment();
    }
    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;

    private GoogleApiClient mGoogleApiClient;
    private TextView mStatusTextView;
    private ProgressDialog mProgressDialog;
    private  GoogleSignInOptions gso;
    private SignInButton mSignInButton;
    private Button mSignOutButton;
    private Button mDisconnectButton;
    private Button mStartTracking;
    private LinearLayout mLinearLayout;
    private String mLogInId;
    private String mUrl;
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        // [START configure_signin]
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
         gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        // [END configure_signin]

        // [START build_client]
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity()).enableAutoManage(getActivity(),this).addApi(Auth.GOOGLE_SIGN_IN_API,gso).build();
        // [END build_client]

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceSate){
        View view = inflater.inflate(R.layout.fragment_sign_in,container,false);
        //View
        mStatusTextView = (TextView)view.findViewById(R.id.status);
        //Button Listeners

        mSignInButton = (SignInButton)view.findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });


        mSignOutButton = (Button)view.findViewById(R.id.sign_out_button);
        mSignOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });


        mDisconnectButton = (Button)view.findViewById(R.id.disconnect_button);
        mDisconnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                revokeAccess();
            }
        });
        // [START customize_button]
        // Customize sign-in button. The sign-in button can be displayed in
        // multiple sizes and color schemes. It can also be contextually
        // rendered based on the requested scopes. For example. a red button may
        // be displayed when Google+ scopes are requested, but a white button
        // may be displayed when only basic profile is requested. Try adding the
        // Scopes.PLUS_LOGIN scope to the GoogleSignInOptions to see the
        // difference.

        //SignInButton signInButton = (SignInButton)view.findViewById(R.id.sign_in_button);
        mSignInButton.setSize(SignInButton.SIZE_STANDARD);
        mSignInButton.setScopes(gso.getScopeArray());
        mLinearLayout = (LinearLayout)view.findViewById(R.id.sign_out_and_disconnect);

        mStartTracking = (Button)view.findViewById(R.id.set_as_driver_button);
        mStartTracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent service = new Intent(getContext(),LocationUpdateService.class);
                getContext().startService(service);
                //mUrl = LocationUpdateHelper.snewUrlString;
                //Intent intent  = SignInWebViewActivity.newIntene(getActivity(),LocationUpdateHelper.snewUrlString);
                //startActivity(intent);
            }
        });
        // [END customize_button]
        return view;
    }
    public void onStart(){
        if(!isNetworkAvailable()){
            FragmentManager fragmentManager = getFragmentManager();
            ConnectionFragment  dialog = new ConnectionFragment();
            dialog.show(fragmentManager,"DialogConnection");
        }
        super.onStart();
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if(opr.isDone()){
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached Sign_in result");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        }
        else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult result) {
                    hideProgressDialog();
                    handleSignInResult(result);
                }
            });
        }
    }


    // [START onActivityResult]
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if(requestCode == RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }
    // [END onActivityResult]

    private boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getContext().getSystemService(getContext().CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    // [START handleSignInResult]
    private void handleSignInResult(GoogleSignInResult result){
        Log.d(TAG, "handleSignInResult : " + result.isSuccess());
        if(result.isSuccess()){
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount account = result.getSignInAccount();
            mStatusTextView.setText(getString(R.string.signed_in_fmt, account.getDisplayName()));
            mLogInId = account.getEmail();
            updateUI(true);
            //Intent intent = new Intent(getActivity(),SelectionActivity.class);
            //startActivity(intent);
        }
        else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }
    //[END handleSignInResult]


    private void signIn(){
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }




    // [START signOut]
    private void signOut(){
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(Status status) {
                //[START_EXCLUDE]
                updateUI(false);
                //[END_EXCLUDE]
            }
        });
    }
    // [END signOut]



    // [START revokeAccess]
    private void revokeAccess(){
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(Status status) {
                //[START_EXCLUDE]
                updateUI(false);
                //[END_EXCLUDE]
            }
        });
    }
    //[END revokeAccess]
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed" + connectionResult);
    }

    private void showProgressDialog(){
        if(mProgressDialog == null){
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }

    private void hideProgressDialog(){
        if(mProgressDialog != null && mProgressDialog.isShowing()){
            mProgressDialog.hide();
        }
    }

    private void updateUI(boolean signedIn){
        if(signedIn){
            mSignInButton.setVisibility(View.GONE);
            mLinearLayout.setVisibility(View.VISIBLE);
            Intent intent = SchoolCodeActivity.newIntent(getActivity(), mLogInId);
           // startActivity(intent);
        }
        else {
            mStatusTextView.setText(R.string.signed_out);
            mSignInButton.setVisibility(View.VISIBLE);
            mLinearLayout.setVisibility(View.GONE);
        }
    }

}
