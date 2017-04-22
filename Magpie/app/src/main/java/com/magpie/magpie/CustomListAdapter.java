package com.magpie.magpie;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import com.magpie.magpie.CollectionUtils.Collection;

import org.w3c.dom.Text;

/**
 * Created by Zachary Arrasmith on 4/17/2017.
 */

public class CustomListAdapter extends BaseAdapter {

    private ArrayList<Collection> collections;
    private Context context;
    private static LayoutInflater inflater;

    public CustomListAdapter(Local_loc local, ArrayList<Collection> cs){
        collections = cs;
        context = local;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return collections.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public class Item{
        ImageView iv;
        TextView tv;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Item item = new Item();
        View listItem = inflater.inflate(R.layout.local_collection_display, null);
        item.iv = (ImageView) listItem.findViewById(R.id.imageViewLocal);
        item.tv = (TextView) listItem.findViewById(R.id.textViewLocal);
        item.tv.setText(collections.get(i).getName());
        try{
            item.iv.setImageBitmap(collections.get(i).getImg());
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
