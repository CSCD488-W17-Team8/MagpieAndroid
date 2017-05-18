package com.magpie.magpie;

import com.magpie.magpie.CollectionUtils.Collection;

/**
 * Created by sean on 5/15/17.
 */

public interface NavActivityListener {

    public void onActiveCollectionPassed(Collection col);
    public void onCollectionListPassed();
}
