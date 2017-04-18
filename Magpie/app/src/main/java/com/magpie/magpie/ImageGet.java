package com.magpie.magpie;

import android.app.IntentService;
import android.content.Intent;
import android.content.Loader;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.content.LocalBroadcastManager;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Zachary on 3/6/2017.
 */

public class ImageGet extends IntentService {
    public ImageGet(){super("ImageGet");}
    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            String[] splitIDs = intent.getStringExtra("Collections").split(",");
            ArrayList<String> collIDs = new ArrayList<>();
            ArrayList<Bitmap> collImages = new ArrayList<>();
            for(int i = 0; i < splitIDs.length; i++){
                collIDs.add(splitIDs[i]);
            }
            for(String id : collIDs) {
                Bitmap bm = BitmapFactory.decodeStream((InputStream) new URL("http://magpiehunt.com/image/logo/" + id).getContent());
                collImages.add(bm);
            }
            Intent imageIntent = new Intent("ImageBack").putExtra("Bitmaps", collImages);
            LocalBroadcastManager.getInstance(this).sendBroadcast(imageIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
