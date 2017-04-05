package com.magpie.magpie.CollectionUtils;

import java.io.Serializable;

/**
 * Created by sean on 4/3/17.
 */

public class Element implements Serializable {

    private String mName;
    private double mLatitude;
    private double mLongitude;
    private boolean mCollected;
    // todo: image

    public Element(String name, double lat, double lon) {

        mName = name;
        mLatitude = lat;
        mLongitude = lon;
        mCollected = false;
    }

    public String getName() {
        return mName;
    }

    void setName(String mName) {
        this.mName = mName;
    }

    public double getLatitude() {
        return mLatitude;
    }

    void setLatitude(double mLatitude) {
        this.mLatitude = mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    void setLongitude(double mLongitude) {
        this.mLongitude = mLongitude;
    }

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
}
