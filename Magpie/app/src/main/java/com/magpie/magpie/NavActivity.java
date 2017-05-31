package com.magpie.magpie;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.magpie.magpie.BadgePage.BadgePage;
import com.magpie.magpie.CollectionUtils.Collection;
import com.magpie.magpie.CollectionUtils.Element;
import com.magpie.magpie.InfoAndShare.InfoPage;
import com.magpie.magpie.LocalList.Local_loc;
import com.magpie.magpie.ObtainableList.Obtainable_loc;
import com.magpie.magpie.QRReader.QRFragment;

import java.util.ArrayList;

public class NavActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMarkerClickListener {
//=======
        //GoogleApiClient.ConnectionCallbacks, GoogleMap.OnMarkerClickListener, ImageButton.OnClickListener {
//>>>>>>> ArrasmithBetaBranch

    private final int REQUEST_LOCATION = 1;
    private final float DEFAULT_ZOOM = 18;

    private ArrayList<Collection> mCollections;
    private Collection mActiveCollection;
    private ArrayList<MarkerOptions> mMarkerList;
    private ArrayList<Element> mMarkerElementsList;
    private Element mActiveElement;

    /**
     * Map-related member variables
     */
    private GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap;
    private LocationManager mLocManager;
    private Marker mSelectedMarker;
    private Location mMyLocation;
    private LocationRequest mLocationRequest;
    private boolean mRequestingLocationUpdates;
    private boolean mLocationPermissionGranted = false;

    /**
     * Persistent UI elements.
     * These will persist across fragment changes.
     */
    private Toolbar mTitleBar;
    private RelativeLayout mViewBar;
    private FragmentManager mFragmentMngr;

