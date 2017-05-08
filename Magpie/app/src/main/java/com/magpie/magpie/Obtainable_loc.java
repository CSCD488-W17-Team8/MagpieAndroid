package com.magpie.magpie;

import android.Manifest;
import android.app.ActionBar;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.renderscript.ScriptGroup;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import com.magpie.magpie.CollectionUtils.*;

public class Obtainable_loc extends Fragment implements View.OnClickListener{
    ArrayList<Collection> collection;
    ArrayList<Collection> added;
    ArrayList<Collection> fromFile;
    Bundle collBundle;
    Button sendAll;
    Button cancel;
    ImageView iv;
    String collectionsToElements;
    String strBadges;
    Bitmap bm;
    JSONArray cmsColl;
    ListView collectionDisplay;
    ArrayAdapter<String> obtainable_loc_Adapter;
    ArrayList<String> allColl;

    /*
     * Method onCreate: Handles the sending in of the Collections that are on the local side.
     * If there is no Collections locally, it sends in null.
     *
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            Bundle b = getArguments();
            fromFile = (ArrayList<Collection>)b.getSerializable("CurrentCollections");
        }
    }

    /*
     * Method parseJSON: Creates a display of the pulled JSON data for the user to make a decision.
     * More specifically, it creates the String visual of pertinent data, and the adapter is called to notify of a change.
     *
     */

    private void parseJSON() {
        try {
            Collection c;
            JSONObject j;
            for (int i = 0; i < cmsColl.length(); i++) {
                j = cmsColl.getJSONObject(i);
                if (j.getInt("Status") == 1 && checkCID(j.getInt("CID"))) { //(0 is false, 1 is true) && (ensuring that a collection isn't already on the system).
                    c = new Collection(j);
                    c.addBitmap(bm);
                    collection.add(c);
                    String fin = j.getString("CID") + " : " + j.getString("Name") + "\r\n";
                    fin += j.getString("Description") + " (" + j.getString("Rating") + ")";
                    allColl.add(fin);
                    obtainable_loc_Adapter.notifyDataSetChanged();

                }
            }
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /*
     * Method checkCID: Handles the checking of the JSON data from the CMS against the passed in Collections from the local device.
     * If there are no Collections locally, this method will not be called.
     *
     */

    private boolean checkCID(int CID) {
        if (fromFile.size() != 0) {
            for (Collection fileColl : fromFile){
                if(fileColl.getCID() == CID){
                    return false;
                }
            }
        }
        return true;
    }

    /*
     * BroadcastReceiver br: Accepts the JSON data from the initial pull from the CMS.
     * It creates a JSONArray from the String passed and it is sent to the method parseJSON.
     *
     */

    private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        if(intent.hasExtra("JSONFromCMS")) {
            try {
                cmsColl = new JSONArray(intent.getStringExtra("JSONFromCMS"));
                parseJSON();
            }
            catch(JSONException e){
                e.printStackTrace();
            }
        /*Intent parseIntent = new Intent(Obtainable_loc.this, JSONParse.class);
        parseIntent.putExtra("JSON", json);
        Obtainable_loc.this.startService(parseIntent);*/

        }
    }};

    /*
     * BroadcastReceiver ebr: Handles the obtaining of all pertinent text data of all selected Collections.
     * Each separate Collection's badges are divided by a "%", which is then used to create the Elements associated with the Collection.
     *
     */

