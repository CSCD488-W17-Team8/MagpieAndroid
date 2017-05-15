package com.magpie.magpie;

import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.magpie.magpie.CollectionUtils.Collection;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Zachary Arrasmith on 5/12/2017.
 */

public class CustomExpandableListAdapterObtainable extends BaseExpandableListAdapter {

    private ArrayList<Collection> jsonCollections;
    private ArrayList<Collection> display;
    private Context context;
    private int width;

    public CustomExpandableListAdapterObtainable(Obtainable_loc obtainable_loc, ArrayList<Collection> fromJSON, int width){
        context = obtainable_loc.getContext();
        jsonCollections = fromJSON;
        display = fromJSON;
        this.width = width;
    }

    @Override
    public int getGroupCount() {
        return display.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return 1;
    }

    @Override
    public Object getGroup(int i) {
        return display.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return display.get(i);
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
        View elvp = li.inflate(R.layout.obtainable_list_display_parent, null);
        TextView title = (TextView) elvp.findViewById(R.id.CollectionTitle);
        TextView acronym = (TextView) elvp.findViewById(R.id.CollectionAcronym);
        title.setText(display.get(i).getName());
        acronym.setText(createAcronym(display.get(i).getName()));
        return elvp;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View elvc = li.inflate(R.layout.obtainable_list_display_child, null);
        TextView desc = (TextView) elvc.findViewById(R.id.CollectionDescription);
        TextView badges = (TextView) elvc.findViewById(R.id.BadgesText);
        TextView rating = (TextView) elvc.findViewById(R.id.RatingText);
        badges.setText(display.get(i).getElementTotal() + " Badges");
        rating.setText("Rating: " + display.get(i).getRating());
        badges.setWidth(width/2);
        desc.setText(display.get(i).getDescription());
        return elvc;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    private String createAcronym(String name) {
        StringBuilder acronymConstructor = new StringBuilder();
        String[] words = name.split(" ");
        if(words.length == 1){
            if(words[0].length() != 0){
                for(int i = 0; i < 3; i++) {
                    acronymConstructor.append(words[0].charAt(i));
                }
            }
        }
        else if(words.length == 2){
            for(int i = 0; i < 2; i++) {
                acronymConstructor.append(words[0].charAt(i));
            }
            acronymConstructor.append(words[1].charAt(0));
        }
        else{
            for(int i = 0; i < words.length; i++){
                acronymConstructor.append(words[i].charAt(0));
            }
        }
        return acronymConstructor.toString();
    }

    public void filterData(String cityOrState){
        ArrayList<Collection> mediator = new ArrayList<>();
        display.clear();
        if(!cityOrState.isEmpty()){
            String toFilter = cityOrState.toLowerCase();
            for(Collection c : jsonCollections){
                if(c.getState().toLowerCase().contains(toFilter) || c.getCity().toLowerCase().contains(toFilter)){
                    mediator.add(c);
                }
            }
            if(mediator.size() > 0){
                display.addAll(mediator);
            }
        }
        else{
            display.addAll(jsonCollections);
        }
        notifyDataSetChanged();
    }
}
