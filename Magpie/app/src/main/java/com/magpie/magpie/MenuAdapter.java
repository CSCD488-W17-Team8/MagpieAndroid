package com.magpie.magpie;

import android.app.Activity;
import android.content.res.TypedArray;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Jacob on 5/1/2017.
 */

public class MenuAdapter extends ArrayAdapter
{

    LayoutInflater inflater;
    int[] image_id;
    String[] tools;
    Activity context;


    public MenuAdapter(Activity context, @LayoutRes int resource, @IdRes int textViewResourceId, @NonNull Object[] objects) {
        super(context, resource, textViewResourceId, objects);

        TypedArray typed_image = context.getResources().obtainTypedArray(R.array.Icon_list);
        this.image_id = new int[typed_image.length()];

        for(int i =  0; i < typed_image.length(); i++)
            image_id[i] = typed_image.getResourceId(i,0);
        typed_image.recycle();

        tools = (String[])objects;
        this.context = context;

    }



    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public Object getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {


        LayoutInflater inflater = this.context.getLayoutInflater();
        View row_view = inflater.inflate(R.layout.nav_list_row, null);

        TextView textView = (TextView) row_view.findViewById(R.id.menu_title);
        textView.setText(tools[position]);
        ImageView imagev = (ImageView) row_view.findViewById(R.id.menu_icon);
        imagev.setImageResource(image_id[position]);
        return row_view ;
    }
}
