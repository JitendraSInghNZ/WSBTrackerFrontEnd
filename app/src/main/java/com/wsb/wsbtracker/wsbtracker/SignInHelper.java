package com.wsb.wsbtracker.wsbtracker;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by jitus_000 on 13/05/2016.
 */
public class SignInHelper extends AsyncTask<String, Void, String>{
    String finalOutput;
    @Override
    protected String doInBackground(String... params) {
        BufferedReader reader= null;
        String JSONResponse = null;
        StringBuilder stringBuilder = new StringBuilder();
        HttpsURLConnection httpsURLConnection = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Email",params[0]);
            jsonObject.put("Password",params[1]);
        }
        catch (JSONException jse){

        }
        try {
            URL url = new URL("https://wsbtracker.appspot.com/login");
            httpsURLConnection = (HttpsURLConnection)url.openConnection();
            httpsURLConnection.setConnectTimeout(10000);
            httpsURLConnection.setRequestMethod("POST");
            httpsURLConnection.setDoOutput(true);
            httpsURLConnection.setDoInput(true);
            httpsURLConnection.setRequestProperty("Content-Type","application/json");
            httpsURLConnection.setRequestProperty("Accept","application/json");
            httpsURLConnection.setRequestProperty("Accept-Encoding", "gzip");
          //  httpsURLConnection.setRequestProperty("X-Requested-With", "XMLHttpRequest");
            httpsURLConnection.connect();
           // Writer writer = new BufferedWriter(new OutputStreamWriter(httpsURLConnection.getOutputStream()));
            OutputStream outputStream = httpsURLConnection.getOutputStream();
            byte[] outputInBytes = jsonObject.toString().getBytes("UTF-8");
            outputStream.write(outputInBytes);
            //Log.i("Header", httpsURLConnection.getOutputStream().toString());
            outputStream.flush();
            outputStream.close();
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
                    Log.e("Stream"," closing stream error");
                }
            }
        }
//        Log.i("finaloutput",finalOutput);
        return finalOutput;
    }


    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }

}
