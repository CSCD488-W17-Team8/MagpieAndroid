package com.magpie.magpie.CollectionUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by sean on 4/3/17.
 */

public class Element implements Serializable {

    private int mLID, mDescID, mPicID, mCollID;
    private String mName, mQRCode;
    private double mLatitude;
    private double mLongitude;
    private boolean mCollected;
    // todo: image

    public Element(String name, double lat, double lon) {

        mName = name;
        mLatitude = lat;
        mLongitude = lon;
    }

    public Element(JSONObject json) {
        try{
            mLID = json.getInt("LID");
            mDescID = json.getInt("DescID");
            mPicID = json.getInt("PicID");
            mCollID = json.getInt("CollectionID");
            mQRCode = json.getString("QRCode");
            mName = json.getString("Name");
            mLatitude = json.getDouble("Latitude");
            mLongitude = json.getDouble("Longitude");
        }
        catch(JSONException e){
            e.printStackTrace();
        }
    }

    public int getLID(){return mLID;}

    public int getDescID(){return mDescID;}

    public int getPicID(){return mPicID;}

    public int getCollectionID(){return mCollID;}

    public String getQRCode(){return mQRCode;}

    public String getName() {
        return mName;
    }

    void setName(String mName) {
        this.mName = mName;
    }

    public double getLatitude() {
        return mLatitude;
    }

    void setLatitude(double mLatitude) {this.mLatitude = mLatitude;}

    public double getLongitude() {
        return mLongitude;
    }

    void setLongitude(double mLongitude) {this.mLongitude = mLongitude;}

    public boolean isCollected() {
        return mCollected;
    }

    void setCollected(boolean mCollected) {this.mCollected = mCollected;}
}