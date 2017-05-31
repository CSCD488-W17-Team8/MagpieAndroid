package com.magpie.magpie.LocalList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
//import android.app.Fragment; // TODO: trying this out
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.zip.ZipFile;

import com.magpie.magpie.BadgePage.BadgePage;
import com.magpie.magpie.CollectionUtils.*;
import com.magpie.magpie.NavActivity;
import com.magpie.magpie.R;
import com.magpie.magpie.Services.UserProgress.GetProgress;
import com.magpie.magpie.Services.ZIPDownload;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/*
 * Class Local_loc: The programmatic representation of the Local List of Collections, Local_loc mainly handles the path to
 * Obtainable_loc, and to BadgePage classes. The main functions of the app handled here are the saving feature as well as the
 * starting point for the image download.
 *
 */

public class Local_loc extends Fragment implements View.OnClickListener{

    // Reference to containing activity
    private NavActivity navActivity;

    FloatingActionButton saveToFile;
    FileOutputStream readCollectionsFromFile;
    ExpandableListView localExpandList;
    CustomExpandableListAdapterLocal celal;
    ArrayAdapter<String> localAdapter;
    ArrayList<String> addToList;
    ArrayList<Bitmap> images;
    boolean removeMode;
    Button removeButton;
    final String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();



    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
            Log.d("NAVACTIVITYSIZEONCREATE", ((NavActivity)getActivity()).getCollections().size()+"");
        navActivity = (NavActivity) getActivity();
        navActivity.setTitle(getString(R.string.toolbar_my_collections));

        if(!navActivity.getReadFromFile()){
            readFile();
            navActivity.setReadFromFile();
            Intent intent = new Intent(getActivity(), GetProgress.class);
            //TODO: package up user's ID for check on CMS
            getActivity().startService(intent);
        }

        // BEGIN: ADDED BY SEAN 5/2/2017

