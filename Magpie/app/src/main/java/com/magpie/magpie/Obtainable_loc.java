package com.magpie.magpie;

import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Obtainable_loc extends AppCompatActivity implements View.OnClickListener{
    Button b;
    TableLayout tl;
    String json;
    String parsedJSON;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.obtainable_loc);
        tl = (TableLayout)findViewById(R.id.MainTable);
        b = (Button) findViewById(R.id.button1);
        LocalBroadcastManager.getInstance(this).registerReceiver(br, new IntentFilter("Passing"));
        LocalBroadcastManager.getInstance(this).registerReceiver(pbr, new IntentFilter("parsedJSONPassing"));
        b.setOnClickListener(this);
    }

    private void createTableViews(String s) {
        String[] jsonSplit = s.split(",");
        for(int i = 0; i < jsonSplit.length; i++){
            RelativeLayout temp = new RelativeLayout(this);
            temp.setId(i);
            appendData(temp, jsonSplit[i]);
            tl.addView(temp);
        }
    }

    private void appendData(RelativeLayout temp, String s) {
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        final TextView tv = new TextView(this);
        tv.setId(R.id.textv1);
        Button b = new Button(this);
        b.setId(R.id.butn1);
        b.setText("ADD");
        b.setOnClickListener(this);
        tv.append(s);
        rlp.addRule(RelativeLayout.CENTER_VERTICAL);
        temp.addView(tv);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, tv.getId());
        temp.addView(b, rlp);

    }

    private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            json = intent.getStringExtra("JSON");
            Intent parseIntent = new Intent(Obtainable_loc.this, JSONParse.class);
            parseIntent.putExtra("JSON", json);
            Obtainable_loc.this.startService(parseIntent);
        }
    };

    private BroadcastReceiver pbr = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            parsedJSON = intent.getStringExtra("parsed");
            createTableViews(parsedJSON);
        }
    };

    @Override
    public void onClick(View view) {
        if(view.getId() == b.getId()) {
            Intent Getintent = new Intent(Obtainable_loc.this, JSONGet.class);
            Obtainable_loc.this.startService(Getintent);
        }
        else if(((RelativeLayout)(ViewGroup)view.getParent()).getChildAt(0) instanceof TextView){
            String data = ((TextView) ((RelativeLayout)(ViewGroup)view.getParent()).getChildAt(0)).getText().toString();
            Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
        }
    }
}
