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

    /**
     * @param element the element to be added to the Collection's ArrayList of Elements.
     * @return true if add was successful, or false if element was null or already exists in the
     * collection.
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

    public ArrayList<Element> getCollectionElements() {
        return mCollectionElements;
    }

    public int getCollectionSize() {
        return mCollectionElements.size();
    }

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

    public Collection CollectionTestBuilder() {
        Collection collection = new Collection("Test Collection");

        collection.addElement(new Element("test1", 0.0, 0.0));
        collection.addElement(new Element("test2", 0.1, 0.1));
        collection.addElement(new Element("test3", 0.2, 0.2));
        collection.addElement(new Element("test4", 0.3, 0.1));
        collection.addElement(new Element("test5", 0.1, 0.2));
        collection.addElement(new Element("test6", 0.1, 0.3));

        return collection;
    }

    /**
     * Potentially where updating the collected status of each Element could take place
     */
    public void updateFromUserProgress() {
        // TODO:
    }
}
