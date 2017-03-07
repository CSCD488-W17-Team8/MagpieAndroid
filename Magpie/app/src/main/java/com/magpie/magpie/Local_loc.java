package com.magpie.magpie;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Local_loc extends AppCompatActivity {
    String all = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_loc);
        String[] json = getIntent().getStringArrayExtra("Collections");
        TextView tv = (TextView) findViewById(R.id.textView1);
        for(String s : json){
            all += s + "\n";
        }
        tv.append(all);
    }
}
