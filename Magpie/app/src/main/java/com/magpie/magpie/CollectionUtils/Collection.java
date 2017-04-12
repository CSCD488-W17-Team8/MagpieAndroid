package com.magpie.magpie.CollectionUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
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
    private int collected; //Thinking about the collection progress here.

    public Collection() {
        mName = "";
        mCollectionElements = new ArrayList<>();
    }

    public Collection(String str) {
        mName = str;
    }

    /**
     * Added by Sean.
     *
     * This method might be temporary
     * // TODO: evaluate necessity
     */
    public boolean addElement(Element element) {
        if (element != null) {
            if (!mCollectionElements.contains(element)) {
                mCollectionElements.add(element);
                return true;
            }
        }
        return false;
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

    public static Collection collectionTestBuilder(double lat, double lon) {

        Collection collection = new Collection("Test Collection");
        collection.buildTestElements(lat, lon);
        return collection;
    }

    private void buildTestElements(double lat, double lon) {

        mCollectionElements.add(new Element("test1", lat+0.1, lon+0.1));
        mCollectionElements.add(new Element("test2", lat-0.1, lon-0.1));
        mCollectionElements.add(new Element("test3", lat-0.1, lon+0.1));
        mCollectionElements.add(new Element("test4", lat+0.1, lon-0.1));
        mCollectionElements.add(new Element("test5", lat+0.2, lon+0.2));
    }

    /**
     * Potentially where updating the collected status of each Element could take place
     */
    public void updateFromUserProgress() {
        // TODO:
    }
}
