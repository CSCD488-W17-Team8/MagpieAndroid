package com.magpie.magpie;

import android.app.DownloadManager;
import android.app.IntentService;
import android.app.LauncherActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.magpie.magpie.CollectionUtils.*;

/*
 * Class Local_loc: The programmatic representation of the Local List of Collections, Local_loc mainly handles the path to
 * Obtainable_loc, and to BadgePage classes. The main functions of the app handled here are the saving feature as well as the
 * starting point for the image download.
 *
 */

public class Local_loc extends Fragment implements View.OnClickListener{
    FloatingActionButton saveToFile;
    FileOutputStream readCollectionsFromFile;
    ArrayList<Collection> localCollections;
    ArrayList<Collection> addedFromCMS;
    FloatingActionButton obtainable_loc_Start;
    ListView localList;
    ArrayAdapter<String> localAdapter;
    ArrayList<String> addToList;
    ArrayList<Bitmap> images;
    boolean removeMode;
    Button removeButton;
    final String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            Bundle b = getArguments();
            //collections = (ArrayList<Collection>) b.getSerializable("CollectionList");

            // BEGIN: ADDED BY SEAN 5/2/2017
            try {
                ((NavActivity) getActivity()).setCollections((ArrayList<Collection>) b.getSerializable("CollectionList"));
            } catch (ClassCastException cce) {
                Log.d("ClassCastException", cce.getMessage());
            }
            // END: ADDED BY SEAN 5/2/2017
            addedFromCMS = (ArrayList<Collection>) b.getSerializable("NewlyAddedCollections");
        }

        // BEGIN: ADDED BY SEAN 5/2/2017

        // END: ADDED BY SEAN 5/2/2017
    }

    /*
     * Method fillTable: Handles the display associated with all collections currently available to the user. For-each is used to
     * properly update the adapter for the ListView.
     *
     */

    public void fillTable(){
        try {
            String visual;
            for (Collection co : localCollections) {
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
     * Method onClick: Determines which button has been clicked. Associated buttons are the toObtainable button, sending the
     * user to the Obtainable_loc class, the save button, writing all relevant Collection data to a text file, and the remove
     * button, which allows the user to remove any collection they wish to.
     *
     */

    @Override
    public void onClick(View view) {
        //Sends the user to the Obtainable_loc class.
        if(view.getId() == R.id.ToObtainable) {
            try {
                obtainable_loc_Start.hide();
                saveToFile.hide();
                Bundle b = new Bundle();
                b.putSerializable("CurrentCollections", localCollections);
                Fragment fr = new Obtainable_loc();
                fr.setArguments(b);
                android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.Nav_Activity, fr);
                ft.commit();
            } catch (Exception e){
                Log.d("Stuff", "Error: ", e);
            }
        }
        //Saves all current collections to a text file.
        else if(view.getId() == R.id.saveButton){
            File f = new File(getActivity().getFilesDir(), "SavedCollections.txt");
            try {
                if (f.exists()) {
                    readCollectionsFromFile = getActivity().openFileOutput("SavedCollections.txt", Context.MODE_PRIVATE);
                    for(Collection c : localCollections){
                        readCollectionsFromFile.write(c.toString().getBytes());
                        readCollectionsFromFile.write("%%%".getBytes());
                    }
                    readCollectionsFromFile.close();
                    Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
                }
                /*else if(f.exists() && localCollections.size() == 0){
                    f.delete();
                }*/
                else {
                    f.createNewFile();
                    readCollectionsFromFile = getActivity().openFileOutput("SavedCollections.txt", Context.MODE_PRIVATE);
                    readCollectionsFromFile.write("".getBytes());
                    for(Collection c : localCollections){
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
        File f = new File(getActivity().getFilesDir(), "SavedCollections.txt");
        try {
            if (f.exists() && f.length() != 0) {
                InputStream fin = getActivity().openFileInput("SavedCollections.txt");
                InputStreamReader inRead = new InputStreamReader(fin);
                BufferedReader bufRead = new BufferedReader(inRead);
                String s = bufRead.readLine();
                String[] allColl = s.split("%%%");
                for(String coll : allColl){
                    localCollections.add(new Collection(coll));
                }
            }
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
        View v = inflater.inflate(R.layout.activity_local_loc, container, false);
        obtainable_loc_Start = (FloatingActionButton) v.findViewById(R.id.ToObtainable);
        addToList = new ArrayList<>();
        localList = (ListView) v.findViewById(R.id.list);
        localAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, addToList);
        localList.setAdapter(localAdapter);
        obtainable_loc_Start.setOnClickListener(this);
        saveToFile = (FloatingActionButton) v.findViewById(R.id.saveButton);
        saveToFile.setOnClickListener(this);
        if(addedFromCMS != null){
            downloadZIPs();
        }
        if(localCollections == null){
            localCollections = new ArrayList<>();
            if(addedFromCMS != null){
                localCollections.addAll(addedFromCMS);
            }
            readFile();
            fillTable();
        }
        else {
            fillTable();
        }
        localList.setOnItemClickListener(localListItemClick);
        removeButton = (Button)v.findViewById(R.id.Removebutn);
        removeButton.setOnClickListener(this);
        return v;
    }

    /*
     * Method downloadZIPs: Prepares the downloading of the zip files and starts the service to download.
     *
     */

    private void downloadZIPs() {
        //The idea here is to construct zip files that contain the CID of the associated Collection within the zip file's name.
        //This is mainly because we are guaranteed that the CID will be unique amongst all the data within the JSON sent.
        ArrayList<Integer> CIDs = new ArrayList<>(addedFromCMS.size());
        for(int i = 0; i < addedFromCMS.size(); i++){
            CIDs.add(addedFromCMS.get(i).getCID());
        }
        Intent download = new Intent(getContext(), ZIPDownload.class);
        download.putIntegerArrayListExtra("TheCIDs", CIDs);
        getContext().startService(download);
    }

    /*
     * OnItemClickListener localListItemClick: Handles what happens when a particular item in the ListView is selected.
     * The boolean removeMode is used to determine whether or not the user is attempting to remove a Collection or not.
     * Otherwise, the user is sent to the Badge Page of the selected Collection. The user will be warned about removing
     * a Collection.
     *
     */
    AdapterView.OnItemClickListener localListItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
            //String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
            //We need to access the location of the downloaded zip files and determine if they do indeed exist.
            String [] downloadFiles = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).list();
            for (String zipName : downloadFiles) {
              /*Should we find a match, the Collection will be ready for use by the user.
                As such, we also want to store the path to the zip file for later use.
                To do that, we want to build the path here and store it in the Collection object.*/
                if (zipName.compareTo("imagesCID" + localCollections.get(i).getCID() + ".zip") == 0) {
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
                        public void onClick(DialogInterface dialogInterface, int which) {
                            localAdapter.remove(addToList.get(i));
                            localCollections.remove(i);
                        }
                    });
                    builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {

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
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, fr).commit();
                    }
                    catch(Exception e){
                        Log.d("Error: ", e.getMessage());
                    }
                }
            }
            else {
                Toast.makeText(getContext(), "The selected Collection has not finished downloading images.", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void addImagesToElements(int index){
        try {
            ZipFile zipImages = new ZipFile(localCollections.get(index).getPicZip());
            ZipEntry firstImage = zipImages.entries().nextElement();
            InputStream zin = zipImages.getInputStream(firstImage);
            Bitmap bm = BitmapFactory.decodeStream(zin);
            for(Element element : localCollections.get(index).getCollectionElements()){
                element.setBadge(bm);
            }
        }
        catch (Exception e){
            Log.d("ZIPREADINGERROR", e.getMessage());
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }
}