package com.magpie.magpie;

import android.content.Intent;
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

public class Local_loc extends AppCompatActivity {
    String all = "";
    Button b;
    TableLayout tl;
    File jsonFile;
    FileOutputStream fos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_loc);
        Intent intent = this.getIntent();
        Bundle b = intent.getExtras();
        ArrayList<Collection> collections = (ArrayList<Collection>)b.getSerializable("CollectionList");
        TextView tv = (TextView) findViewById(R.id.textView1);
        for(Collection c : collections){
            ArrayList<Element> elements = c.getCollectionElements();
            for(Element e : elements){
                all += e.getName() + "\n";
            }
        }
    }
}