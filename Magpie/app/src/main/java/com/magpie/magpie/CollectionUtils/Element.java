package com.magpie.magpie.CollectionUtils;

import android.graphics.Bitmap;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sean on 4/3/17.
 */

public class Element implements Serializable {

    private int mLID, mPicID, mCollID;
    private String mName, mQRCode, mCreator, mInfoLink, mDesc;
    private double mLatitude, mLongitude, mTime;
    private boolean mCollected; //Assuming no user database or Requires internet connection
    private Bitmap mBadge;
    private Bitmap mActualImage; //Assuming that there is a real world image associated with this.

    public Element(String name, double lat, double lon) {

        mName = name;
        mLatitude = lat;
        mLongitude = lon;
        mCollected = false;
    }

    public Element(String fromFile){
        String[] data = fromFile.split("÷");
        mName = data[0];
        mLID = Integer.parseInt(data[1]);
        mDesc = data[2];
        mPicID =  Integer.parseInt(data[3]);
        mCollID = Integer.parseInt(data[4]);
        mQRCode = data[5];
        mLatitude = Double.parseDouble(data[6]);
        mLongitude = Double.parseDouble(data[7]);
        mCreator = data[8];
        mInfoLink = data[9];
    }

    public Element(JSONObject json) {
        try{
            mLID = json.getInt("LID");
            mDesc = json.getString("Description");
            mPicID = json.getInt("PicID");
            mCollID = json.getInt("CollectionID");
            mQRCode = json.getString("QRCode");
            mName = json.getString("Name");
            mLatitude = json.getDouble("Latitude");
            mLongitude = json.getDouble("Longitude");
            mCreator = json.getString("Creator");
            mInfoLink = json.getString("InfoLink");

        }
        catch(JSONException e){
            e.printStackTrace();
        }
    }

    public int getLID(){return mLID;}

    public String getDesc(){return mDesc;}

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

    public void setCollected(boolean mCollected) {this.mCollected = mCollected;}

    public Bitmap getBadge(){return mBadge;}

    public void setBadge(Bitmap fromZIP){mBadge = fromZIP;}

    public String getCreator(){return mCreator;}

    public String getInfoLink(){return mInfoLink;}

    public double getTime(){return mTime;}

    public void setTime(double time){mTime = time;}

    @Override
    public String toString(){
        String ret = mName + "÷" + mLID + "÷" + mDesc + "÷" + mPicID + "÷" +
                mCollID + "÷" + mQRCode + "÷" + mLatitude + "÷" + mLongitude + "÷" + mCreator + "÷" + mInfoLink;
        return ret;
    }
    @Override
    public boolean equals(Object o) {

        if (o == null)
            return false;

        if (!Element.class.isAssignableFrom(o.getClass()))
            return false;

        final Element other = (Element) o;
        if (this.mName.equals(other.getName()))
            return false;

        return true;
    }
}

