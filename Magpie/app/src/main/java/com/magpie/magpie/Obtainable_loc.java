package com.magpie.magpie;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.SearchView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import com.magpie.magpie.CollectionUtils.*;

public class Obtainable_loc extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener{
    ArrayList<Collection> collection;
    ArrayList<Collection> added;
    ArrayList<Collection> fromFile;
    Bundle collBundle;
    Button sendAll;
    Button cancel;
    ImageView iv;
    String collectionsToElements;
    String strBadges;
    Bitmap bm;
    JSONArray cmsColl;
    ListView collectionDisplay;
    ExpandableListView obtainExpandList;
    ArrayAdapter<String> obtainable_loc_Adapter;
    Spinner sort;
    ArrayList<String> allColl;
    CustomExpandableListAdapterObtainable celao;
    SearchView searchList;
    NavActivity navActivity;

    /*
     * Method onCreate: Handles the sending in of the Collections that are on the local side.
     * If there is no Collections locally, it sends in null.
     *
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navActivity = (NavActivity)getActivity();
        navActivity.setTitle(getString(R.string.toolbar_add_collection));
    }

    /*
     * Method parseJSON: Creates a display of the pulled JSON data for the user to make a decision.
     * More specifically, it creates the String visual of pertinent data, and the adapter is called to notify of a change.
     *
     */

    private void parseJSON() {
        try {
            Collection c;
            JSONObject j;
            for (int i = 0; i < cmsColl.length(); i++) {
                j = cmsColl.getJSONObject(i);
                if (j.getInt("Status") == 1 && checkCID(j.getInt("CID"))) { //(0 is false, 1 is true) && (ensuring that a collection isn't already on the system).
                    c = new Collection(j);
                    c.addBitmap(bm);
                    collection.add(c);
                    String fin = j.getString("CID") + " : " + j.getString("Name") + "\r\n";
                    fin += j.getString("Description") + " (" + j.getString("Rating") + ")";
                    allColl.add(fin);
                    //obtainable_loc_Adapter.notifyDataSetChanged();

                }
            }
            celao.notifyDataSetChanged();
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /*
     * Method checkCID: Handles the checking of the JSON data from the CMS against the passed in Collections from the local device.
     * If there are no Collections locally, this method will not be called.
     *
     */

    private boolean checkCID(int CID) {
        if (navActivity.getCollections().size() != 0) {
            for (Collection exist : navActivity.getCollections()){
                if(exist.getCID() == CID){
                    return false;
                }
            }
        }
        return true;
    }

    /*
     * BroadcastReceiver br: Accepts the JSON data from the initial pull from the CMS.
     * It creates a JSONArray from the String passed and it is sent to the method parseJSON.
     *
     */

    private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        if(intent.hasExtra("JSONFromCMS")) {
            try {
                cmsColl = new JSONArray(intent.getStringExtra("JSONFromCMS"));
                parseJSON();
            }
            catch(JSONException e){
                e.printStackTrace();
            }
        /*Intent parseIntent = new Intent(Obtainable_loc.this, JSONParse.class);
        parseIntent.putExtra("JSON", json);
        Obtainable_loc.this.startService(parseIntent);*/

        }
    }};

    /*
     * BroadcastReceiver ebr: Handles the obtaining of all pertinent text data of all selected Collections.
     * Each separate Collection's badges are divided by a "%", which is then used to create the Elements associated with the Collection.
     *
     */

