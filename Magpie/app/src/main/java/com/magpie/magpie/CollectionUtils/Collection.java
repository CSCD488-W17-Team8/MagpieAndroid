package com.magpie.magpie.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by sean on 4/3/17.
 */

public class Collection implements Serializable{

    private String mName;
    private ArrayList<Element> mCollectionElements;

    public Collection() {
        mName = "";
        mCollectionElements = new ArrayList<>();
    }

    public Collection(String str) {
        mName = str;
    }

    public ArrayList<Element> getCollectionElements() {
        return mCollectionElements;
    }

    public int getCollectionSize() {
        return mCollectionElements.size();
    }

    public String getName() {
        return mName;
    }
}
