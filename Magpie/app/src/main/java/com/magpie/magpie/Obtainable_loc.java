package com.magpie.magpie;

import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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

public class Obtainable_loc extends AppCompatActivity implements View.OnClickListener{
    ArrayList<String> collections;
    Button b;
    Button imageTest;
    Button sendAll;
    ImageView iv;
    TableLayout tl;
    String json;
    String collect;
    String badges;
    Bitmap bm;
    JSONArray ja;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.obtainable_loc);
        tl = (TableLayout)findViewById(R.id.MainTable);
        b = (Button) findViewById(R.id.button1);
        sendAll = (Button) findViewById(R.id.button3);
        imageTest = (Button) findViewById(R.id.button2);
        LocalBroadcastManager.getInstance(this).registerReceiver(br, new IntentFilter("Passing"));
        //LocalBroadcastManager.getInstance(this).registerReceiver(pbr, new IntentFilter("parsedJSONPassing"));
        LocalBroadcastManager.getInstance(this).registerReceiver(ibr, new IntentFilter("Image"));
        b.setOnClickListener(this);
        imageTest.setOnClickListener(this);
        sendAll.setOnClickListener(this);
        iv = (ImageView) findViewById(R.id.imageView6);
        collections = new ArrayList<>();
        collect = "";
    }
    private void parseJSON(){
        try{
            String[] s = new String[9];
            JSONObject j;
            for(int i = 0; i < ja.length(); i++){
                j = ja.getJSONObject(i);
                if(j.getInt("IsActive") == 0){
                    s[0] = j.getString("CID");
                    s[1] = j.getString("Name");
                    s[2] = j.getString("City") + ", " + j.getString("State");
                    s[3] = j.getString("Rating");
                    s[4] = j.getString("Description");
                    s[5] = j.getString("NumberOfLandmarks");
                    s[6] = j.getString("CollectionLength");
                    s[7] = j.getString("IsOrder");
                    s[8] = j.getString("PicID");
                    RelativeLayout temp = new RelativeLayout(this);
                    temp.setId(i);
                    appendData(temp, s, j.toString());
                    tl.addView(temp);
                }
            }
        }
        catch (JSONException je){
            je.printStackTrace();
        }
    }
    /*private void createTableViews(String s) {
        String[] jsonSplit = s.split(",");
        for(int i = 0; i < jsonSplit.length; i++){
            RelativeLayout temp = new RelativeLayout(this);
            temp.setId(i);
            appendData(temp, jsonSplit[i]);
            tl.addView(temp);
        }
    }*/

    private void appendData(RelativeLayout temp, String[] s, String jSON) {
        String fin = s[1] + " : " + s[2] + "\r\n";
        fin += s[4] + " (" + s[3] + ")";
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        final TextView tv = new TextView(this);
        tv.setId(R.id.textv1);
        tv.setTag(s[0]);
        Button b = new Button(this);
        b.setId(R.id.butn1);
        b.setText("ADD");
        b.setTag(jSON);
        b.setOnClickListener(this);

        tv.append(fin);
        rlp.addRule(RelativeLayout.CENTER_VERTICAL);
        temp.addView(tv);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, tv.getId());
        temp.addView(b, rlp);

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
        }
        else{
            badges = intent.getStringExtra("Landmarks");
        }
        /*Intent parseIntent = new Intent(Obtainable_loc.this, JSONParse.class);
        parseIntent.putExtra("JSON", json);
        Obtainable_loc.this.startService(parseIntent);*/

        }
    };

    /*private BroadcastReceiver pbr = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            parsedJSON = intent.getStringExtra("parsed");
            createTableViews(parsedJSON);
        }
    };*/

    private BroadcastReceiver ibr = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            bm = intent.getParcelableExtra("Image");
            iv.setImageBitmap(bm);
        }
    };

    @Override
    public void onClick(View view) {
        if(view.getId() == b.getId()) {
            Intent getIntent = new Intent(Obtainable_loc.this, JSONGet.class);
            getIntent.putExtra("Type", "All");
            Obtainable_loc.this.startService(getIntent);
        }
        else if(view.getId() == imageTest.getId()){
            Intent imageIntent = new Intent(Obtainable_loc.this, ImageTest.class);
            Obtainable_loc.this.startService(imageIntent);
        }
        else if(view.getId() == sendAll.getId()){
            Intent collIntent = new Intent(Obtainable_loc.this, JSONGet.class);
            collIntent.putExtra("Type", collect);
            String[] badgeArr = badges.split("%");
            Intent local = new Intent(view.getContext(), Local_loc.class);
            String[] send = new String[collections.size()];
            for(int i = 0; i < collections.size(); i++){
                send[i] = collections.get(i) + "%" + badgeArr[0];
            }
            local.putExtra("Collections", send);
            startActivity(local);
        }
        else if(((RelativeLayout)(ViewGroup)view.getParent()).getChildAt(0) instanceof TextView){
            collect += (String) ((TextView) ((RelativeLayout)(ViewGroup)view.getParent()).getChildAt(0)).getTag() + ",";
            String data = (String) ((Button) ((RelativeLayout)(ViewGroup)view.getParent()).getChildAt(1)).getTag();
            Intent local = new Intent(view.getContext(), Local_loc.class);
            collections.add(data);
        }

    }
}