    private BroadcastReceiver ebr = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                strBadges = intent.getStringExtra("CollectionElements");
                String[] badgeArr = strBadges.split("%");
                createElements(badgeArr);
                if(isAdded()) {
                    navActivity.setNavButtonsEnabled(true);
                }
                else{
                    Toast.makeText(getContext(), "There was an error adding a collection", Toast.LENGTH_SHORT).show();
                }
            }
            catch(Exception e){
                Log.d("UNIQUERECEIVE", e.getMessage());
            }
        }
    };

    /*
     * Method onNavButtonClicked: Handles the separate button clicks that can happen --
     * Cancel: Returns the user to the local list
     * Apply: adds all selected Collections to the local side.
     *
     */

    @Override
    public void onClick(View view) {

        if(view.getId() == cancel.getId()){
            Fragment fr = new Local_loc();
            navActivity.startNewFragment(fr);
        }
        else if(((String)(view.getTag())).compareTo("Apply") == 0) {
            try {
                if (added.size() != 0) {
                    for (Collection i : added) {
                        collectionsToElements += i.getCID() + ",";
                    }
                    Intent collIntent = new Intent(getContext(), JSONElements.class);
                    collIntent.putExtra("SelectedCollectionCIDs", collectionsToElements);
                    getContext().startService(collIntent);
                }
            }
            catch (Exception e){
                Log.d("PULLERROR", e.getMessage());
            }
        }
        /*
        else if(((String)(view.getTag())).compareTo("Send") == 0) {
            Fragment fr = new Local_loc();
            Bundle coll = new Bundle();
            if (added.size() != 0) {
                coll.putSerializable("CollectionList", added);
            }
            fr.setArguments(coll);
            android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.Nav_Activity, fr);
            ft.commit();
        }
        */
    }

    /*
     * Method createElements: Creates a JSONArray from all of the passed in Strings and creates the Elements.
     * Each element is sent to the ArrayList in collection, where it is added to the ArrayList for the associated Collection.
     *
     */

    private void createElements(String[] badgeArr) {
        try {
            int x = 0;
            for(int k = 0; k < badgeArr.length; k++) {
                JSONArray ja = new JSONArray(badgeArr[k]);
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject json = ja.getJSONObject(i);
                    int number = json.getInt("CollectionID");
                    Collection coll;
                    while (number != collection.get(x).getCID()) {
                        coll = collection.get(x);
                        if(number != collection.get(x).getCID())
                            x++;
                    }
                    collection.get(x).addElement(json);
                }
            }
            navActivity.addCollection(collection.get(x));
        }
        catch(Exception e){
            Log.v("CREATEELEMENTSEXCEP", e.getMessage());
        }
    }

    /*
     * Method onCreateView: Creates the visual for the user to interact with. Mostly handled here is the instantiation of variables.
     *
     */

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int writePermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        if(writePermission == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        if(readPermission == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
        View v = inflater.inflate(R.layout.obtainable_loc, container, false);
        super.onCreate(savedInstanceState);
        Intent getIntent = new Intent(getContext(), JSONGet.class);
        getContext().startService(getIntent);
        Intent intent = getActivity().getIntent();
        added = new ArrayList<>();
        if (intent.hasExtra("LocalCollections")) {
            fromFile = (ArrayList<Collection>) intent.getSerializableExtra("Collections");
        }
        //collectionDisplay = (ListView) v.findViewById(R.id.listView);
        cancel = (Button) v.findViewById(R.id.Return);
        LocalBroadcastManager.getInstance(v.getContext()).registerReceiver(br, new IntentFilter("FromCMS"));
        LocalBroadcastManager.getInstance(v.getContext()).registerReceiver(ebr, new IntentFilter("Elements"));
        obtainExpandList = (ExpandableListView) v.findViewById(R.id.ObtainableExpandList);
        searchList = (SearchView) v.findViewById(R.id.SearchList);
        setUpSearch();
        cancel.setOnClickListener(this);
        collection = new ArrayList<>();
        collectionsToElements = "";
        collBundle = new Bundle();
        allColl = new ArrayList<>();
        sort = (Spinner) v.findViewById(R.id.SortByDropDown);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this.getContext(), R.array.ObtainSort, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sort.setAdapter(spinnerAdapter);
        sort.setOnItemSelectedListener(this);
        Display d = getActivity().getWindowManager().getDefaultDisplay();
        Point p = new Point();
        d.getSize(p);
        celao = new CustomExpandableListAdapterObtainable(this, collection, p.x);
        obtainExpandList.setAdapter(celao);
        obtainExpandList.setIndicatorBounds(obtainExpandList.getRight() + 1150, obtainExpandList.getWidth());
        obtainExpandList.setOnChildClickListener(childClick);
        return v;
    }

    private void setUpSearch() {
        searchList.setIconifiedByDefault(false);
        searchList.setOnQueryTextListener(queryListener);
        searchList.setSubmitButtonEnabled(true);
        searchList.setQueryHint("City or State");
    }

    SearchView.OnQueryTextListener queryListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            celao.filterData(query);
            return true;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            celao.filterData(newText);
            return true;
        }
    };

    ExpandableListView.OnChildClickListener childClick = new ExpandableListView.OnChildClickListener() {
        @Override
        public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
            String strAdded = " added";
            String strRemoved = " removed";
            Collection c = collection.get(i);
            try {
                navActivity.setNavButtonsEnabled(false);
                Intent collIntent = new Intent(getContext(), JSONElements.class);
                collIntent.putExtra("SelectedCollectionCIDs", c.getCID() + "");
                getContext().startService(collIntent);
            }
            catch (Exception e){
                Log.d("PULLERROR", e.getMessage());
            }

            return true;
        }
    };

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(position == 0){
            sortListByNumberOfBadges();
        }
        else if(position == 1){
            sortListByCollectionLength();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void sortListByNumberOfBadges(){
        Collection[] sortTemp = new Collection[collection.size()];
        Object[] tempArray = collection.toArray();
        for(int i = 0; i < tempArray.length; i++){
            sortTemp[i] = (Collection)tempArray[i];
        }
        Collection cur;
        Collection next;
        int leftToSort = collection.size();
        while(leftToSort > 0){
            for(int i = 1; i < leftToSort; i++) {
                cur = sortTemp[i];
                next = sortTemp[i - 1];
                if (cur.getElementTotal() < next.getElementTotal()) {
                    Collection temp = cur;
                    sortTemp[i] = next;
                    sortTemp[i - 1] = temp;
                }
            }
            leftToSort--;
        }
        collection.clear();
        for(Collection c : sortTemp){
            collection.add(c);
        }
        celao.notifyDataSetChanged();
    }

    public void sortListByCollectionLength(){
        Collection[] sortTemp = new Collection[collection.size()];
        Object[] tempArray = collection.toArray();
        for(int i = 0; i < tempArray.length; i++){
            sortTemp[i] = (Collection)tempArray[i];
        }
        Collection cur;
        Collection next;
        int leftToSort = collection.size();
        while(leftToSort > 0){
            for(int i = 1; i < leftToSort; i++) {
                cur = sortTemp[i];
                next = sortTemp[i - 1];
                if (cur.getDistance() < next.getDistance()) {
                    Collection temp = cur;
                    sortTemp[i] = next;
                    sortTemp[i - 1] = temp;
                }
            }
            leftToSort--;
        }
        collection.clear();
        for(Collection c : sortTemp){
            collection.add(c);
        }
        celao.notifyDataSetChanged();
    }

}

//obtainable_loc_Adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, allColl)//{
        /*@Override
        public View getView(int position, View convertView, ViewGroup parent){
            Vi
        }
    //}*/;
//collectionDisplay.setAdapter(obtainable_loc_Adapter);
        /*collectionDisplay.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (added.size() == 0) {
                    Collection c = collection.get(i);
                    added.add(c);
                    view.setBackgroundColor(Color.GREEN);
                    sendAll.setEnabled(true);
                } else if (added.contains(collection.get(i))) {
                    added.remove(collection.get(i));
                    if(added.size() == 0)
                        sendAll.setEnabled(false);
                    view.setBackgroundColor(Color.WHITE);
                }
                else{
                    added.add(collection.get(i));
                    view.setBackgroundColor(Color.GREEN);
                }
*/
