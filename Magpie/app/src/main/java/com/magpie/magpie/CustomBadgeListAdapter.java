package com.magpie.magpie;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.magpie.magpie.CollectionUtils.Collection;
import com.magpie.magpie.CollectionUtils.Element;

import java.util.ArrayList;

/**
 * Created by Zachary Arrasmith on 5/1/2017.
 */

/*
 * Class CustomBadgeListAdapter: Handles the construction of both types of ListItems, normal and grid.
 * This class extends the BaseAdapter class in order to add it to the GridView and ListView objects in the BadgePage class.
 * The field 'type' is used in this case to determine which type of ListView is being constructed.
 *
 */

public class CustomBadgeListAdapter extends BaseAdapter {

    private ArrayList<Element> elements;
    private Context context;
    private String type;
    private static LayoutInflater inflater;

    public CustomBadgeListAdapter(BadgePage bp, ArrayList<Element> e, String t){
        context = bp.getContext();
        elements = e;
        type = t;
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
    public class Item{
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
            item.title.setText(elements.get(i).getName());
        }
        catch(Exception e){
            Log.d("SETEXTERROR", e.getMessage());
        }
        item.distance.setText("LID: " + elements.get(i).getLID());
        item.time.setText("Latitude: " + elements.get(i).getLatitude());
        item.badgeImage.setImageBitmap(elements.get(i).getBadge());
        return badgeListItem;
    }
}
