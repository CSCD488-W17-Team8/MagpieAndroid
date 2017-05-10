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
import android.view.LayoutInflater;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.magpie.magpie.CollectionUtils.*;

public class Local_loc extends Fragment implements View.OnClickListener{
    FloatingActionButton save;
    FileOutputStream fos;
    ArrayList<Collection> collections;
    FloatingActionButton obtain;
    ListView lv;
    ArrayAdapter<String> localAdapter;
    ArrayList<String> addToList;
    ArrayList<Bitmap> images;
    CustomListAdapter cla;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            Bundle b = getArguments();
            //collections = (ArrayList<Collection>) b.getSerializable("CollectionList");

            // BEGIN: ADDED BY SEAN 5/2/2017
            ((NavActivity)getActivity()).setCollections((ArrayList<Collection>) b.getSerializable("CollectionList"));
            // END: ADDED BY SEAN 5/2/2017
        }

        // BEGIN: ADDED BY SEAN 5/2/2017

        // END: ADDED BY SEAN 5/2/2017
    }

    public void fillTable(){
        String visual;
        for(Collection co : collections){
            visual = co.getName() + "\r\n Number of Landmarks: " + co.getCollected() + " / " + co.getCollectionSize() +
                    "\r\n" + co.getDescription() + " (" + co.getRating() + ")";
            addToList.add(visual);
            localAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.ToObtainable) {
            try {
                obtain.hide();
                save.hide();
                Bundle b = new Bundle();
                b.putSerializable("CurrentCollections", collections);
                Fragment fr = new Obtainable_loc();
                fr.setArguments(b);
                android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.Nav_Activity, fr);
                ft.commit();
            } catch (Exception e){
                Log.d("Stuff", "Error: ", e);
            }
        }
        else if(view.getId() == R.id.saveButton){
            File f = new File(getActivity().getFilesDir(), "SavedCollections.txt");
            try {
                if (f.exists()) {
                    fos = getActivity().openFileOutput("SavedCollections.txt", Context.MODE_PRIVATE);
                    for(Collection c : collections){
                        fos.write(c.toString().getBytes());
                        fos.write("%%%".getBytes());
                    }
                    fos.close();
                    Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
                } else {
                    f.createNewFile();
                    fos = getActivity().openFileOutput("SavedCollections.txt", Context.MODE_PRIVATE);
                    fos.write("".getBytes());
                    for(Collection c : collections){
                        fos.write(c.toString().getBytes());
                    }
                    fos.close();
                    Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
                }
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    private void readFile(){
        File f = new File(getActivity().getFilesDir(), "SavedCollections.txt");
        try {
            if (f.exists() && f.length() != 0) {
                InputStream fin = getActivity().openFileInput("SavedCollections.txt");
                InputStreamReader inRead = new InputStreamReader(fin);
                BufferedReader bufRead = new BufferedReader(inRead);
                String s = bufRead.readLine();
                String[] allColl = s.split("%%%");
                for(String coll : allColl){
                    collections.add(new Collection(coll));
                }
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
            View v = inflater.inflate(R.layout.activity_local_loc, container, false);
            obtain = (FloatingActionButton) v.findViewById(R.id.ToObtainable);
            addToList = new ArrayList<>();
            lv = (ListView) v.findViewById(R.id.list);
            localAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, addToList);
            lv.setAdapter(localAdapter);
            obtain.setOnClickListener(this);
            save = (FloatingActionButton) v.findViewById(R.id.saveButton);
            save.setOnClickListener(this);
            if(collections == null){
                collections = new ArrayList<>();
            }
            readFile();
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    try {
                        obtain.hide();
                        save.hide();
                        Collection send = collections.get(i);
                        send.setSelected();
                        Bundle coll = new Bundle();
                        coll.putSerializable("TheCollection", send);
                        Fragment fr = new BadgePage();
                        fr.setArguments(coll);
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, fr).commit();
                    }
                    catch(Exception e){
                        Log.d("Error: ", e.getMessage());
                    }
                }
            });
            if (collections != null) {
                //cla = new CustomListAdapter(this, collections);
                //lv.setAdapter(cla);
                fillTable();
            }
        return v;
    }
}