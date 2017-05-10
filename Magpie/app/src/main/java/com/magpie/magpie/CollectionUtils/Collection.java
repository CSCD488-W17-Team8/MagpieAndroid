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
        
    public Collection(String fromFile) {
        String [] elementSplit = fromFile.split("%%");
        String [] elements = elementSplit[11].split(",");
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
        mPicID = elementSplit[6];
        mDistance = Double.parseDouble(elementSplit[7]);
        mOrdered = Boolean.parseBoolean(elementSplit[8]);
        mElementTotal = Integer.parseInt(elementSplit[9]);
        mSelected = Boolean.parseBoolean(elementSplit[10]);
    }

    public Collection(JSONObject json){
        try {
            mCID = json.getInt("CID");
            mCity = json.getString("City");
            mState = json.getString("State");
            mRating = json.getString("Rating");
            mDescription = json.getString("Description");
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

    public Collection(boolean isTest) {
        mName = "Test Collection";
        mCollectionElements = new ArrayList<>();
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
        
        Collection collection = new Collection(true);
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
    
    /**
     * Potentially where updating the collected status of each Element could take place
     */
    public void updateFromUserProgress() {
        // TODO: maybe here? Maybe not?
    }

    @Override
    public String toString(){
        String elementStr= "";
        for(Element e : mCollectionElements){
            elementStr += e.toString() + ",";
        }
        String fin = mName + "%%" + mCID + "%%" + mCity + "%%" + mState + "%%" + mRating
                + "%%" + mDescription + "%%" + mPicID + "%%" + mDistance + "%%" + mOrdered
                + "%%" + mElementTotal + "%%" + mSelected + "%%" + elementStr;
        return fin;
    }
}
