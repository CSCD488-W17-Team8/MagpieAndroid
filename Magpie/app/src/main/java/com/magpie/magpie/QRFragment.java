package com.magpie.magpie;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.Result;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import me.dm7.barcodescanner.zxing.ZXingScannerView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link QRFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link QRFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QRFragment extends Fragment implements ZXingScannerView.ResultHandler, View.OnClickListener, ActivityCompat.OnRequestPermissionsResultCallback
        {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private ZXingScannerView zXingScannerView;
    TextView resultsView;
    Boolean updated = false;
    String result_text = "Initial Point";
    private int cam_permissionCheck = 0;
    Button scan_button;
    private final int MY_PERMISSIONS_REQUEST_CAMERA = 666;
    IntentIntegrator integrator;


    public QRFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment QRFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static QRFragment newInstance(String param1, String param2) {
        QRFragment fragment = new QRFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_qr, container, false);
        Fragment fragment = this;
        resultsView = (TextView) view.findViewById(R.id.result_view);
        resultsView.setText("What the fuck Happened Bro!");
        scan_button = (Button) view.findViewById(R.id.btn_scan);
        scan_button.setOnClickListener(this);
       // zXingScannerView = new ZXingScannerView(this);
        integrator = new IntentIntegrator(getActivity());
        permissionCheck();
        checkAppPermissions();
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

            public void onActivityResult(int requestCode, int resultCode, Intent intent) {
                IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
                if (scanResult != null) {

                    // handle scan result

                    result_text = scanResult.getContents();
                    resultsView.setText(result_text);
                    Toast.makeText(getActivity(), result_text, Toast.LENGTH_SHORT).show();

                }
                // else continue with any other code you need in the method...
            }

            @Override
            public void onClick(View view)
            {
                if(cam_permissionCheck != PackageManager.PERMISSION_GRANTED)
                {   Toast.makeText(getActivity(), "Camera Permissions have been Turned Off", Toast.LENGTH_SHORT).show();    }
                else
                {
                    /**
                     setContentView(zXingScannerView);
                     zXingScannerView.setResultHandler(this);
                     zXingScannerView.startCamera();
                     */


                    integrator.setOrientationLocked(false);
                    integrator.initiateScan(integrator.QR_CODE_TYPES);
                    resultsView.setText(result_text);
                }
            }

            @Override
            public void onRequestPermissionsResult(int result_code, String[] permissions, int[] grantResults)
            {

                switch (result_code)
                {
                    case MY_PERMISSIONS_REQUEST_CAMERA:
                    {
                        // If request is cancelled, the result arrays are empty.
                        if (grantResults.length == 1
                                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                            // permission was granted, yay! Do the
                            // contacts-related task you need to do.

                        } else {

                            // permission denied, boo! Disable the
                            // functionality that depends on this permission.
                        }
                        return;
                    }
                    default:
                    {   super.onRequestPermissionsResult(result_code, permissions, grantResults);   }
                }


            }


            public void permissionCheck()
            {
                cam_permissionCheck = ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.CAMERA);
            }

            public void checkAppPermissions()
            {
                // checking the Camera and Access Fine-Location Permissions - Jacob

                // if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
                // {

                //}
                check_and_request(Manifest.permission.CAMERA,
                        cam_permissionCheck, MY_PERMISSIONS_REQUEST_CAMERA);
            }
///////////////////////////////////////////////////////////////////////////////

            /**
             *
             * If Permissions are denied, the component dependent on the permission
             * must be disabled in someway or another.
             *
             * Thus, there must be a way to enable the component when the Permissions
             * have been granted?
             *
             */



            ///////////////////////////////////////////////////////////////////////////////////
            public void check_and_request(String permission_type,
                                          int permission_check,
                                          int permission_callback)
            {


                if(permission_check != PackageManager.PERMISSION_GRANTED)
                {
                    // Should we show an explanation?



                    if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                            permission_type))
                    {
                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.
                    }
                    else
                    {
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{permission_type},
                                permission_callback);

                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }

                }
            }//end of the method


            protected void pause_reader()
            {
                super.onPause();
                zXingScannerView.stopCamera();
                getActivity().setContentView(R.layout.activity_qreader);
                if(updated)
                    resultsView.setText(result_text);

                updated = false;
            }

            @Override
            public void handleResult(Result result)
            {
                //Toast.makeText(getApplicationContext(), result.getText(), Toast.LENGTH_SHORT).show();
                result_text = result.getText();

                // zXingScannerView.resumeCameraPreview(this);
                onPause();


            }
}
