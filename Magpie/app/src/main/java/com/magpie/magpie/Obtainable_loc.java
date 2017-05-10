package com.magpie.magpie;

import android.app.ActionBar;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.Fragment;
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

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import com.magpie.magpie.CollectionUtils.*;

public class Obtainable_loc extends Fragment implements View.OnClickListener{
    ArrayList<String> collections;
    ArrayList<Collection> collection;
    ArrayList<Collection> added;
    ArrayList<Collection> fromFile;
    Bundle collBundle;
    Button b;
    Button sendAll;
    Button cancel;
    ImageView iv;
    String json;
    String collect;
    String badges;
    Bitmap bm;
    JSONArray ja;
    ListView lv;
    ArrayAdapter<String> obtainAdapter;
    ArrayList<String> allColl;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            Bundle b = getArguments();
            fromFile = (ArrayList<Collection>)b.getSerializable("CurrentCollections");
        }
    }
    private void parseJSON() {
        try {
            Collection c;
            JSONObject j;
            for (int i = 0; i < ja.length(); i++) {
                j = ja.getJSONObject(i);
                if (j.getInt("Status") == 1 && checkCID(j.getInt("CID"))) { //(0 is false, 1 is true) && (ensuring that a collection isn't already on the system).
                    c = new Collection(j);
                    c.addBitmap("http://magpiehunt.com/image/logo/" + c.getCID());
                    collection.add(c);
                    String fin = j.getString("CID") + " : " + j.getString("Name") + "\r\n";
                    fin += j.getString("Description") + " (" + j.getString("Rating") + ")";
                    allColl.add(fin);
                    obtainAdapter.notifyDataSetChanged();

                }
            }
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

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
    private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        if(intent.hasExtra("JSONFromCMS")) {
            try {
                ja = new JSONArray(intent.getStringExtra("JSONFromCMS"));
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

    private BroadcastReceiver ebr = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            badges = intent.getStringExtra("CollectionElements");
            String[] badgeArr = badges.split("%");
            createElements(badgeArr);
        }
    };

    @Override
    public void onClick(View view) {
        if(view.getId() == b.getId()) {
            Intent getIntent = new Intent(getContext(), JSONGet.class);
            getContext().startService(getIntent);
            //b.setEnabled(false);
        }
        else if(view.getId() == cancel.getId()){
            // BEGIN: ADDED BY SEAN 2/1/2017
            Fragment fr = new Local_loc();
            //fr.setArguments(b);
            android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.Nav_Activity, fr);
            ft.commit();
            // END: ADDED BY SEAN 2/1/2017

            //Intent ret = new Intent(view.getContext(), Local_loc.class);
            //startActivity(ret);

            /*DownloadManager dm = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            DownloadManager.Request zipReq = new DownloadManager.Request(Uri.parse("https://snippets.khromov.se/wp-content/uploads/2013/08/stock_pictures_from_morguefile.zip"));
            zipReq.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "test.zip");
            long uriID = dm.enqueue(zipReq);*/

        }
        else if(((String)(view.getTag())).compareTo("Apply") == 0) {
            sendAll.setTag("Send");
            sendAll.setText(R.string.obtainConfirm);
            if (added.size() != 0) {
                for (Collection i : added) {
                    collect += i.getCID() + ",";
                }
                Intent collIntent = new Intent(getContext(), JSONElements.class);
                collIntent.putExtra("SelectedCollectionCIDs", collect);
                getContext().startService(collIntent);
            }
        }
        else if(((String)(view.getTag())).compareTo("Send") == 0) {
            Fragment fr = new Local_loc();
            Bundle coll = new Bundle();
            if (added.size() != 0) {
                coll.putSerializable("CollectionList", added);
            }
            fr.setArguments(coll);
            android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.Nav_Activity, fr);
            ft.commit();
        }
    }
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.obtainable_loc, container, false);
        super.onCreate(savedInstanceState);
        Intent intent = getActivity().getIntent();
        added = new ArrayList<>();
        if (intent.hasExtra("LocalCollections")) {
            fromFile = (ArrayList<Collection>) intent.getSerializableExtra("Collections");
        }
        lv = (ListView) v.findViewById(R.id.listView);
        b = (Button) v.findViewById(R.id.button1);
        sendAll = (Button) v.findViewById(R.id.button3);
        cancel = (Button) v.findViewById(R.id.Return);
        LocalBroadcastManager.getInstance(v.getContext()).registerReceiver(br, new IntentFilter("FromCMS"));
        LocalBroadcastManager.getInstance(v.getContext()).registerReceiver(ebr, new IntentFilter("Elements"));
        b.setOnClickListener(this);
        sendAll.setOnClickListener(this);
        cancel.setOnClickListener(this);
        iv = (ImageView) v.findViewById(R.id.imageView6);
        collections = new ArrayList<>();
        collection = new ArrayList<>();
        collect = "";
        collBundle = new Bundle();
        sendAll.setTag("Apply");
        allColl = new ArrayList<>();
        obtainAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, allColl)//{
        /*@Override
        public View getView(int position, View convertView, ViewGroup parent){
            Vi
        }
    }*/;
        lv.setAdapter(obtainAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (added.size() == 0) {
                    Collection c = collection.get(i);
                    added.add(c);
                } else if (added.contains(collection.get(i))) {
                    added.remove(collection.get(i));
                } else {
                    added.add(collection.get(i));
                }
            }
        });
        return v;
    }
}