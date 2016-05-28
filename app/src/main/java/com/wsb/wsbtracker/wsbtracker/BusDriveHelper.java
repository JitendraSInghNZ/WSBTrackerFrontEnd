package com.wsb.wsbtracker.wsbtracker;

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
public class BusDriveHelper extends AsyncTask<String,Void,String>{
    String finalOutput;
    @Override
    protected String doInBackground(String... params) {
        BufferedReader reader= null;
        String JSONResponse = null;
        StringBuilder stringBuilder = new StringBuilder();
        HttpsURLConnection httpsURLConnection = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Bus",new JSONObject().put("Number",202));
            jsonObject.put("Operation","drive");
            jsonObject.put("Token",params[0]);
        }
        catch (JSONException jse){
             Log.e("BusDriveHelper",jse.toString());
        }
        try {
            URL url = new URL("https://wsbtracker.appspot.com/busses/drive");
            httpsURLConnection = (HttpsURLConnection)url.openConnection();
            httpsURLConnection.setConnectTimeout(10000);
            httpsURLConnection.setRequestMethod("POST");
            httpsURLConnection.setDoOutput(true);
            httpsURLConnection.setDoInput(true);
            httpsURLConnection.setRequestProperty("Content-Type","application/json");
            httpsURLConnection.setRequestProperty("Accept","application/json");
            httpsURLConnection.setRequestProperty("Accept-Encoding", "gzip");
            httpsURLConnection.connect();
            OutputStream outputStream = httpsURLConnection.getOutputStream();
            byte[] outputInBytes = jsonObject.toString().getBytes("UTF-8");
            outputStream.write(outputInBytes);
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
                    Log.e("Stream", " closing stream error");
                }
            }
        }
           Log.i("finaloutput",finalOutput);
        return finalOutput;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }
}
