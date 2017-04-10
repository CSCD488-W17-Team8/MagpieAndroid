package com.magpie.magpie;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.ArrayList;

import com.magpie.magpie.CollectionUtils.*;

public class Local_loc extends AppCompatActivity implements View.OnClickListener{
    String all = "";
    Button b;
    TableLayout tl;
    File jsonFile;
    FileOutputStream fos;
    ArrayList<Collection> collections;
    FloatingActionButton obtain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_loc);
        Intent intent = this.getIntent();
        tl = (TableLayout) findViewById(R.id.tableLocal1);
        obtain = (FloatingActionButton) findViewById(R.id.ToObtainable);
        obtain.setOnClickListener(this);
        if(intent.getExtras() != null) {
            Bundle b = intent.getExtras();
            collections = (ArrayList<Collection>) b.getSerializable("CollectionList");
            fillTable();
        }
    }

    public void fillTable(){
        RelativeLayout rl = new RelativeLayout(this);
        String visual;
        TextView tv;
        Button b;
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        for(Collection c : collections){
            visual = c.getName() + "\r\n Number of Landmarks: 0 / " + c.getCollectionSize() +
                    "\r\n" + c.getDescription() + " (" + c.getRating() + ")";
            tv = new TextView(this);
            tv.setId(R.id.textv1);
            tv.setTag(c.getCID());
            b = new Button(this);
            b.setId(R.id.butn1);
            b.setTag("" + c.getCID());
            b.setText("Details");
            b.setOnClickListener(this);
            tv.append(visual);
            rlp.addRule(RelativeLayout.CENTER_VERTICAL);
            rl.addView(tv);
            rlp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, tv.getId());
            rl.addView(b, rlp);
            tl.addView(rl);
        }
    }

    @Override
    public void onClick(View view) {
        if(((RelativeLayout)view.getParent()).getChildAt(1) instanceof Button){
            String cid =  (String)view.getTag();
            int id = Integer.parseInt(cid);
            Collection fin = null;
            for(Collection c : collections){
                if(c.getCID() == id){
                    fin = c;
                }
            }
            Bundle coll = new Bundle();
            coll.putSerializable("TheCollection", fin);
            Intent i = new Intent(view.getContext(), Badge_page.class);
            i.putExtras(coll);
            startActivity(i);
        }
        if(view.getId() == R.id.ToObtainable){
            Intent obtain = new Intent(view.getContext(), Obtainable_loc.class);
            startActivity(obtain);
        }
    }
}