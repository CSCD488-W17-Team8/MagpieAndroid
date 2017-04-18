package com.magpie.magpie;

import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class Obtainable_loc extends AppCompatActivity implements View.OnClickListener{
    ArrayList<String> collections;
    ArrayList<Collection> collection;
    ArrayList<Collection> added;
    Bundle collBundle;
    Button b;
    Button imageTest;
    Button sendAll;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.obtainable_loc);
        lv = (ListView) findViewById(R.id.listView);
        b = (Button) findViewById(R.id.button1);
        sendAll = (Button) findViewById(R.id.button3);
        imageTest = (Button) findViewById(R.id.button2);
        LocalBroadcastManager.getInstance(this).registerReceiver(br, new IntentFilter("Passing"));
        LocalBroadcastManager.getInstance(this).registerReceiver(ebr, new IntentFilter("Elements"));
        b.setOnClickListener(this);
        imageTest.setOnClickListener(this);
        sendAll.setOnClickListener(this);
        iv = (ImageView) findViewById(R.id.imageView6);
        collections = new ArrayList<>();
        collection = new ArrayList<>();
        added = new ArrayList<>();
        collect = "";
        collBundle = new Bundle();
        sendAll.setTag("Apply");
        allColl = new ArrayList<>();
        obtainAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, allColl)//{
            /*@Override
            public View getView(int position, View convertView, ViewGroup parent){
                Vi
            }
        }*/;
        lv.setAdapter(obtainAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(added.size() == 0){
                    Collection c = collection.get(i);
                    added.add(c);
                }
                else if(added.contains(collection.get(i))){
                    added.remove(collection.get(i));
                }
                else{
                    added.add(collection.get(i));
                }
            }
        });
    }
    private void parseJSON() {
        try {
            String[] s = new String[9];
            Collection c;
            JSONObject j;
            for (int i = 0; i < ja.length(); i++) {
                j = ja.getJSONObject(i);
                if (j.getInt("IsActive") == 1) { //) is false, 1 is true
                    c = new Collection(j);
                    c.addBitmap("http://magpiehunt.com/image/logo/" + c.getCID());
                    collection.add(c);
                    s[0] = j.getString("CID");
                    s[1] = j.getString("Name");
                    s[2] = j.getString("City") + ", " + j.getString("State");
                    s[3] = j.getString("Rating");
                    s[4] = j.getString("Description");
                    s[5] = j.getString("NumberOfLandmarks");
                    s[6] = j.getString("CollectionLength");
                    s[7] = j.getString("IsOrder");
                    s[8] = j.getString("PicID");
                    String fin = s[1] + " : " + s[2] + "\r\n";
                    fin += s[4] + " (" + s[3] + ")";
                    allColl.add(fin);
                    obtainAdapter.notifyDataSetChanged();

                }
            }
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        if(intent.hasExtra("JSON")) {
            json = intent.getStringExtra("JSON");
            try {
                ja = new JSONArray(json);
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
            badges = intent.getStringExtra("Landmarks");
            String[] badgeArr = badges.split("%");
            createElements(badgeArr);
        }
    };

    @Override
    public void onClick(View view) {
        if(view.getId() == b.getId()) {
            Intent getIntent = new Intent(Obtainable_loc.this, JSONGet.class);
            getIntent.putExtra("Type", "All");
            Obtainable_loc.this.startService(getIntent);
        }
        else if(((String)(view.getTag())).compareTo("Apply") == 0) {
            for(Collection i : added){
                collect += i.getCID() + ",";
            }
            sendAll.setTag("Send");
            sendAll.setText("Confirm");
            Intent collIntent = new Intent(Obtainable_loc.this, JSONElements.class);
            collIntent.putExtra("Type", collect);
            Obtainable_loc.this.startService(collIntent);
        }
        else if(((String)(view.getTag())).compareTo("Send") == 0){
            collBundle.putSerializable("CollectionList", added);
            //TODO: Change to onSavedInstanceState method of data passing.
            Intent local = new Intent(view.getContext(), Local_loc.class);
            local.putExtras(collBundle);
            startActivity(local);
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
}