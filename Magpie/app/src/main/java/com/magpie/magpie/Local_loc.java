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
        tl = (TableLayout) findViewById(R.id.tableLayout);
        String[] json = getIntent().getStringArrayExtra("Collections");
/*        try {
            writeToFile(jsonFile, json);
            jsonFile = new File(getApplicationContext().getFilesDir(), "json.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        createTableViews(json);
    }

    private void writeToFile(File jsonFile, String[] json) throws IOException {
        fos = new FileOutputStream(jsonFile, true);
        for(int i = 0; i < json.length; i++) {
            byte[] jsonBytes = json[i].getBytes();
            fos.write(jsonBytes);
        }
    }

    /**
     * This method creates the table views for the user visual of the data.
     * @param json A String array of JSON objects represented as Strings.
     */
    private void createTableViews(String[] json) {
        for(int i = 0; i < json.length; i++){
            String[] coll = json[i].split("%");
            RelativeLayout temp = new RelativeLayout(this);
            temp.setId(i);
            addData(temp, coll);

            tl.addView(temp);
        }
    }

    /**
     * This method adds the data and views to the layout created in the method createTableViews.
     * @param temp The layout all the data and views will be appended to.
     * @param s The String representation of the JSON object to be displayed to the user.
     */
    private void addData(RelativeLayout temp, String[] s) {
        try {
            JSONObject jSON = new JSONObject(s[0]);
            RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            final TextView tv = new TextView(this);
            tv.setId(R.id.textv1);
            b = new Button(this);
            b.setId(R.id.butn1);
            b.setText("VIEW BADGES");
            b.setTag(s[1]);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String data = ((Button) ((RelativeLayout) (ViewGroup) view.getParent()).getChildAt(1)).getTag().toString();
                    Toast.makeText(getApplicationContext(), data, Toast.LENGTH_SHORT).show();
                }
            });
            String fin = jSON.getString("Name") + "\r\n" + jSON.getString("NumberOfLandmarks") + "\r\n" + jSON.getString("Description") + " (" + jSON.getString("Rating") + ")";
            tv.append(s[0]);
            rlp.addRule(RelativeLayout.CENTER_VERTICAL);
            temp.addView(tv);
            rlp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, tv.getId());
            temp.addView(b, rlp);
        }
        catch(JSONException e){
            e.printStackTrace();
        }
    }
}