    private boolean showingBadgePage = false;
    private boolean mReadFromFile = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);

        /*
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        createLocationRequest();
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi
                .checkLocationSettings(mGoogleApiClient, builder.build());
        */

        /*
        result.setResultCallback(new ResultCallback<LocationSettingsResult>()) {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can
                        // initialize location requests here.
                        //...
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied, but this can be fixed
                        // by showing the user a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(OuterClass.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way
                        // to fix the settings so we won't show the dialog.
                        //...
                        break;
                }
            }
        });
        */

        mRequestingLocationUpdates = false;

        mCollections = new ArrayList<>();
        mActiveCollection = new Collection();

        mMarkerElementsList = new ArrayList<>();
        mMarkerList = new ArrayList<>();
        //mActiveElement = new Element(); // Leave it null?

        mTitleBar = (Toolbar)findViewById(R.id.nav_toolbar);
        mViewBar = (RelativeLayout)findViewById(R.id.view_bar);

        // TODO: set visibility of view_bar

        mFragmentMngr = getSupportFragmentManager(); // TODO: test this
        if (findViewById(R.id.fragment_container) != null) {

            /**
             * Don't inflate a new fragment if a saved instance state is present
             */
            if (savedInstanceState != null) {
                return;
            }

            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new Local_loc()).commit();

        }

        /*
        if (i.hasExtra(mBundleExtraKey)) {

            Bundle b = i.getBundleExtra(mBundleExtraKey);

            if (b.containsKey(mActiveCollectionKey)) {
                mActiveCollection = (Collection) b.getSerializable(mActiveCollectionKey);
            }
        }
        */
    }

    @Override
    protected void onStart() {

        // TODO: determine if this needs to be here?
        //mGoogleApiClient.connect();
        super.onStart();

    }

    @Override
    protected void onStop() {

        //mGoogleApiClient.disconnect();
        super.onStop();

    }

    @Override
    public void onConnected(Bundle connectionHint) {

        //if (checkPermission()) TODO: check for permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            mMyLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            if (mMyLocation != null) {

                // TODO: do stuff here?

            }
        } else {
            Toast.makeText(this.getApplicationContext(), "Woops! Something has gone wrong!", Toast.LENGTH_SHORT).show();
            Log.d("ERROR", "Error: unable to place markers. Permission not granted");
        }

    }

    protected void createLocationRequest() {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(2000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    }

    public void onNavButtonClicked(View v) {

        switch (v.getId()) {

            case R.id.map_nav_button:
                startAllCollectionMapFragment();
                break;

            case R.id.qr_nav_button:
                Fragment qrFrag = new QRFragment();
                startNewFragment(qrFrag);
                //Toast.makeText(getApplicationContext(), "Not ready yet", Toast.LENGTH_SHORT).show();
                break;

            case R.id.home_nav_button:
                //Toast.makeText(getApplicationContext(), "Not ready yet", Toast.LENGTH_SHORT).show();
                startNewFragment(new Local_loc());
                break;

            case R.id.search_nav_button:
                Fragment olocFrag = new Obtainable_loc();
                startNewFragment(olocFrag);
                //Toast.makeText(getApplicationContext(), "Not ready yet", Toast.LENGTH_SHORT).show();
                break;

            case R.id.account_nav_button:
                Toast.makeText(getApplicationContext(), "Not ready yet", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void onRadioButtonClicked(View view) {

        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()) {

            case R.id.radio_list_view:
                if (checked) {
                    BadgePage badgeList = new BadgePage();
                    Bundle b = new Bundle();
                    b.putString("Type", "List");
                    badgeList.setArguments(b);
                    startNewFragment(badgeList);
                }
                break;
            case R.id.radio_grid_view:
                if (checked) {
                    BadgePage badgeGrid = new BadgePage();
                    Bundle b = new Bundle();
                    b.putString("Type", "Grid");
                    badgeGrid.setArguments(b);
                    startNewFragment(badgeGrid);
                }
                break;
            case R.id.radio_map_view:
                if (checked) {

                    startCollectionMapFragment();

                }
                break;

        }

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

            // TESTING. TODO: remove testing parts
            //setActiveCollection(Collection.collectionTestBuilder("Test Collection", mMyLocation.getLatitude(), mMyLocation.getLongitude()));
            // TESTING END
            //createMarkerList();
            placeMarkers();

        }

        initMap();
    }

    /**
     * initializes map and its UI settings.
     */
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
        // TODO: link to calibrate settings?
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
        handler.postDelayed(runnable, 1);
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

            View parent = findViewById(R.id.Nav_Activity) ;
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
                mLocationPermissionGranted = true;
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

    // TODO: remove?
    private void createMarkers() {

        mMarkerList = new ArrayList<>();

        for (Element element : mMarkerElementsList) {

            MarkerOptions marker = new MarkerOptions();
            marker.position(new LatLng(element.getLatitude(), element.getLongitude()));
            marker.title(element.getName());
            //marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.pinavailable)); // Not the final pin image; wasn't working here
            mMarkerList.add(marker);

        }

    }

    private void createMarkerList(Element element) {

        if (element != null) {

            if (mMarkerElementsList != null) {

                mMarkerElementsList.add(element);

            } else {

                Toast.makeText(getApplicationContext(), "Woops! Something has gone wrong!", Toast.LENGTH_SHORT);
                Log.d("NULLPOINTER", "mMarkerElementsList is null. Cannot add marker in createMarkerList.");
            }
        }

    }

    /**
     * Creates a list of markers from the Elements in the active collection.
     * Markers are saved in the mMarkerList member variable.
     */
    private void createCollectionMarkerList(Collection collection) {


        if (collection != null) {

            ArrayList<Element> elements = collection.getCollectionElements();

            for (Element element : elements) {

                createMarkerList(element);

            }
        }
    }

    /**
     * Creates list of Markers from the Elements in all collections.
     */
    private void createAllCollectionMarkerList() {

        if (mCollections != null) {

            for (Collection collection : mCollections) {

                createCollectionMarkerList(collection);

            }
        }
    }

    /**
     * Places markers in mMarkerList
     */
    private void placeMarkers() {

        for (Element element : mMarkerElementsList) {

            MarkerOptions marker = new MarkerOptions();
            marker.position(new LatLng(element.getLatitude(), element.getLongitude()));
            marker.title(element.getName());

            if (element.isCollected())
                marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_black));
            else
                marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_grey));

            mMarkerList.add(marker);
            mMap.addMarker(marker);

        }

        /*
        for (MarkerOptions marker : mMarkerList) {
            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_black));
            mMap.addMarker(marker);
        }
        */
    }

    @Override
    public void onLocationChanged(Location location) {

        mMyLocation = location;
        updateUI();

    }

    private void updateUI() {

        moveToLocation(mMyLocation);

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
    public void onConnectionSuspended(int i) {

    }

    protected void startLocationUpdates() {
        //LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        onMarkerSelected(marker);
        return true;
    }

    private void onMarkerSelected(Marker marker) {

        mSelectedMarker = marker;
        //float[] results = new float[1];
        //Location.distanceBetween(mMyLocation.getLatitude(), mMyLocation.getLongitude(), mSelectedMarker.getPosition().latitude, mSelectedMarker.getPosition().longitude, results);

        // This is for testing
        Toast.makeText(getApplicationContext(), marker.getTitle()+" marker selected.", Toast.LENGTH_SHORT).show();

        // TODO: determine marker
        if (mMarkerElementsList != null) {

            for (Element element : mMarkerElementsList) {

                if (marker.getTitle().equals(element.getName())) {

                    // TODO: inflate custom tooltip
                    // Goes right to info page for now.
                    startNewFragment(new InfoPage());

                }

            }

        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.whoops_message), Toast.LENGTH_SHORT).show();
            Log.d("NULLLIST", "mMarkerElementsList is null. Error in onMarkerSelected");
        }
    }

    /**
     * Starts a version of the map with only a single Marker representing a single badge selected by
     * the user.
     */
    public void startMarkerMapFragment() {

        setViewBarVisibility(false);
        setTitle(mActiveElement.getName());
        mMarkerElementsList = new ArrayList<>();
        createMarkerList(mActiveElement);
        //createMarkers();
        startMapFragment();

    }

    /**
     * Starts a version of the map populated with Markers for each Element in the currently active
     * collection.
     */
    public void startCollectionMapFragment() {

        setViewBarVisibility(true);
        setTitle(mActiveCollection.getName());
        mMarkerElementsList = new ArrayList<>();
        createCollectionMarkerList(mActiveCollection);
        //createMarkers();
        startMapFragment();

    }

    /**
     * Starts a version of the map populate with Markers for each Element in each Collection the
     * user is participating in.
     */
    public void startAllCollectionMapFragment() {

        setViewBarVisibility(false);
        setTitle(getString(R.string.toolbar_badges_near_me));
        mMarkerElementsList = new ArrayList<>();
        createAllCollectionMarkerList();
        //createMarkers();
        startMapFragment();

    }

    /**
     * Changes the fragment currently active in the fragment container
     * @param fr the fragment to be started.
     */
    public void startNewFragment(Fragment fr) {

        if (fr.getClass().getSimpleName().equals(BadgePage.class.getSimpleName())) {

            setViewBarVisibility(true);

        } else {

            setViewBarVisibility(false);

        }

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fr).commit();

    }

    /**
     * Toggles visibility of the view bar
     * @param visible determines visibility of the view bar.
     */
    public void setViewBarVisibility(boolean visible) {

        if (visible) {

            mViewBar.setVisibility(View.VISIBLE);

        } else {

            mViewBar.setVisibility(View.GONE);

        }

    }

    /**
     * Starts the Map fragment.
     */
    public void startMapFragment() {

        MapFragment mapFragment = new MapFragment();
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, mapFragment).commit();
        mapFragment.getMapAsync(this);

    }

    public void setActiveCollection(Collection collection) {
        mActiveCollection = collection;
    }

    public ArrayList<Collection> getCollections() {
        return mCollections;
    }

    public void setCollections(ArrayList<Collection> collections) {
        this.mCollections = collections;
    }

    public void addCollection(Collection collection) {
        this.mCollections.add(collection);
    }

    public void addNewCollections(ArrayList<Collection> newCollections) {
        this.mCollections.addAll(newCollections);
    }

    public void setTitle(String title) {
        mTitleBar.setTitle(title.toUpperCase());
    }

    public Collection getActiveCollection(){return mActiveCollection;}

    //@Override TODO: not needed?
    public void setAddedCollections(ArrayList<Collection> added) {
        addNewCollections(added);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void setNavButtonsEnabled(boolean enabled) {

        ((ImageButton) findViewById(R.id.map_nav_button)).setEnabled(enabled);
        ((ImageButton) findViewById(R.id.qr_nav_button)).setEnabled(enabled);
        ((ImageButton) findViewById(R.id.home_nav_button)).setEnabled(enabled);
        ((ImageButton) findViewById(R.id.search_nav_button)).setEnabled(enabled);
        ((ImageButton) findViewById(R.id.account_nav_button)).setEnabled(enabled);

    }

    public boolean getReadFromFile(){return mReadFromFile;}

    public void setReadFromFile(){mReadFromFile = true;}

    public RelativeLayout getViewBar(){return mViewBar;}

    /*
    @Override
    public void onBackPressed() {



        switch (getSupportFragmentManager().getFragments().get(0).getId()) {

            case R.id.fragment_local_loc:
                super.onBackPressed();
                break;

            case R.id.fragment_obtainable_loc:
                b.putSerializable("CurrentCollections", localCollections);
                Fragment fr = new Obtainable_loc();
                fr.setArguments(b);
                android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.Nav_Activity, fr);
                ft.commit();
                break;
        }
    }
    */
}