    private BroadcastReceiver ebr = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            strBadges = intent.getStringExtra("CollectionElements");
            String[] badgeArr = strBadges.split("%");
            createElements(badgeArr);
            Fragment fr = new Local_loc();
            Bundle coll = new Bundle();
            if (added.size() != 0) {
                coll.putSerializable("NewlyAddedCollections", added);
            }
            fr.setArguments(coll);
            android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.Main_Activity, fr);
            ft.commit();
        }
    };

    /*
     * Method onClick: Handles the separate button clicks that can happen --
     * Cancel: Returns the user to the local list
     * Apply, adds all selected Collections to the local side.
     *
     */

    @Override
    public void onClick(View view) {
        if(view.getId() == cancel.getId()){
            Fragment fr = new Local_loc();
            android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.Main_Activity, fr);
            ft.commit();
        }
        else if(((String)(view.getTag())).compareTo("Apply") == 0) {
            sendAll.setEnabled(false);
            if (added.size() != 0) {
                for (Collection i : added) {
                    collectionsToElements += i.getCID() + ",";
                }
                Intent collIntent = new Intent(getContext(), JSONElements.class);
                collIntent.putExtra("SelectedCollectionCIDs", collectionsToElements);
                getContext().startService(collIntent);
            }
        }
    }

    /*
     * Method createElements: Creates a JSONArray from all of the passed in Strings and creates the Elements.
     * Each element is sent to the ArrayList in collection, where it is added to the ArrayList for the associated Collection.
     *
     */

    private void createElements(String[] badgeArr) {
        try {
            for(int k = 0; k < badgeArr.length; k++) {
                JSONArray ja = new JSONArray(badgeArr[k]);
                for (int j = 0; j < added.size(); j++) {
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject json = ja.getJSONObject(i);
                        if (json.getInt("CollectionID") == added.get(j).getCID()) {
                            added.get(j).addElement(json);
                        }
                    }
                }
            }
        }
        catch(JSONException e){
            e.printStackTrace();
        }
    }

    /*
     * Method onCreateView: Creates the visual for the user to interact with. Mostly handled here is the instantiation of variables.
     *
     */

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int writePermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        if(writePermission == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        if(readPermission == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
        View v = inflater.inflate(R.layout.obtainable_loc, container, false);
        super.onCreate(savedInstanceState);
        Intent getIntent = new Intent(getContext(), JSONGet.class);
        getContext().startService(getIntent);
        Intent intent = getActivity().getIntent();
        added = new ArrayList<>();
        if (intent.hasExtra("LocalCollections")) {
            fromFile = (ArrayList<Collection>) intent.getSerializableExtra("Collections");
        }
        collectionDisplay = (ListView) v.findViewById(R.id.listView);
        sendAll = (Button) v.findViewById(R.id.button3);
        sendAll.setEnabled(false);
        cancel = (Button) v.findViewById(R.id.Return);
        iv = (ImageView) v.findViewById(R.id.FromZip);
        LocalBroadcastManager.getInstance(v.getContext()).registerReceiver(br, new IntentFilter("FromCMS"));
        LocalBroadcastManager.getInstance(v.getContext()).registerReceiver(ebr, new IntentFilter("Elements"));
        //LocalBroadcastManager.getInstance(v.getContext()).registerReceiver(zbr, new IntentFilter("ImageZIP"));
        /*try {
            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
            String file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).list()[5];
            ZipFile zipImages = new ZipFile(path + "/" + file);
            ZipEntry firstImage = zipImages.entries().nextElement();
            InputStream zin = zipImages.getInputStream(firstImage);
            bm = BitmapFactory.decodeStream(zin);
        }
        catch(Exception e){
            Log.d("IOException", e.getMessage());
        }*/
        sendAll.setOnClickListener(this);
        cancel.setOnClickListener(this);
        collection = new ArrayList<>();
        collectionsToElements = "";
        collBundle = new Bundle();
        sendAll.setTag("Apply");
        allColl = new ArrayList<>();
        obtainable_loc_Adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, allColl)//{
        /*@Override
        public View getView(int position, View convertView, ViewGroup parent){
            Vi
        }
    }*/;
        collectionDisplay.setAdapter(obtainable_loc_Adapter);
        collectionDisplay.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (added.size() == 0) {
                    Collection c = collection.get(i);
                    added.add(c);
                    view.setBackgroundColor(Color.GREEN);
                    sendAll.setEnabled(true);
                } else if (added.contains(collection.get(i))) {
                    added.remove(collection.get(i));
                    if(added.size() == 0)
                        sendAll.setEnabled(false);
                    view.setBackgroundColor(Color.WHITE);
                }
                else{
                    added.add(collection.get(i));
                    view.setBackgroundColor(Color.GREEN);
                }
            }
        });
        return v;
    }
}