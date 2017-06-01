package com.magpie.magpie.BadgePage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.magpie.magpie.CollectionUtils.Element;
import com.magpie.magpie.NavActivity;
import com.magpie.magpie.R;
import com.magpie.magpie.Services.TrackGPS;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by Zachary Arrasmith on 5/1/2017.
 */

/*
 * Class CustomBadgeListAdapter: Handles the construction of both types of ListItems, normal and grid.
 * This class extends the BaseAdapter class in order to add it to the GridView and ListView objects in the BadgePage class.
 * The field 'type' is used in this case to determine which type of ListView is being constructed.
 *
 */

 class CustomBadgeListAdapter extends BaseAdapter {

    private final double WALKING_RATE = 272.8;
    private final double FOOT_CONVERSION = 3.28;

    private ArrayList<Element> elements;
    private Context context;
    private String type;
    private TrackGPS user_location;
    private Location user;
    private float dist;
    private static LayoutInflater inflater;
    private NavActivity navActivity;

     CustomBadgeListAdapter(BadgePage bp, ArrayList<Element> e, String t, ZipFile picZIP, NavActivity nav){
        context = bp.getContext();
        elements = e;
        type = t;
        user_location = new TrackGPS(bp.getContext());
        navActivity = nav;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {return elements.size();}

    @Override
    public Object getItem(int i) {return null;}

    @Override
    public long getItemId(int i) {return 0;}

    /*
     * Class Item: This class defines the views that are associated with the Badge Page.
     *
     * ImageView badgeImage: The image associated with a particular badge. This image will be obtained via a zip file.
     * TextView title: The name of the badge.
     * TextView distance: The Euclidean distance from the user to the badge's physical location. Measured in feet[could be miles]
     * TextView time: The time it would take for the user to reach the badge's physical location. Measured in feet per minute.
     *                This is calculated using the average walk speed of a human combined with the distance measurement.
     *
     *
     */
    private class Item{
        ImageView badgeImage;
        TextView title;
        TextView distance;
        TextView time;
    }

    /*
     * Method getView: Creates the ListItem that will be used in the BadgePage's ListView. Handled here is the variation of list visual.
     *
     */

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Item item = new Item();
        View badgeListItem;
        /*
         * The below if structure handles the particular ListView type being created.
         */
        if(type.compareTo("List") == 0) {
            badgeListItem = inflater.inflate(R.layout.badge_list_display, null);
            item.badgeImage = (ImageView) badgeListItem.findViewById(R.id.BadgeListImg);
            item.title = (TextView) badgeListItem.findViewById(R.id.BadgeListTitle);
            item.time = (TextView) badgeListItem.findViewById(R.id.BadgeListTime);
            item.distance = (TextView) badgeListItem.findViewById(R.id.BadgeListDistance);
        }
        else {
            badgeListItem = inflater.inflate(R.layout.badge_grid_display, null);
            item.badgeImage = (ImageView) badgeListItem.findViewById(R.id.GridImage);
            item.title = (TextView) badgeListItem.findViewById(R.id.GridTitle);
            item.time = (TextView) badgeListItem.findViewById(R.id.GridTime);
            item.distance = (TextView) badgeListItem.findViewById(R.id.GridDistance);
        }
        try {
            item.title.setText(navActivity.getActiveCollection().getCollectionElements().get(i).getName());
        }
        catch(Exception e){
            Log.d("SETEXTERROR", e.getMessage());
        }

        item.distance.setText("Distance: " + determineDistance(i));
        navActivity.getActiveCollection().getCollectionElements().get(i).setTime(determineTime());
        item.time.setText("Time: " + navActivity.getActiveCollection().getCollectionElements().get(i).getTime());
        if(navActivity.getActiveCollection().getCollectionElements().get(i).getBadge() != null) {
            item.badgeImage.setImageBitmap(navActivity.getActiveCollection().getCollectionElements().get(i).getBadge());
        }
        else{
            item.badgeImage.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), android.R.drawable.picture_frame));
        }
        return badgeListItem;
    }

    private int determineDistance(int i){
        Location bloc = new Location("badge_location");
        bloc.setLatitude(navActivity.getActiveCollection().getCollectionElements().get(i).getLatitude());
        bloc.setLongitude(navActivity.getActiveCollection().getCollectionElements().get(i).getLongitude());
        user = new Location("USERLOC");
        if(user_location.canGetLocation()) {
            user.setLatitude(user_location.getLatitude());
            user.setLongitude(user_location.getLongitude());
            dist = (float) ((bloc.distanceTo(user)) * FOOT_CONVERSION);
        }
        return (int)dist;
    }

    private double determineTime(){
        double time = dist / WALKING_RATE;

        return (int)time;
    }
}
