package com.magpie.magpie;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Zachary Arrasmith on 3/3/2017.
 */

public class JSONGet extends IntentService {
    private String json;
    public JSONGet(){super("JSONGet");}
    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            String type = intent.getStringExtra("Type");
            HttpURLConnection hurl = (HttpURLConnection) new URL("http://magpiehunt.com/api/collection/").openConnection();
            hurl.connect();
            InputStream is = hurl.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder jsonBuilder = new StringBuilder();
            String temp = "";
            while ((temp = reader.readLine()) != null) {
                jsonBuilder.append(temp);
            }
            json = jsonBuilder.toString();
            Intent local = new Intent("Passing").putExtra("JSON", json);
            LocalBroadcastManager.getInstance(this).sendBroadcast(local);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}