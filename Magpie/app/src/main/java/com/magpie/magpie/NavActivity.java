package com.magpie.magpie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.magpie.magpie.CollectionUtils.Collection;

import java.util.ArrayList;

public class NavActivity extends FragmentActivity {

    private ArrayList<Collection> mCollections;
    private Collection mActiveCollection;

    private String mBundleExtraKey = "";
    private String mActiveCollectionKey = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);
        Intent i = getIntent();

        mBundleExtraKey = getString(R.string.bundle_extra_key);
        mActiveCollectionKey = getString(R.string.active_collection_key);

        mCollections = new ArrayList<>();
        mActiveCollection = new Collection();

        if (i.hasExtra(mBundleExtraKey)) {

            Bundle b = i.getBundleExtra(mBundleExtraKey);

            if (b.containsKey(mActiveCollectionKey)) {
                mActiveCollection = (Collection) b.getSerializable(mActiveCollectionKey);
            }
        }

    }

    protected void startMapActivity(View v) {
        Intent i = new Intent(v.getContext(), MapsActivity.class);
        Bundle b = new Bundle();
        b.putSerializable(mActiveCollectionKey, mCollections);
        i.putExtra(mBundleExtraKey, mCollections);
        startActivity(i);
        finish();
    }

    public ArrayList<Collection> getCollections() {
        return mCollections;
    }

    public void setCollections(ArrayList<Collection> collections) {
        this.mCollections = collections;
    }

    public void addCollection(Collection collection) {
        this.mCollections.add(collection);
    }

    public void addNewCollections(ArrayList<Collection> newCollections) {
        this.mCollections.addAll(newCollections);
    }

    /*
    @Override
    public void onBackPressed() {



        switch (getSupportFragmentManager().getFragments().get(0).getId()) {

            case R.id.activity_local_loc:
                super.onBackPressed();
                break;

            case R.id.fragment_obtainable_loc:
                b.putSerializable("CurrentCollections", localCollections);
                Fragment fr = new Obtainable_loc();
                fr.setArguments(b);
                android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.Nav_Activity, fr);
                ft.commit();
                break;
        }
    }
    */
}
