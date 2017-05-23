package com.magpie.magpie;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.magpie.magpie.CollectionUtils.Element;
import com.magpie.magpie.UserProgress.SendProgress;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InfoPage.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InfoPage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InfoPage extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    /**       CONSTANTS          */
    private final double RANGE = 20;

    private Element curElement;
    private final long MIN_TIME_BW_UPDATES = 2000;
    private final float MIN_DISTANCE_CHANGE_FOR_UPDATES = 1;
    private final double METERS_TO_FEET = 3.28084;
    private final double METERS_TO_MILES = 0.000621371;

    /**       WIDGETS            */
    ImageButton btn_map, btn_share, btn_collect;

    TextView result_box;
    ImageView result_image;

    boolean isCollected = false;
    LocationManager locationManager;

    String result_text;
    private TrackGPS gps;
    double longitude_user, longitude_dest;
    double latitude_user, latitude_dest;
    Location user_location = new Location("user_location");
    Location dest_location = new Location("dest_location");

    NavActivity navActivity;


    public InfoPage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InfoPage.
     */
    // TODO: Rename and change types and number of parameters
    public static InfoPage newInstance(String param1, String param2) {

        InfoPage fragment = new InfoPage();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navActivity = (NavActivity) getActivity();
        latitude_dest  = navActivity.getActiveCollection().getCollectionElements().get(navActivity.getActiveCollection().getSelectedElement()).getLatitude();
        longitude_dest = navActivity.getActiveCollection().getCollectionElements().get(navActivity.getActiveCollection().getSelectedElement()).getLongitude();



        gps = new TrackGPS(getContext());


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_infopage, container, false);
        result_box = (TextView)view.findViewById(R.id.Info_box_view);
        result_image = (ImageView)view.findViewById(R.id.display_img);

        dest_location.setLatitude(latitude_dest);
        dest_location.setLongitude(longitude_dest);

        btn_map = (ImageButton)view.findViewById(R.id.map_btn);
        btn_share = (ImageButton)view.findViewById(R.id.share_btn);
        btn_collect = (ImageButton) view.findViewById(R.id.collect_btn);

        btn_map.setOnClickListener(this);
        btn_share.setOnClickListener(this);
        btn_collect.setOnClickListener(this);

        result_box.setText(navActivity.getActiveCollection().getCollectionElements().get(navActivity.getActiveCollection().getSelectedElement()).getName() + "\n" + navActivity.getActiveCollection().getCollectionElements().get(navActivity.getActiveCollection().getSelectedElement()).getDesc());
        isCollected = navActivity.getActiveCollection().getCollectionElements().get(navActivity.getActiveCollection().getSelectedElement()).isCollected();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v)
    {



        if(v.getId() == btn_map.getId())
        {
            /**
             *
             * Simply launches an intent to the Map Activity
             *
             */

            displayToast();
        }
        else if(v.getId() == btn_share.getId())
        {

            /**
             *
             * Can the user press the share button if they have not
             * collected the current Badge.
             *
             */
            Fragment shareFrag = new ShareFragment();
            navActivity.startNewFragment(shareFrag);

        }
        else if(v.getId() == btn_collect.getId())
        {
            if(!isCollected) {
                Boolean result = collectBadge();
                if (result) {
                    /**
                     *
                     * Do something like display stuff or
                     * whatever.
                     */
                    Intent intent = new Intent(getActivity(), SendProgress.class);
                    intent.putExtra("UserProgressCollection", navActivity.getCollections());
                    navActivity.startService(intent);
                }
            }
            else
            {
                Toast.makeText(getActivity(), "Badge is already collected", Toast.LENGTH_SHORT).show();
            }
        }


    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

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
            if(isCollected || result_feet <= 20)
            {
                displayToast("You Collected the Badge");
                isCollected = true;
            }
            else
            {   displayToast("You are " + result_feet + " feet away");  }

            //Toast.makeText(getApplicationContext(),"Longitude:"+Double.toString(longitude_user)+"\nLatitude:"+Double.toString(latitude_user),Toast.LENGTH_SHORT).show();
        }
        else
        {

            gps.showSettingsAlert();
        }

        return isCollected;
    }


    public void displayToast(String value)
    {
        Toast.makeText(getContext(), value, Toast.LENGTH_SHORT).show();
    }

    public void displayToast()
    {
        Toast.makeText(getContext(), "Not implemented", Toast.LENGTH_SHORT).show();
    }
}
