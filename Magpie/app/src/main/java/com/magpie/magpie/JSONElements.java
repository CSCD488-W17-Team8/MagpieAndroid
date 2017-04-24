package com.magpie.magpie;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Zachary Arrasmith on 4/5/2017.
 */

public class JSONElements extends IntentService {
    private String json;
    public JSONElements(){super("JSONElements");}
    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            json = "";
            String type = intent.getStringExtra("SelectedCollectionCIDs");
            String[] selected = type.split(",");
            for(String s : selected){
                HttpURLConnection hurl = (HttpURLConnection) new URL("http://magpiehunt.com/api/landmark/all/" + s).openConnection();
                hurl.connect();
                InputStream is = hurl.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                StringBuilder jsonBuilder = new StringBuilder();
                String temp = "";
                while ((temp = reader.readLine()) != null) {
                    jsonBuilder.append(temp);
                }
                json += jsonBuilder.toString() + "%";
            }
            Intent loc = new Intent("Elements").putExtra("CollectionElements", json);
            LocalBroadcastManager.getInstance(this).sendBroadcast(loc);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
