package com.magpie.magpie.Services.UserProgress;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.magpie.magpie.CollectionUtils.Collection;
import com.magpie.magpie.CollectionUtils.Element;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Zachary Arrasmith on 5/16/2017.
 */

public class SendProgress extends IntentService {

    public SendProgress(){super("SendProgress");}
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        ArrayList<Collection> userCollections = (ArrayList<Collection>)intent.getSerializableExtra("UserProgressCollection");
        String userID = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjJiMmU0ZDZmMTgyMWFkNGQ3NmQ0NTUzYzk1MWI3NTYyMmFjYjY1MzkifQ.eyJhenAiOiIxMDUxNTQ5ODQzNDk4LXZrYXEzMWoyNWZhdWk2ajZ0cDZoY2NydWxya2x2bWR0LmFwcHMuZ29vZ2xldXNlcmNvbnRlbnQuY29tIiwiYXVkIjoiMTA1MTU0OTg0MzQ5OC12a2FxMzFqMjVmYXVpNmo2dHA2aGNjcnVscmtsdm1kdC5hcHBzLmdvb2dsZXVzZXJjb250ZW50LmNvbSIsInN1YiI6IjEwODk4NDgxMjY1NjYzODk1MDEwNiIsImVtYWlsIjoiemFjaGFyeWFycmFzbWl0aEBnbWFpbC5jb20iLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwiYXRfaGFzaCI6Ik9MTGdvTDJkOFZSdThIQlpSaDhYSFEiLCJpc3MiOiJhY2NvdW50cy5nb29nbGUuY29tIiwiaWF0IjoxNDk1MjQ1Nzk4LCJleHAiOjE0OTUyNDkzOTgsIm5hbWUiOiJaYWNoYXJ5IEFycmFzbWl0aCIsInBpY3R1cmUiOiJodHRwczovL2xoNC5nb29nbGV1c2VyY29udGVudC5jb20vLTR0ZUdZSmdqZTgwL0FBQUFBQUFBQUFJL0FBQUFBQUFBQUM4L01ad1JEUWFNR1JNL3M5Ni1jL3Bob3RvLmpwZyIsImdpdmVuX25hbWUiOiJaYWNoYXJ5IiwiZmFtaWx5X25hbWUiOiJBcnJhc21pdGgiLCJsb2NhbGUiOiJlbiJ9.BHIE2uwiEClnmYOVj0NVJiAqgT0OgFhKhJqVoBwPF3iiUyV98344mFlXyCIg2HS1IdF6ygXgnmJddDPXr1Ercp3aMiR9sEfhCZej5Rgjop08IusHo7olfWGs9UaLP8v15rjhHKd9xnAnSwVy8eoGd22SlAY90jnz9-_N6od2XWm6zHKmXphzcLxcH1JgqPmeE2ZemNUo-1fTcTlNqMiw3gAdktSrAv82DXjR3p17zLiGmGvOAMXtxPVxjfMnKPTrE-0v1w5V9g4j5qbRQJzFmc4aATF2qOQx-_gOVnKEMU53BfEz6vRNzKTiLI8kMwrfHCiLqRGXs_da7vIsJR7bGQ";
        JSONArray collectionArray = new JSONArray();
        try {
            for(int j = 0;  j < userCollections.size(); j++) {
                JSONArray booleansArray = new JSONArray();
                boolean[] elementCollected = new boolean[userCollections.get(j).getCollectionElements().size()];
                for (int i = 0; i < userCollections.get(j).getCollectionElements().size(); i++) {
                    elementCollected[i] = userCollections.get(j).getCollectionElements().get(i).isCollected();
                    booleansArray.put(elementCollected[i]);
                }
                JSONObject coll = new JSONObject();
                coll.put("cid", userCollections.get(j).getCID());
                coll.put("landmarks", booleansArray);
                collectionArray.put(coll);
            }

            JSONObject postToCMS = new JSONObject("{"
                    + "  \"id_token\": \"eyJhbGciOiJSUzI1NiIsImtpZCI6IjJiMmU0ZDZmMTgyMWFkNGQ3NmQ0NTUzYzk1MWI3NTYyMmFjYjY1MzkifQ.eyJhenAiOiIxMDUxNTQ5ODQzNDk4LXZrYXEzMWoyNWZhdWk2ajZ0cDZoY2NydWxya2x2bWR0LmFwcHMuZ29vZ2xldXNlcmNvbnRlbnQuY29tIiwiYXVkIjoiMTA1MTU0OTg0MzQ5OC12a2FxMzFqMjVmYXVpNmo2dHA2aGNjcnVscmtsdm1kdC5hcHBzLmdvb2dsZXVzZXJjb250ZW50LmNvbSIsInN1YiI6IjEwODk4NDgxMjY1NjYzODk1MDEwNiIsImVtYWlsIjoiemFjaGFyeWFycmFzbWl0aEBnbWFpbC5jb20iLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwiYXRfaGFzaCI6Ik9MTGdvTDJkOFZSdThIQlpSaDhYSFEiLCJpc3MiOiJhY2NvdW50cy5nb29nbGUuY29tIiwiaWF0IjoxNDk1MjQ1Nzk4LCJleHAiOjE0OTUyNDkzOTgsIm5hbWUiOiJaYWNoYXJ5IEFycmFzbWl0aCIsInBpY3R1cmUiOiJodHRwczovL2xoNC5nb29nbGV1c2VyY29udGVudC5jb20vLTR0ZUdZSmdqZTgwL0FBQUFBQUFBQUFJL0FBQUFBQUFBQUM4L01ad1JEUWFNR1JNL3M5Ni1jL3Bob3RvLmpwZyIsImdpdmVuX25hbWUiOiJaYWNoYXJ5IiwiZmFtaWx5X25hbWUiOiJBcnJhc21pdGgiLCJsb2NhbGUiOiJlbiJ9.BHIE2uwiEClnmYOVj0NVJiAqgT0OgFhKhJqVoBwPF3iiUyV98344mFlXyCIg2HS1IdF6ygXgnmJddDPXr1Ercp3aMiR9sEfhCZej5Rgjop08IusHo7olfWGs9UaLP8v15rjhHKd9xnAnSwVy8eoGd22SlAY90jnz9-_N6od2XWm6zHKmXphzcLxcH1JgqPmeE2ZemNUo-1fTcTlNqMiw3gAdktSrAv82DXjR3p17zLiGmGvOAMXtxPVxjfMnKPTrE-0v1w5V9g4j5qbRQJzFmc4aATF2qOQx-_gOVnKEMU53BfEz6vRNzKTiLI8kMwrfHCiLqRGXs_da7vIsJR7bGQ\","
                    + "  \"collections\": [{"
                    + "    \"cid\": 8,"
                    + "    \"landmarks\": ["
                    + "      false,"
                    + "            false,"
                    + "            false,"
                    + "            false,"
                    + "            false,"
                    + "            false,"
                    + "            false,"
                    + "            false"
                    + "    ]"
                    + "  }]"
                    + "}");
            Log.d("POSTTOCMSJSON", postToCMS.toString(1));
            HttpURLConnection post = (HttpURLConnection)new URL("http://magpiehunt.com/api/user/app/progress/push").openConnection();
            post.setRequestMethod("POST");
            post.setDoInput(false);
            post.setDoOutput(true);
            OutputStream writeJSON = post.getOutputStream();
            writeJSON.write(postToCMS.toString().getBytes("UTF-8"));
            Log.d("HTTPRESULT", post.getResponseMessage());
            post.disconnect();
            //TODO: Set up the connection to the CMS via endpoint provided.
        }
        catch (Exception e){
            Log.d("SENDPROGRESSEXCEP", e.getMessage());
        }
    }
}
