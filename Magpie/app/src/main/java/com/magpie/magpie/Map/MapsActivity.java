package com.magpie.magpie.Map;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.magpie.magpie.CollectionUtils.Collection;
import com.magpie.magpie.CollectionUtils.Element;
import com.magpie.magpie.NavActivity;
import com.magpie.magpie.R;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * TODO: remove this file.
 * This Activity is no longer needed. All map functionality is now in NavActivity
 */

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener,
        GoogleApiClient.ConnectionCallbacks, GoogleMap.OnMarkerClickListener {

    /**
     * TODO:
     * Go from badge info screen to map, centered on marker
     * Go from badge screen to map, centered on user
     * Swipe right to go from map back to badge screen
     * Access to
     */

    private final int REQUEST_LOCATION = 1;
    private final float DEFAULT_ZOOM = 18;

    // Keys
    private String mBundleExtraKey = "";
    private String mActiveCollectionKey = "";
    private String zoomKey = "";

    private GoogleMap mMap;

    /**
     * I moved the LocationManager to a member variable in order to keep a persistent reference to
     * attach a LocationListener to. LocationManager is instantiated in onMapReady().
     */
    private LocationManager mLocManager;
    //private TextView mCollectionTitleTextView;
    //private TextView mTempCoordinateTextView;
    //private TextView mDistanceTextView;
    //private TextView mTimeTextView;

    private Collection mCollection;
    private Marker mSelectedMarker;
    private ArrayList<MarkerOptions> mMarkers;
    private Location mMyLocation;
    private String mLastUpdateTime;
    private boolean mRequestingLocationUpdates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mBundleExtraKey = getString(R.string.bundle_extra_key);
        mActiveCollectionKey = getString(R.string.active_collection_key);
        zoomKey = getString(R.string.zoom_key);

        // Get intent from the previous activity if applicable
        Intent intent = getIntent();

        //super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //mCollectionTitleTextView = (TextView) findViewById(R.id.collectionTitleTextView);
        //mTempCoordinateTextView = (TextView) findViewById(R.id.tempCoordinateTextView);
        //mDistanceTextView = (TextView) findViewById(R.id.distanceTextView);
        //mTimeTextView = (TextView) findViewById(R.id.minutesTextView);

        mMarkers = new ArrayList<>();

        if (intent.hasExtra(mBundleExtraKey)) {

            Bundle b = intent.getBundleExtra(mBundleExtraKey);
            if (b.containsKey(mActiveCollectionKey)) {
                // If the Intent does contain a Bundle from a previous Activity
                //mCollection = (Collection) b.getSerializable(mActiveCollectionKey);
                //mCollection = ((NavActivity).g

            } else {
                // If the Intent does NOT contain a Bundle from a previous Activity
                //mCollectionTitleTextView.setText(getResources().getString(R.string.no_collection_selected));
                //mTempCoordinateTextView.setText(getResources().getString(R.string.no_location));
            }
        }
    }

    /**
     * Saves the zoom level and the active collection to the saved Bundle
     * @param outState the container for data to be passed between intents
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {

        // TODO: save to preferences instead?
        outState.putFloat(zoomKey, mMap.getCameraPosition().zoom);
        //if (mCollection != null)

            outState.putSerializable(mActiveCollectionKey, mCollection);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState.containsKey(mActiveCollectionKey)) {
            mCollection = (Collection) savedInstanceState.get(mActiveCollectionKey);
            if (mCollection != null) {
                createMarkerList();
                //mCollectionTitleTextView.setText(mCollection.getName());
            }
            if (savedInstanceState.containsKey(zoomKey)) {
                // TODO: set zoom
            }
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.leaving_map_alert)
                .setMessage("Are you sure you want to return to Your Collections?")
                .setPositiveButton(R.string.yes_confirmation, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Bundle b = new Bundle();
                        b.putSerializable(mActiveCollectionKey, mCollection);
                        Intent i = new Intent(MapsActivity.this, NavActivity.class);
                        i.putExtra(mBundleExtraKey, b);
                        startActivity(i);
                        finish();
                    }
                })
                .setNegativeButton(R.string.no_confirmation, null)
                .show();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        /**
         * LocationManager instantiated here. LocationListener also attached.
         */
        mLocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // TODO: add LocationListener

        /*  This portion was added by default
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        */


        mMyLocation = getLocation();
        if (mMyLocation != null) {
            moveToLocation(mMyLocation);
            //mTempCoordinateTextView.setText(mMyLocation.getLatitude()+", "+mMyLocation.getLongitude());

            // TESTING
            //mCollection = Collection.collectionTestBuilder(mMyLocation.getLatitude(), mMyLocation.getLongitude());
            //mCollectionTitleTextView.setText(mCollection.getName());
            //placeTestMarkers();
            createMarkerList();
            placeMarkers();
            // TESTING END
        }

        //placeMarkers();

        initMap();
    }

    private void initMap() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            /**
             * Calls ActivityCompat.requestPermissions to request missing permissions.
             */
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
            return;
        }
        mMap.setMyLocationEnabled(true);

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(false);

        /**
         * Register the map as a Marker click listener. This is so when a marker is clicked, it will
         * be set as the currently tracked Marker the user will be given relative info for in the
         * map UI
         */
        mMap.setOnMarkerClickListener(this);

        /**
         * After UI settings have been established and markers have been placed, the map begins
         * running.
         */
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                /*
                Location location = getLocation();
                if (location != null)
                    moveToLocation(location);
                */


                mMyLocation = getLocation();
                if (mMyLocation != null) {
                    moveToLocation(mMyLocation);
                    //mTempCoordinateTextView.setText("Lat: "+mMyLocation.getLatitude()+", Lon: "+mMyLocation.getLongitude());

                }

            }
        };
        Handler handler = new Handler();
        handler.postDelayed(runnable, 200);
    }

    private Location getLocation() {

        Location location = null;

        String provider = getProvider(Criteria.ACCURACY_FINE, mLocManager.GPS_PROVIDER);
        try {
            location = mLocManager.getLastKnownLocation(provider);
        } catch (SecurityException e) {
            Log.e("Permission not granted", "Security Exception: "+e.getMessage());
        }

        if (location==null) {
            provider = getProvider(Criteria.ACCURACY_COARSE,
                    mLocManager.NETWORK_PROVIDER);
            try {
                location = mLocManager.getLastKnownLocation(provider) ;
            } catch(SecurityException e) {
                Log.e("Permission not granted", "Security Exception: "+e.getMessage());
            }
        }

        return location ;
    }

    /**
     * I moved the LocationManager to a member variable. It was originally passed as the first
     * argument in this method. I'm making this note in case I need to switch back for any reason.
     */
    private String getProvider(int accuracy, String defProvider) {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(accuracy);

        String providerName = mLocManager.getBestProvider(criteria, false);
        if (providerName == null)
            providerName = defProvider;

        if (!mLocManager.isProviderEnabled(providerName)) {

            View parent = findViewById(R.id.map) ;
            Snackbar snack = Snackbar.make(parent, "Location Provider Not Enabled: Goto Settings?",
                    Snackbar.LENGTH_LONG) ;
            snack.setAction("Confirm", new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) ;
                }

            });

            snack.show();
        }

        return providerName ;
    }

    /**
     * Relocates the camera to be centered on the user's location
     * @param location the user's current location.
     */
    private void moveToLocation(Location location) {

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),
                location.getLongitude()), DEFAULT_ZOOM));
    }

    /**
     * Handles the case where the user grants location permission request
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == REQUEST_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initMap() ;
            } else {
                /**
                 * TODO: Maybe a small popup warning screen?
                 * Might take user back to either login or list screen if permission is not granted.
                 */
                Toast.makeText(this, "Magpie requires location permissions.",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Creates a list of markers from the Elements in the active collection.
     * Markers are saves in the mMarkers member variable.
     */
    private void createMarkerList() {

        if (mCollection != null) {
            ArrayList<Element> elements = mCollection.getCollectionElements();
            for (Element element : elements) {

                MarkerOptions marker = new MarkerOptions();
                marker.position(new LatLng(element.getLatitude(), element.getLongitude()));
                marker.title(element.getName());
                marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.qr_black));

                mMarkers.add(marker);
            }


            placeMarkers();
        }
    }

    /**
     * Places markers on the Map after markers were made in the createMarkerList method
     */
    private void placeMarkers() {

        for (MarkerOptions marker : mMarkers) {
            mMap.addMarker(marker);
        }
    }

    private void placeTestMarkers() {

        /**
         * EXAMPLE:
         * LatLng sydney = new LatLng(-34, 151);
         * mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
         */

        // TODO: left off here!
        mMap.addMarker(new MarkerOptions().position(new LatLng(mMyLocation.getLatitude()+0.001, mMyLocation.getLongitude())).title("Test 0"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(mMyLocation.getLatitude(), mMyLocation.getLongitude()+0.001)).title("Test 1"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(mMyLocation.getLatitude(), mMyLocation.getLongitude()-0.001)).title("Test 2"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(mMyLocation.getLatitude()-0.001, mMyLocation.getLongitude())).title("Test 3"));
    }

    private void updateUI() {
        //mTempCoordinateTextView.setText("Lat: "+mMyLocation.getLatitude()+", Lon: "+mMyLocation.getLongitude());
    }



    @Override
    public void onLocationChanged(Location location) {
        mMyLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    protected void startLocationUpdates() {
        //LocationServices.FusedLocationApi.
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        onMarkerSelected(marker);
        return true;
    }

    private void onMarkerSelected(Marker marker) {

        mSelectedMarker = marker;
        float[] results = new float[1];
        Location.distanceBetween(mMyLocation.getLatitude(), mMyLocation.getLongitude(), mSelectedMarker.getPosition().latitude, mSelectedMarker.getPosition().longitude, results);
        //mDistanceTextView.setText("Distance: "+results[0]);
        //mTimeTextView.setText("Time: "+(results[0]/1.4)+"s");
        // TODO: fill in UI elements pertaining to marker.
    }
}
