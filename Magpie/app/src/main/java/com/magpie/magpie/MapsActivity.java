package com.magpie.magpie;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.widget.TextViewCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.magpie.magpie.CollectionUtils.Collection;
import com.magpie.magpie.CollectionUtils.Element;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private final int REQUEST_LOCATION = 1;
    private final float DEFAULT_ZOOM = 18;
    private final String ACTIVE_COLLECTION_KEY = getResources().getString(R.string.active_collection_key);
    private final String ZOOM_KEY = getResources().getString(R.string.zoom_key);
    //private final String ACTIVE_COLLECTION_KEY = "ACTIVE COLLECTION";
    //private final String ZOOM_KEY = "ZOOM";

    private GoogleMap mMap;

    private Collection mCollection;
    private ArrayList<MarkerOptions> mMarkers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        TextView collectionTitleTextView = (TextView)findViewById(R.id.collectionTitleTextView);
        collectionTitleTextView.setText(getString(R.string.no_collection_selected));

        TextView tempCoordinateTextView = (TextView)findViewById(R.id.tempCoordinateTextView);
        tempCoordinateTextView.setText(getString(R.string.no_location));

        mMarkers = new ArrayList<>();

        /**
         * The active collection is restored from the savedInstanceState.
         */
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(ACTIVE_COLLECTION_KEY)) {  // checks if key is present
                mCollection = (Collection) savedInstanceState.get(ACTIVE_COLLECTION_KEY);
                if (mCollection != null) {
                    createMarkerList();
                    collectionTitleTextView.setText(mCollection.getName());
                }
            }
            if (savedInstanceState.containsKey(ZOOM_KEY)) {
                // set zoom
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
        outState.putFloat(ZOOM_KEY, mMap.getCameraPosition().zoom);
        //if (mCollection != null)
            outState.putSerializable(ACTIVE_COLLECTION_KEY, mCollection);
        super.onSaveInstanceState(outState);
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

        /*  This portion was added by default
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        */

        initMap();
    }

    private void initMap() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            // we don’t yet have permission, so request it and return

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Location location = getLocation();
                if (location != null)
                    moveToLocation(location);
            }
        };
        Handler handler = new Handler();
        handler.postDelayed(runnable, 200);
    }

    private Location getLocation() {

        Location location = null;

        LocationManager locManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        String provider = getProvider(locManager, Criteria.ACCURACY_FINE, locManager.GPS_PROVIDER);
        try {
            location = locManager.getLastKnownLocation(provider);
        } catch (SecurityException e) {
            Log.e("Permission not granted", "Security Exception: "+e.getMessage());
        }

        if (location==null) {
            provider = getProvider(locManager, Criteria.ACCURACY_COARSE,
                    locManager.NETWORK_PROVIDER);
            try {
                location = locManager.getLastKnownLocation(provider) ;
            } catch(SecurityException e) {
                Log.e("Permission not granted", "Security Exception: "+e.getMessage());
            }
        }

        return location ;
    }

    private String getProvider(LocationManager locManager, int accuracy, String defProvider) {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(accuracy);

        String providerName = locManager.getBestProvider(criteria, false);
        if (providerName == null)
            providerName = defProvider;

        if (!locManager.isProviderEnabled(providerName)) {

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == REQUEST_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initMap() ;
            } else {
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
}
