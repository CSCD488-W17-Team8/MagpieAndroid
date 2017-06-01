package com.magpie.magpie.LocalList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.magpie.magpie.CollectionUtils.Collection;
import com.magpie.magpie.R;

import java.util.ArrayList;

/**
 * Created by Zachary Arrasmith on 5/11/2017.
 */

 class CustomExpandableListAdapterLocal extends BaseExpandableListAdapter {

    private ArrayList<Collection> localCollections;
    Context context;

     CustomExpandableListAdapterLocal(Local_loc local, ArrayList<Collection> fromLocal_loc){
        context = local.getContext();
        localCollections = fromLocal_loc;
    }
    @Override
    public int getGroupCount() {
        return localCollections.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return 1;
    }

    @Override
    public Object getGroup(int i) {
        return localCollections.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return localCollections.get(i);
    }

    @Override
    public long getGroupId(int i) {
        return 0;
    }

    @Override
    public long getChildId(int i, int i1) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View elvp = li.inflate(R.layout.local_list_display_parent, null);
        TextView acro = (TextView) elvp.findViewById(R.id.CollectionAcronym);
        TextView full = (TextView) elvp.findViewById(R.id.CollectionTitle);
        ProgressBar circularProgress = (ProgressBar) elvp.findViewById(R.id.CollectionProgress);
        Collection toBeAdded = localCollections.get(i);
        circularProgress.setMax(toBeAdded.getCollectionSize());
        circularProgress.setProgress(4);
        full.setText(toBeAdded.getName());
        acro.setText(localCollections.get(i).getAbbrev());
        return elvp;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View elvc = li.inflate(R.layout.local_list_display_child, null);
        TextView desc = (TextView) elvc.findViewById(R.id.CollectionDescription);
        TextView timeToComplete = (TextView) elvc.findViewById(R.id.TimeToComplete);
        TextView distance = (TextView) elvc.findViewById(R.id.DistanceValue);
        TextView badges = (TextView) elvc.findViewById(R.id.BadgesValue);
        TextView reward = (TextView) elvc.findViewById(R.id.RewardValue);
        TextView rating = (TextView) elvc.findViewById(R.id.RatingValue);
        desc.setText(localCollections.get(i).getDescription());
        timeToComplete.setText(localCollections.get(i).getHour() + " hrs. " + localCollections.get(i).getMinute() + " mins.");
        distance.setText(localCollections.get(i).getDistance() + " miles");
        badges.setText(localCollections.get(i).getCollectionSize() + " Badges");
        reward.setText("Nothing");
        rating.setText(localCollections.get(i).getRating());
        return elvc;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

}
