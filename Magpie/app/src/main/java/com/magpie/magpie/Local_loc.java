package com.magpie.magpie;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.content.LocalBroadcastManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

import com.magpie.magpie.CollectionUtils.*;

public class Local_loc extends FragmentActivity implements View.OnClickListener, BadgePage.OnFragmentInteractionListener{
    String all = "";
    Button b;
    File jsonFile;
    FileOutputStream fos;
    ArrayList<Collection> collections;
    FloatingActionButton obtain;
    ListView lv;
    ArrayAdapter<String> localAdapter;
    ArrayList<String> addToList;
    ArrayList<Bitmap> images;
    CustomListAdapter cla;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_local_loc);
            Intent intent = this.getIntent();
            obtain = (FloatingActionButton) findViewById(R.id.ToObtainable);
            addToList = new ArrayList<>();
            lv = (ListView) findViewById(R.id.list);
            localAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, addToList);
            obtain.setOnClickListener(this);
            if (intent.getExtras() != null) {
                Bundle b = intent.getExtras();
                collections = (ArrayList<Collection>) b.getSerializable("CollectionList");
                lv.setAdapter(localAdapter);
                //cla = new CustomListAdapter(this, collections);
                //lv.setAdapter(cla);
                fillTable();
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        obtain.hide();
                        Collection send = collections.get(i);
                        send.setSelected();
                        Bundle coll = new Bundle();
                        coll.putSerializable("TheCollection", send);
                        Fragment fr = new BadgePage();
                        fr.setArguments(coll);
                        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
                        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
                        ft.replace(R.id.activity_local_loc, fr);
                        ft.commit();
                    }
                });
            }
        }
        catch(Exception e){
            Log.e("Local: ", "exception", e);
        }
    }

    public void fillTable(){
        String visual;
        for(Collection c : collections){
            visual = c.getName() + "\r\n Number of Landmarks: " + c.getCollected() + " / " + c.getCollectionSize() +
                    "\r\n" + c.getDescription() + " (" + c.getRating() + ")";
            addToList.add(visual);
            localAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.ToObtainable) {
            try {
                Intent obtain = new Intent(view.getContext(), Obtainable_loc.class);
                startActivity(obtain);
            } catch (Exception e){
                Log.d("Stuff", "Error: ", e);
            }
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}