package com.magpie.magpie;

import android.app.DownloadManager;
import android.app.IntentService;
import android.app.LauncherActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.magpie.magpie.CollectionUtils.*;
import com.magpie.magpie.UserProgress.SendProgress;

import org.json.JSONObject;

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
    ExpandableListView localExpandList;
    CustomExpandableListAdapterLocal celal;
    ArrayAdapter<String> localAdapter;
    ArrayList<String> addToList;
    ArrayList<Bitmap> images;
    boolean removeMode;
    Button removeButton;
    final String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
    private NavActivity navActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navActivity = (NavActivity) getActivity();
        navActivity.setTitle(getString(R.string.toolbar_my_collections));
        navActivity.setCollections(readFile());

        // BEGIN: ADDED BY SEAN 5/2/2017

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
            JSONObject jsonProgress = new JSONObject(progress);

        }
        catch (Exception e){
            Log.d("PARSEPROGRESSEXCEP", e.getMessage());
        }
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

            Intent intent = new Intent(getContext(), SendProgress.class);
            intent.putExtra("UserID", "test");
            intent.putExtra("UserProgressCollection", localCollections);
            getContext().startService(intent);
            /*File f = new File(getActivity().getFilesDir(), "SavedCollections.txt");
            try {
                if (f.exists()) {
                    readCollectionsFromFile = getActivity().openFileOutput("SavedCollections.txt", Context.MODE_PRIVATE);
                    for(Collection c : localCollections){
                        readCollectionsFromFile.write(c.toString().getBytes());
                        readCollectionsFromFile.write("÷÷÷".getBytes());
                    }
                    readCollectionsFromFile.close();
                    Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
                }
                /*else if(f.exists() && localCollections.size() == 0){
                    f.delete();
                }*/
               /* else {
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
            }*/
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
    private ArrayList<Collection> readFile(){
        ArrayList<Collection> fromFile = new ArrayList<>();
        File f = new File(getActivity().getFilesDir(), "SavedCollections.txt");
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
            return fromFile;
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return fromFile;
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
        LocalBroadcastManager.getInstance(v.getContext()).registerReceiver(pbr, new IntentFilter("ProgressFromCMS"));
        //localList = (ListView) v.findViewById(R.id.list);
        localExpandList = (ExpandableListView) v.findViewById(R.id.expandList);
        //localAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, addToList);
        //localList.setAdapter(localAdapter);
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
        celal = new CustomExpandableListAdapterLocal(this, localCollections);
        localExpandList.setAdapter(celal);
        //localList.setOnItemClickListener(localListItemClick);
        removeButton = (Button)v.findViewById(R.id.Removebutn);
        removeButton.setOnClickListener(this);
        localExpandList.setIndicatorBounds(localExpandList.getRight() + 1150, localExpandList.getWidth());
        localExpandList.setOnChildClickListener(childClick);
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

    ExpandableListView.OnChildClickListener childClick = new ExpandableListView.OnChildClickListener() {
        @Override
        public boolean onChildClick(ExpandableListView expandableListView, View view, final int i, int i1, long l) {
            //We need to access the location of the downloaded zip files and determine if they do indeed exist.
            String[] downloadFiles = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).list();
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
            if (localCollections.get(i).getDownloaded()) {
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
        try {
            obtainable_loc_Start.hide();
            saveToFile.hide();
            Collection send = localCollections.get(i);
            //navActivity.setCurrentCollection(send);
            Fragment fr = new BadgePage();
            //navActivity.nextFragment(fr);
        } catch (Exception e) {
            Log.d("Error: ", "There was a problem with the operation.");
        }
    }

    private void removeFromExpandList(final int i) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Remove");
        builder.setMessage("Are you sure you want to remove this Collection?");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                localCollections.remove(i);
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

    private void addImagesToElements(int index){
        try {
            ZipFile zipImages = new ZipFile(localCollections.get(index).getPicZip());
            Enumeration<? extends ZipEntry> entries = zipImages.entries();
            ZipEntry curImage = entries.nextElement();
            ZipEntry defaultImage = curImage;
            int count = 0;
            for(Element element : localCollections.get(index).getCollectionElements()){
                InputStream zin = zipImages.getInputStream(curImage);
                Bitmap bm = BitmapFactory.decodeStream(zin);
                element.setBadge(bm);
                if(entries.hasMoreElements()) {
                    curImage = entries.nextElement();
                    String elementName = curImage.getName();
                    String extension = elementName.substring(elementName.length() - 3);
                    if (extension.compareTo("zip") == 0) {
                        curImage = zipImages.entries().nextElement();
                    }
                }
                else{
                    curImage = defaultImage;
                }
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