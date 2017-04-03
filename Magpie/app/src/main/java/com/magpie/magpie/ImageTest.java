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

/**
 * Created by Zachary on 3/6/2017.
 */

public class ImageTest extends IntentService {
    public ImageTest(){super("ImageTest");}
    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            Bitmap bm = BitmapFactory.decodeStream((InputStream) new URL("http://magpiehunt.com/image/logo/8").getContent());
            Intent i = new Intent("Image").putExtra("Image", bm);
            LocalBroadcastManager.getInstance(this).sendBroadcast(i);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
