package com.magpie.magpie;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

import com.magpie.magpie.CollectionUtils.*;

public class Local_loc extends AppCompatActivity {
    String all = "";
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
        tv.append(all);
    }
}
