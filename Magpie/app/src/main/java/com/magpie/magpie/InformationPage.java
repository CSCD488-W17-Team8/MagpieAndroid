package com.magpie.magpie;

import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.magpie.magpie.CollectionUtils.*;

public class InformationPage extends AppCompatActivity implements View.OnClickListener{

    /**       CONSTANTS          */
    private final double RANGE = 20;

    private final long MIN_TIME_BW_UPDATES = 2000;
    private final float MIN_DISTANCE_CHANGE_FOR_UPDATES = 1;
    private final double METERS_TO_FEET = 3.28084;
    private final double METERS_TO_MILES = 0.000621371;

            /**       WIDGETS            */
    ImageButton btn_map, btn_share, btn_collect;

    TextView result_box;
    ImageView result_image;
    Element element;
    boolean isCollected = false;
    LocationManager locationManager;

    String result_text;
    private TrackGPS gps;
    double longitude_user, longitude_dest = -117.583700;
    double latitude_user, latitude_dest = 47.490508;
    Location user_location = new Location("user_location");
    Location dest_location = new Location("dest_location");




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        gps = new TrackGPS(InformationPage.this);

        result_box = (TextView)findViewById(R.id.Info_box_view);
        result_image = (ImageView)findViewById(R.id.display_img);

        dest_location.setLatitude(latitude_dest);
        dest_location.setLongitude(longitude_dest);

        btn_map = (ImageButton)findViewById(R.id.map_btn);
        btn_share = (ImageButton)findViewById(R.id.share_btn);
        btn_collect = (ImageButton) findViewById(R.id.collect_btn);


        btn_map.setOnClickListener(this);
        btn_share.setOnClickListener(this);
        btn_collect.setOnClickListener(this);

    }



    @Override
    public void onClick(View v)
    {

        Intent intent;
        /**
         *
         * The share and map button result in
         * launching an intent
         *
         * The collect Button results in the calling of a
         * helper method to determine if the user is able
         * to collect the badge or not.
         *
         *
         */

        if(v.getId() == btn_map.getId())
        {
            /**
             *
             * Simply launches an intent to the Map Activity
             *
             */

            Toast.makeText(this, "Not implemented", Toast.LENGTH_SHORT).show();
        }
        else if(v.getId() == btn_share.getId())
        {

            /**
             *
             * Can the user press the share button if they have not
             * collected the current Badge.
             *
             */
            Toast.makeText(this, "Not implemented", Toast.LENGTH_SHORT).show();

        }
        else if(v.getId() == btn_collect.getId())
        {

            Boolean result = collectBadge();
            if(result)
            {
                /**
                 *
                 * Do something like display stuff or
                 * whatever.
                 */
            }

        }

    }



    public boolean collectBadge()
    {

        /**
         * From here the application must determine the current mode
         * (offline or online / GPS Location Services on or off)
         *
         * If GPS Location Services are off' (This might be done somewhere else);
         *      -Launches the QR Reader
         *
         * Else
         *      Grabs the User's current location and compares it
         *       with the badge location. If the user's location
         *       is within Range, collect the badge and then return true;
         *
         *       (Probably Call a method here that Fills in the Results)
         *
         *       Else
         *          Inform the User with a Toast that they are not within Range.
         *
         */



        Log.v("Inside the collect", "Success!");

        if(gps.canGetLocation()){


            Log.v("CanGet", "Success!");
            longitude_user = gps.getLongitude();
            latitude_user = gps.getLatitude();
            user_location.setLongitude(longitude_user);
            user_location.setLatitude(latitude_user);

            Log.v("Distance", "Success");
            double distance = user_location.distanceTo(dest_location);

            Log.v("distance :", "" + distance);
            int result_feet = (int)(distance * METERS_TO_FEET);
            Log.v("distance :", "" + result_feet);
            if(result_feet <= 20)
            {
                Toast.makeText(getApplicationContext(), "You Collected the Badge", Toast.LENGTH_LONG);
                isCollected = true;
            }
            else
            {
                Toast.makeText(getApplicationContext(), "You are " + result_feet + " feet away", Toast.LENGTH_LONG);

            }

            //Toast.makeText(getApplicationContext(),"Longitude:"+Double.toString(longitude_user)+"\nLatitude:"+Double.toString(latitude_user),Toast.LENGTH_SHORT).show();
        }
        else
        {

            gps.showSettingsAlert();
        }

        return isCollected;
    }
}
