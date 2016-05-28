package com.wsb.wsbtracker.wsbtracker;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by jitus_000 on 23/05/2016.
 */
public class GMapFollowTask extends AsyncTask<String,Void,String>{
    String finalOutput;

    @Override
    protected String doInBackground(String... params) {
        BufferedReader reader= null;
        String JSONResponse = null;
        StringBuilder stringBuilder = new StringBuilder();
        HttpsURLConnection httpsURLConnection = null;
        try {
            String urlString = Uri.parse("https://wsbtracker.appspot.com/busses/location").buildUpon().appendQueryParameter("token",params[0]).appendQueryParameter("busNumber","202").build().toString();
            URL url = new URL(urlString);
            Log.i("GMapFollowTask",url.toString());
            httpsURLConnection = (HttpsURLConnection)url.openConnection();
            httpsURLConnection.setConnectTimeout(10000);
            httpsURLConnection.setRequestMethod("GET");
            httpsURLConnection.setDoOutput(true);
            httpsURLConnection.setDoInput(true);
            httpsURLConnection.setRequestProperty("Content-Type","application/json");
            httpsURLConnection.setRequestProperty("Accept","application/json");
            httpsURLConnection.setRequestProperty("Accept-Encoding", "gzip");
            httpsURLConnection.connect();
            reader = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream()));
            while ((JSONResponse = reader.readLine()) != null){
                stringBuilder.append(JSONResponse);
            }
            finalOutput = stringBuilder.toString();
        }
        catch (MalformedURLException mue){

        }
        catch (IOException ioe){

        }
        finally {
            if(httpsURLConnection != null){
                httpsURLConnection.disconnect();
            }
            if(reader != null){
                try {
                    reader.close();
                }
                catch (IOException ioe){
                    Log.e("Stream", " closing stream error");
                }
            }
        }
        Log.i("finaloutput",finalOutput);
        return finalOutput;
    }
}
