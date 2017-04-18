package com.magpie.magpie.CollectionUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by sean on 4/3/17.
 */

public class Collection implements Serializable{

    private ArrayList<Element> mCollectionElements;
	private int mCID;
    private String mCity, mState, mRating, mDescription, mPicID, mName; //PicID is apparently used only within context of the database.
    private double mDistance;
    private boolean mOrdered;
    private int mElementTotal; //May not be needed. Putting this in here as CMS has it on the database.
    private int mCollected; //Thinking about the collection progress here.
    private boolean mSelected;
    private Bitmap img;

    public Collection() {
        mName = "";
        mCollectionElements = new ArrayList<>();
    }

    public Collection(String str) {
        mName = str;
    }

    public Collection(JSONObject json){
        try {
            mCID = json.getInt("CID");
            mCity = json.getString("City");
            mState = json.getString("State");
            mRating = json.getString("Rating");
            mDescription = json.getString("Description");
            mDistance = json.getDouble("CollectionLength");
            if(json.getInt("IsOrder") == 1) //0 is false, 1 is true
                mOrdered = true;
            else
                mOrdered = false;
            mPicID = json.getString("PicID");
            mName = json.getString("Name");
            mElementTotal = json.getInt("NumberOfLandmarks");
            mCollectionElements = new ArrayList<>();
        }
        catch(JSONException e){
            e.printStackTrace();
        }
    }

    public void addElement(JSONObject json){
        Element e = new Element(json);
        mCollectionElements.add(e);
    }
	
    public ArrayList<Element> getCollectionElements() {
        return mCollectionElements;
    }

    public int getCollectionSize() {
        return mCollectionElements.size();
    }

    public int getCID() {return mCID;}

    public String getCity(){return mCity;}

    public double getDistance() {return mDistance;}

    public String getDescription() {return mDescription;}

    public String getRating() {return mRating;}

    public String getPicID() {return mPicID;}

    public String getState() {return mState;}

    public boolean getOrdered(){return mOrdered;}

    public int getCollected(){return mCollected;}

    public void setCollected(Element e){
        if(!e.isCollected()){
            e.setCollected(true);
            mCollected++;
        }
    }

    public String getName() {
        return mName;
    }

    public void addBitmap(String url){
        try {
            img = BitmapFactory.decodeStream((InputStream) new URL(url).getContent());
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public Bitmap getImg(){return img;}

    public void setSelected(){
        if(mSelected){
            mSelected = false;
        }
        else
            mSelected = true;
    }
}
