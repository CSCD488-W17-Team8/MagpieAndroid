package com.magpie.magpie.CollectionUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.Serializable;
import java.net.InterfaceAddress;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by sean on 4/3/17.
 */

public class Collection implements Serializable{

    private ArrayList<Element> mCollectionElements;
	private int mCID;
    //PicID is apparently used only within context of the database. We can use it to store the path to the zip file containing all the pictures related to the Collection.
    private String mCity, mState, mRating, mDescription, mPicZip, mName, mAbbrev;
    private double mDistance;
    private boolean mOrdered;
    private int mElementTotal; //May not be needed. Putting this in here as CMS has it on the database.
    private int mCollected; //Thinking about the collection progress here.
    private boolean mSelected, mDownloaded; //mDownloaded is an internal check to ensure that the associated zip file has been downloaded successfully.
    private Bitmap img;
    private int mHour, mMin, mSec, mZIPCode, mSelectedElement;

    public Collection() {
        mName = "";
        mCollectionElements = new ArrayList<>();
    }

    public Collection(String fromFile) {
        String [] elementSplit = fromFile.split("÷÷");
        String [] elements = elementSplit[17].split(",");
        mCollectionElements = new ArrayList<>();
        for(String e : elements){
            mCollectionElements.add(new Element(e));
        }
        mName = elementSplit[0];
        mCID = Integer.parseInt(elementSplit[1]);
        mCity = elementSplit[2];
        mState = elementSplit[3];
        mRating = elementSplit[4];
        mDescription = elementSplit[5];
        mDistance = Double.parseDouble(elementSplit[7]);
        mOrdered = Boolean.parseBoolean(elementSplit[8]);
        mElementTotal = Integer.parseInt(elementSplit[9]);
        mSelected = Boolean.parseBoolean(elementSplit[10]);
        mDownloaded = Boolean.parseBoolean(elementSplit[11]);
        mHour = Integer.parseInt(elementSplit[12]);
        mMin = Integer.parseInt(elementSplit[13]);
        mSec = Integer.parseInt(elementSplit[14]);
        mZIPCode = Integer.parseInt(elementSplit[15]);
        mAbbrev = elementSplit[15];
    }

    public Collection(JSONObject json){
        try {
            mCID = json.getInt("CID");
            mCity = json.getString("City");
            mState = json.getString("State");
            mRating = json.getString("Rating");
            mDescription = json.getString("Description");
            mAbbrev = json.getString("Abbreviation");
            String[] time = json.getString("TimeToComplete").split(":");
            mHour = Integer.parseInt(time[0]);
            mMin = Integer.parseInt(time[1]);
            mSec = Integer.parseInt(time[2]);
            mZIPCode = json.getInt("Zip Code");
            mDistance = json.getDouble("Distance");
            if(json.getInt("IsOrder") == 1) //0 is false, 1 is true
                mOrdered = true;
            else
                mOrdered = false;
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

    public int getElementTotal() {return mElementTotal;}

    public String getRating() {return mRating;}

    public String getPicZip() {return mPicZip;}

    public String getState() {return mState;}

    public boolean getOrdered(){return mOrdered;}

    public int getCollected(){return mCollected;}

    public int getHour(){return mHour;}

    public int getMinute(){return mMin;}

    public int getSecond(){return mSec;}

    public int getZIPCode(){return mZIPCode;}

    public String getAbbrev(){return mAbbrev;}

    public void setCollected(Element e){
        if(!e.isCollected()){
            e.setCollected(true);
            mCollected++;
        }
    }

    /**
     * Gets the name of the Collection
     * @return the name of the Colletion
     */
    public String getName() {
        return mName;
    }
    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;

        if (!Collection.class.isAssignableFrom(o.getClass()))
            return false;

        final Collection other = (Collection) o;
        if (this.mName.equals(other.getName()))
            return false;

        return true;
    }
    public void addBitmap(Bitmap bm){
        img = bm;
    }
    public Bitmap getImg(){return img;}

    public void setSelected(){
        if(mSelected){
            mSelected = false;
        }
        else
            mSelected = true;
    }

    public boolean getDownloaded(){return mDownloaded;}

    public void setDownloaded(){mDownloaded = true;}

    public void setPicZip(String path){mPicZip = path;}

    @Override
    public String toString(){
        String elementStr= "";
        for(Element e : mCollectionElements){
            elementStr += e.toString() + ",";
        }
        String fin = mName + "÷÷" + mCID + "÷÷" + mCity + "÷÷" + mState + "÷÷" + mRating
                + "÷÷" + mDescription + "÷÷" + mPicZip + "÷÷" + mDistance + "÷÷" + mOrdered
                + "÷÷" + mElementTotal + "÷÷" + mSelected + "÷÷" + mDownloaded + "÷÷" + mHour + "÷÷" + mMin + "÷÷"
                + mSec + "÷÷" + mZIPCode + "÷÷" + mAbbrev + "÷÷" + elementStr;
        return fin;
    }

    /**
     * Temporary method for generating a test Collection to be used when testing the Maps Activity's
     * ability to place markers from a collection. The Maps Activity passes the user's location to
     * collectionTestBuilder and the method will generate a list of tokens within a range of the
     * user.
     * @param lat user's latitude position.
     * @param lon user's longitude position.
     * @return a Collection to be used as a test case for the Maps Activity
     */
    public static Collection collectionTestBuilder(double lat, double lon) {

        Collection collection = new Collection("Test Collection");
        collection.buildTestElements(lat, lon);
        return collection;
    }

    private void buildTestElements(double lat, double lon) {

        mCollectionElements.add(new Element("test1", lat+0.001, lon+0.001));
        mCollectionElements.add(new Element("test2", lat-0.001, lon-0.001));
        mCollectionElements.add(new Element("test3", lat-0.001, lon+0.001));
        mCollectionElements.add(new Element("test4", lat+0.001, lon-0.001));
        mCollectionElements.add(new Element("test5", lat+0.002, lon+0.002));
    }

    public void setSelectedElement(int i){mSelectedElement = i;}

    public int getSelectedElement(){return mSelectedElement;}

}
