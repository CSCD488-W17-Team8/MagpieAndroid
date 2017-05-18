package com.magpie.magpie.UserProgress;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Zachary Arrasmith on 5/16/2017.
 */

public class GetProgress extends IntentService {

    public GetProgress(){super("GetProgress");}
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        try {
            String json;
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
            Intent back = new Intent("ProgressFromCMS").putExtra("ProgressJSONFromCMS", json);
            LocalBroadcastManager.getInstance(this).sendBroadcast(back);
        }
        catch (Exception e){
            Log.d("GETPROGRESSEXCEP", e.getMessage());
        }
    }
}
