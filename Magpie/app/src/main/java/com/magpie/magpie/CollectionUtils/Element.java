package com.magpie.magpie.CollectionUtils;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sean on 4/3/17.
 */

public class Element implements Serializable {

    private int mLID, mDescID, mPicID, mCollID;
    private String mName, mQRCode;
    private double mLatitude;
    private double mLongitude;
    private boolean mCollected; //Assuming no user database or Requires internet connection
    // todo: image

    public Element(String name, double lat, double lon) {

        mName = name;
        mLatitude = lat;
        mLongitude = lon;
        mCollected = false;
    }

    public Element(String fromFile){
        String[] data = fromFile.split("%");
        mName = data[0];
        mLID = Integer.parseInt(data[1]);
        mDescID = Integer.parseInt(data[2]);
        mPicID =  Integer.parseInt(data[3]);
        mCollID = Integer.parseInt(data[4]);
        mQRCode = data[5];
        mLatitude = Double.parseDouble(data[6]);
        mLongitude = Double.parseDouble(data[7]);
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

    void setCollected(boolean mCollected) {
        this.mCollected = mCollected;
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

    @Override
    public String toString(){
        String ret = mName + "%" + mLID + "%" + mDescID + "%" + mPicID + "%" +
                mCollID + "%" + mQRCode + "%" + mLatitude + "%" + mLongitude;
        return ret;
    }
}

