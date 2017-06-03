package com.magpie.magpie.InfoAndShare;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//import com.twitter.sdk.android.core.TwitterAuthConfig;
//import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import com.magpie.magpie.NavActivity;
import com.magpie.magpie.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ShareFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ShareFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShareFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";



    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "TYzIIr7VSCgzD0Oii9fWfTBzJ";
    private static final String TWITTER_SECRET = "gvidwZAB4S71DPaEKcmF98HL5tQDOcwAggilkgijuP0yDAf1rg";
    static final int REQUEST_IMAGE_CAPTURE = 1;

    ImageView upload_image;
    Bitmap capturedImage;
    TextView dialogBox;
    Button btn_pic;
    Button btn_twit;
    Button btn_snpcht;
    Button btn_instg;

    NavActivity navActivity;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ShareFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShareFragment.
     */


    // TODO: Rename and change types and number of parameters
    public static ShareFragment newInstance(String param1, String param2)
    {
        ShareFragment fragment = new ShareFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        navActivity = (NavActivity) getActivity();
        if (getArguments() != null)
        {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_share, container, false);

        //TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        upload_image = (ImageView)view.findViewById(R.id.upload_image);
        dialogBox = (EditText)view.findViewById(R.id.text_dialog);

        if(navActivity.getUriPath() != null)
        {
            Toast.makeText(navActivity, "Picture is here ", Toast.LENGTH_SHORT).show();
            //capturedImage = navActivity.capturedImage;
            Picasso.with(getContext()).load(navActivity.getUriPath()).rotate(90f).into(upload_image);
        }
        else
        {   Toast.makeText(navActivity, "Picture was not saved to the nav act", Toast.LENGTH_SHORT).show();     }

       // upload_image.setImageBitmap(capturedImage);
        btn_pic = (Button)view.findViewById(R.id.btn_camera);
        btn_pic.setOnClickListener(this);

        btn_twit = (Button)view.findViewById(R.id.btn_twitter);
        btn_twit.setOnClickListener(this);

        btn_snpcht = (Button)view.findViewById(R.id.btn_snpcht);
        btn_snpcht.setOnClickListener(this);

        btn_instg = (Button)view.findViewById(R.id.btn_instgrm);
        btn_instg.setOnClickListener(this);

        //Fabric.with(this, new TwitterCore(authConfig), new TweetComposer());

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

    /** https://sites.google.com/site/androidhowto/how-to-1/detect-if-an-app-is-installed   */
    private boolean isAppInstalled(String uri) {
        PackageManager pm = getActivity().getPackageManager();
        boolean installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            installed = false;
        }
        return installed;
    }

    public void post_instagram()
    {

        String sharename = "The Magpie Application";
        String sharedescription = "This is a test";


        if(isAppInstalled("com.instagram.android")&& navActivity.getUriPath() != null)//&& capturedImage != null)
        {

            String type = "image/*";
            Uri bmpUri = getLocalBitmapUri(upload_image);

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/*");
            shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);

            shareIntent.setPackage("com.instagram.android");
            startActivity(shareIntent);

        }
        else
        {
            if(!isAppInstalled("com.instagram.android"))
                Toast.makeText(getActivity(), "Instagram is not Installed on this Device", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getActivity(), "No Image", Toast.LENGTH_SHORT).show();
        }

    }

    public void post_snapchat()
    {
        Toast.makeText(getActivity(), "Not Implemented", Toast.LENGTH_SHORT).show();
    }

    public void post_tweet()
    {

        Toast.makeText(getActivity(), "Need to Register this Device with Fabric", Toast.LENGTH_SHORT).show();
/**
        TweetComposer.Builder builder = new TweetComposer.Builder(getActivity()).text(dialogBox.getText().toString());
        if(capturedImage != null)
        {
            Uri uri = navActivity.getUriPath();
            builder.image(uri);
        }

        builder.show();
 */
    }
    /**     http://stackoverflow.com/questions/16300959/android-share-image-from-url */
    public Uri getLocalBitmapUri(Bitmap bmp)
    {
        Uri bmpUri = null;
        try {
            File file =  new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    public Uri getLocalBitmapUri(ImageView imageView) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable){
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            File file =  new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), "share_image_" + System.currentTimeMillis() + ".png");
            file.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }


    @Override
    public void onClick(View v)
    {
        if(v.getId() == btn_pic.getId())
        {   dispatchTakePictureIntent();    }
        else if(v.getId() == btn_instg.getId())
        {   post_instagram();   }
        else if(v.getId() == btn_twit.getId())
        {   post_tweet();    }
        else if(v.getId() == btn_snpcht.getId())
        {   post_snapchat();    }
    }


    /** SIMPLY NAVIGATES TO THE PICTURE FRAGMENT PAGE   */
    private void dispatchTakePictureIntent()
    {
        /**
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }

         */
        Fragment picFrag = new PictureFragment();
        navActivity.startNewFragment(picFrag);
    }

}
