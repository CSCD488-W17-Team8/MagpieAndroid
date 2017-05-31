package com.magpie.magpie.Services;

import android.Manifest;
import android.app.DownloadManager;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;

import java.util.ArrayList;

/**
 * Created by Zachary Arrasmith on 4/26/2017.
 */

/*
 * Class ZIPDownload: This class handles the downloading of any zip file needed to properly display images in the rest of the app.
 * This class extends the IntentService class in order to properly run the downloads in the background.
 * There will be a looping of downloads for this class to queue up, determined by the passed in CID values.
 * NOTE: It is expected that zip downloads are to be handled in the Local_loc class, but are only started upon return from the
 * Obtainable_loc class. This is to prevent a re-download of the same zip files that already exist on the system.
 *
 */

public class ZIPDownload extends IntentService {
    long uriID;
    public ZIPDownload(){super("ZIPDownload");}
    @Override
    protected void onHandleIntent(Intent intent) {
        ArrayList<Integer> CIDs = intent.getIntegerArrayListExtra("TheCIDs");
        DownloadManager dm = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        for(Integer i : CIDs){
            DownloadManager.Request zipReq = new DownloadManager.Request(Uri.parse("http://magpiehunt.com/images/collection/" + i));
            zipReq.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "imagesCID" + i.toString() + ".zip");
            uriID = dm.enqueue(zipReq);
        }
        Intent i = new Intent("ImageZIP").putExtra("URI", uriID);
        LocalBroadcastManager.getInstance(this).sendBroadcast(i);
    }
}