        // TODO: debug this. Gets a NullPointerReference
        // END: ADDED BY SEAN 5/2/2017
    }

    BroadcastReceiver pbr = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String progress = intent.getStringExtra("ProgressJSONFromCMS");
            parseProgress(progress);
        }
    };

    private void parseProgress(String progress) {
        try {
            JSONObject collectionProgress;
            JSONArray jsonProgress = new JSONArray(progress);
            for(int i = 0; i < jsonProgress.length(); i++){
                collectionProgress = jsonProgress.getJSONObject(i);
                for(Collection c : navActivity.getCollections()){
                    if(c.getCID() == collectionProgress.getInt("cid")){
                        assignCollected(c, collectionProgress);
                    }
                }
            }
        }
        catch (Exception e){
            Log.d("PARSEPROGRESSEXCEP", e.getMessage());
        }
    }

    private void assignCollected(Collection c, JSONObject progress) {
        try {
            JSONArray landmarkIDs = progress.getJSONArray("landmarks");
            for(int i = 0; i < landmarkIDs.length(); i++) {
                int lid = landmarkIDs.getInt(i);
                for (Element e : c.getCollectionElements()) {
                    if(e.getLID() == lid){
                        e.isCollected();
                    }
                }
            }
        }
        catch (JSONException e){
            Log.d("ASSIGNCOLLECTEDEXCEP", "Data error");
        }
    }

    /*
     * Method fillTable: Handles the display associated with all collections currently available to the user. For-each is used to
     * properly update the adapter for the ListView.
     *
     */

    public void fillTable(ArrayList<Collection> toDisplay){
        try {
            String visual;
            for (Collection co : toDisplay) {
                //Remove the line below for the eventual addition of the download complete receiver.
                //co.setDownloaded();
                visual = co.getName() + "\r\n Number of Landmarks: " + co.getCollected() + " / " + co.getCollectionSize() +
                        "\r\n" + co.getDescription() + " (" + co.getRating() + ")";
                addToList.add(visual);
            }
        }
        catch (Exception e){
            Log.d("LOCALADAPTER", e.getMessage());
        }
    }

    /*
     * Method onNavButtonClicked: Determines which button has been clicked. Associated buttons are the toObtainable button, sending the
     * user to the Obtainable_loc class, the save button, writing all relevant Collection data to a text file, and the remove
     * button, which allows the user to remove any collection they wish to.
     *
     */

    @Override
    public void onClick(View view) {
        //Saves all current collections to a text file.
        if(view.getId() == R.id.saveButton){

        }
        //Activates remove mode.
        else if(view.getId() == R.id.Removebutn){
            if(removeMode) {
                removeMode = false;
                removeButton.setText("Remove");
            }else {
                removeMode = true;
                removeButton.setText("Finish");
            }
        }
    }

    /*
     * Method readFile: opens the existing collections file, aptly named 'SavedCollections.txt', and parses it for later use in
     * the app.
     *
     */
    private void readFile(){
        ArrayList<Collection> fromFile = new ArrayList<>();
        File f = new File(getActivity().getFilesDir(), "SavedCollections.txt");
        //f.delete();
        try {
            if (f.exists() && f.length() != 0) {
                InputStream fin = getActivity().openFileInput("SavedCollections.txt");
                InputStreamReader inRead = new InputStreamReader(fin);
                BufferedReader bufRead = new BufferedReader(inRead);
                String s = bufRead.readLine();
                String[] allColl = s.split("÷÷÷");
                for(String coll : allColl){
                    fromFile.add(new Collection(coll));
                }
            }
            navActivity.setCollections(fromFile);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    /*
     * Method onCreateView: Creates the UI for the user and prepares all possible interactions they can make. Of note here is the
     * check for anything on the file, as well as for whether or not the page is being created from the Obtainable_loc class.
     *
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        View v = inflater.inflate(R.layout.fragment_local_loc, container, false);
        addToList = new ArrayList<>();
        LocalBroadcastManager.getInstance(v.getContext()).registerReceiver(pbr, new IntentFilter("ProgressFromCMS"));
        //localList = (ListView) v.findViewById(R.id.list);
        localExpandList = (ExpandableListView) v.findViewById(R.id.expandList);
        //localAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, addToList);
        //localList.setAdapter(localAdapter);
        saveToFile = (FloatingActionButton) v.findViewById(R.id.saveButton);
        saveToFile.setOnClickListener(this);
        for(Collection c : navActivity.getCollections()){
            if(!c.getDownloaded())
                downloadZIPs();
        }
        fillTable(navActivity.getCollections());
        /*if(localCollections == null){
            localCollections = new ArrayList<>();
            if(addedFromCMS != null){
                localCollections.addAll(addedFromCMS);
            }
            fillTable();
        }
        else {
            fillTable();
        }*/
        celal = new CustomExpandableListAdapterLocal(this, navActivity.getCollections());
        localExpandList.setAdapter(celal);
        //localList.setOnItemClickListener(localListItemClick);
        removeButton = (Button)v.findViewById(R.id.Removebutn);
        removeButton.setOnClickListener(this);
        localExpandList.setIndicatorBounds(localExpandList.getRight() + 1150, localExpandList.getWidth());
        localExpandList.setOnChildClickListener(childClick);
        writeToFile();
        return v;
    }

    /*
     * Method downloadZIPs: Prepares the downloading of the zip files and starts the service to download.
     *
     */

    private void downloadZIPs() {
        //The idea here is to construct zip files that contain the CID of the associated Collection within the zip file's name.
        //This is mainly because we are guaranteed that the CID will be unique amongst all the data within the JSON sent.
        ArrayList<Integer> CIDs = new ArrayList<>(navActivity.getCollections().size());
        for(int i = 0; i < navActivity.getCollections().size(); i++){
            CIDs.add(navActivity.getCollections().get(i).getCID());
        }
        Intent download = new Intent(getContext(), ZIPDownload.class);
        download.putIntegerArrayListExtra("TheCIDs", CIDs);
        getContext().startService(download);
    }

    ExpandableListView.OnChildClickListener childClick = new ExpandableListView.OnChildClickListener() {
        @Override
        public boolean onChildClick(ExpandableListView expandableListView, View view, final int i, int i1, long l) {
            //We need to access the location of the downloaded zip files and determine if they do indeed exist.
            String[] downloadFiles = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).list();
            for (String zipName : downloadFiles) {
              /*Should we find a match, the Collection will be ready for use by the user.
                As such, we also want to store the path to the zip file for later use.
                To do that, we want to build the path here and store it in the Collection object.*/
                if (zipName.compareTo("imagesCID" + navActivity.getCollections().get(i).getCID() + ".zip") == 0) {
                    navActivity.getCollections().get(i).setDownloaded();
                    try {
                        navActivity.getCollections().get(i).setPicZip(new ZipFile(path + "/" + zipName));
                    }
                    catch (IOException ioe){
                        Log.d("ZIPFILEACCESS", ioe.getMessage());
                    }
                }
            }
            //Because we want the user to be prevented from accessing a Collection until we have access to images, we will have an if statement check for the completion of the download.
            if (navActivity.getCollections().get(i).getDownloaded()) {
                if (removeMode) {
                    removeFromExpandList(i);
                } else {
                    toBadgePage(i);
                }
            } else {
                Toast.makeText(getContext(), "The selected Collection has not finished downloading images.", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
    };

    private void toBadgePage(int i) {

        navActivity.setActiveCollection(navActivity.getCollections().get(i));
        navActivity.startNewFragment(new BadgePage());

        /*
        try {
            saveToFile.hide();
            Collection send = navActivity.getCollections().get(i);
            navActivity.setActiveCollection(send);
            Fragment fr = new BadgePage();
            navActivity.startNewFragment(fr);
        } catch (Exception e) {
            Log.d("Error: ", "There was a problem with the operation.");
        }
        */
    }

    private void removeFromExpandList(final int i) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Remove");
        builder.setMessage("Are you sure you want to remove this Collection?");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                navActivity.getCollections().remove(i);
                celal.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {

            }
        });
        builder.show();
    }

    private void writeToFile(){
        File f = new File(getActivity().getFilesDir(), "SavedCollections.txt");
        try {
            if (f.exists()) {
                readCollectionsFromFile = getActivity().openFileOutput("SavedCollections.txt", Context.MODE_PRIVATE);
                for(Collection c : navActivity.getCollections()){
                    readCollectionsFromFile.write(c.toString().getBytes());
                    readCollectionsFromFile.write("÷÷÷".getBytes());
                }
                readCollectionsFromFile.close();
                Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
            }
            else if(f.exists() && navActivity.getCollections().size() == 0){
                f.delete();
            }
            else {
                f.createNewFile();
                readCollectionsFromFile = getActivity().openFileOutput("SavedCollections.txt", Context.MODE_PRIVATE);
                readCollectionsFromFile.write("".getBytes());
                for(Collection c : navActivity.getCollections()){
                    readCollectionsFromFile.write(c.toString().getBytes());
                }
                readCollectionsFromFile.close();
                Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

}

    /*
     * OnItemClickListener localListItemClick: Handles what happens when a particular item in the ListView is selected.
     * The boolean removeMode is used to determine whether or not the user is attempting to remove a Collection or not.
     * Otherwise, the user is sent to the Badge Page of the selected Collection. The user will be warned about removing
     * a Collection.
     *
     */
/*AdapterView.OnItemClickListener localListItemClick = new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
        //String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        //We need to access the location of the downloaded zip files and determine if they do indeed exist.
        String [] downloadFiles = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).list();
        for (String zipName : downloadFiles) {
              /*Should we find a match, the Collection will be ready for use by the user.
                As such, we also want to store the path to the zip file for later use.
                To do that, we want to build the path here and store it in the Collection object.*/
/*            if (zipName.compareTo("imagesCID" + localCollections.get(i).getCID() + ".zip") == 0) {
                localCollections.get(i).setDownloaded();
                localCollections.get(i).setPicZip(path + "/" + zipName);
                addImagesToElements(i);
            }
        }
        //Because we want the user to be prevented from accessing a Collection until we have access to images, we will have an if statement check for the completion of the download.
        localCollections.get(i).setDownloaded();
        if(localCollections.get(i).getDownloaded()) {
            if (removeMode) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Remove");
                builder.setMessage("Are you sure you want to remove this Collection?");
                builder.setIcon(android.R.drawable.ic_dialog_alert);
                builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onNavButtonClicked(DialogInterface dialogInterface, int which) {
                        localAdapter.remove(addToList.get(i));
                        localCollections.remove(i);
                    }
                });
                builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onNavButtonClicked(DialogInterface dialogInterface, int which) {

                    }
                });
                builder.show();
            } else {
                try {
                    obtainable_loc_Start.hide();
                    saveToFile.hide();
                    Collection send = localCollections.get(i);
                    send.setSelected();
                    Bundle coll = new Bundle();
                    coll.putSerializable("TheCollection", send);
                    Fragment fr = new BadgePage();
                    fr.setArguments(coll);
                    android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
                    android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.Main_Activity, fr);
                    ft.commit();
                } catch (Exception e) {
                    Log.d("Error: ", "There was a problem with the operation.");
                }
            }
        }
        else {
            Toast.makeText(getContext(), "The selected Collection has not finished downloading images.", Toast.LENGTH_SHORT).show();
        }
    }
};*/
