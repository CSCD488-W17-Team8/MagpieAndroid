package com.magpie.magpie.UserProgress;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.magpie.magpie.CollectionUtils.Collection;
import com.magpie.magpie.CollectionUtils.Element;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Zachary Arrasmith on 5/16/2017.
 */

public class SendProgress extends IntentService {

    public SendProgress(){super("SendProgress");}
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        ArrayList<Collection> userCollections = (ArrayList<Collection>)intent.getSerializableExtra("UserProgressCollection");
        String userID = intent.getStringExtra("UserID");
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

            JSONObject postToCMS = new JSONObject();
            postToCMS.put("token_id", userID);
            postToCMS.put("collections", collectionArray);
            //TODO: Set up the connection to the CMS via endpoint provided.
        }
        catch (Exception e){
            Log.d("SENDPROGRESSEXCEP", e.getMessage());
        }
    }
}
