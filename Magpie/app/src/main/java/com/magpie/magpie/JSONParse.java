package com.magpie.magpie;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by Zachary Arrasmith on 3/3/2017.
 */

public class JSONParse extends IntentService {
    private JSONArray json;
    private JSONObject current;
    private String fin, temp = "";
    public JSONParse(){super("JSONParse");}
    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            json = new JSONArray(intent.getStringExtra("JSON"));
            for(int i = 0; i < json.length(); i++) {
                current = json.getJSONObject(i);
                temp += current.getString("Name")+" -- ";
                temp += current.getString("CID")+",";
            }
            fin = temp;
            Intent parsed = new Intent("parsedJSONPassing").putExtra("parsed", fin);
            LocalBroadcastManager.getInstance(this).sendBroadcast(parsed);
        }
        catch(JSONException je){
            je.printStackTrace();
        }
    }
}