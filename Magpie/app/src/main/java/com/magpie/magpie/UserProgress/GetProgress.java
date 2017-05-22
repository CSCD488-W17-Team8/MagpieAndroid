package com.magpie.magpie.UserProgress;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Zachary Arrasmith on 5/16/2017.
 */

public class GetProgress extends IntentService {

    String test = "{\"id_token\": \"eyJhbGciOiJSUzI1NiIsImtpZCI6IjMwOGYyNDg3NTZiNWY2ZWU0ZGQ0YzVkODBiNTU4NTA5OTdmZmRlN2YifQ.eyJhenAiOiIxMDUxNTQ5ODQzNDk4LXZrYXEzMWoyNWZhdWk2ajZ0cDZoY2NydWxya2x2bWR0LmFwcHMuZ29vZ2xldXNlcmNvbnRlbnQuY29tIiwiYXVkIjoiMTA1MTU0OTg0MzQ5OC12a2FxMzFqMjVmYXVpNmo2dHA2aGNjcnVscmtsdm1kdC5hcHBzLmdvb2dsZXVzZXJjb250ZW50LmNvbSIsInN1YiI6IjEwODk4NDgxMjY1NjYzODk1MDEwNiIsImVtYWlsIjoiemFjaGFyeWFycmFzbWl0aEBnbWFpbC5jb20iLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwiYXRfaGFzaCI6InpoQkl1SkV5dVdkWEFpeHRaeHpFUFEiLCJpc3MiOiJhY2NvdW50cy5nb29nbGUuY29tIiwiaWF0IjoxNDk1NDE4MjgwLCJleHAiOjE0OTU0MjE4ODAsIm5hbWUiOiJaYWNoYXJ5IEFycmFzbWl0aCIsInBpY3R1cmUiOiJodHRwczovL2xoNC5nb29nbGV1c2VyY29udGVudC5jb20vLTR0ZUdZSmdqZTgwL0FBQUFBQUFBQUFJL0FBQUFBQUFBQUM4L01ad1JEUWFNR1JNL3M5Ni1jL3Bob3RvLmpwZyIsImdpdmVuX25hbWUiOiJaYWNoYXJ5IiwiZmFtaWx5X25hbWUiOiJBcnJhc21pdGgiLCJsb2NhbGUiOiJlbiJ9.FjEYFWjOtss-DFW-6Zv20IuGsEpsguqCdkrG6nQ96LNT0vFuPkgQ_4U8_HvcFkxX3zOCbCBNFZ7AkBHprUfmhkYUx7eZP0-Ur9rT3zevySbHo6wLhrgmc2ydwV0m_BRpyaKyxcDvSt6ClipaeRfYmylntA2LZvCVQSwGJH628dSgP-rRaattNSoWE1pY8tyeKw0IYbyFFQaABuzUHvCfVUNpze8_iVzXICfMdc5TOBn6j1DYJYBr6m57ZDGie3BP0-lbnJDGsiaOEuTP6FyZHnv0PnB7klSrpgiGEinnoDBxQBPyspjWntq546RziiqeXCGACioMp8A8ZIbu65LPsA\"}";

    public GetProgress(){super("GetProgress");}
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String json = "";
        try {
            HttpURLConnection hurl = (HttpURLConnection) new URL("http://magpiehunt.com/api/user/app/progress/pull").openConnection();
            hurl.setRequestMethod("POST");
            hurl.setRequestProperty("Content-Type", "application/json");
            hurl.setRequestProperty("Accept", "application/json");
            hurl.setDoOutput(true);
            hurl.setDoInput(true);
            OutputStream oput = hurl.getOutputStream();
            oput.write(test.getBytes("UTF-8"));
            oput.close();
            Log.d("GETPROGRESSRESPONSE", hurl.getResponseMessage());
            InputStream is = hurl.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder jsonBuilder = new StringBuilder();
            String temp = "";
            while ((temp = reader.readLine()) != null) {
                jsonBuilder.append(temp);
            }
            json = jsonBuilder.toString();
            JSONArray jArray   = new JSONArray(json);
            Log.d("PULLRESULT", jArray.getJSONObject(0).toString(1));
            Intent back = new Intent("ProgressFromCMS").putExtra("ProgressJSONFromCMS", json);
            LocalBroadcastManager.getInstance(this).sendBroadcast(back);
        }
        catch (Exception e){
            Log.d("GETPROGRESSEXCEP", e.getMessage());
        }
    }
}
