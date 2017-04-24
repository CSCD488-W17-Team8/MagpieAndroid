package com.magpie.magpie;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QReader extends AppCompatActivity implements ZXingScannerView.ResultHandler
{

    private ZXingScannerView zXingScannerView;

    //private static final int REQUEST_CAMERA_RESULT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qreader);
    }



    public void scan(View view)
    {
        zXingScannerView = new ZXingScannerView(getApplicationContext());
        setContentView(zXingScannerView);
        zXingScannerView.setResultHandler(this);
        zXingScannerView.startCamera();


    }



    protected void onPause()
    {
        super.onPause();
        zXingScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result)
    {
        Toast.makeText(getApplicationContext(), result.getText(), Toast.LENGTH_SHORT).show();
        zXingScannerView.resumeCameraPreview(this);
    }

    /**
       private void openCamera()
       {
         CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
         try
         {
             Log.v("CAMERA", mCameraId + " " + mCameraDeviceStateCallback);
             if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
             {
                 if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                 == PackageManager.PERMISSION_GRANTED)
                 {
                 cameraManager.openCamera(mCameraId, mCameraDeviceStateCallback,mBackgroundHandler);
                 }
                 else
                 {
                     if (shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA))
                        {
                        Toast.makeText(this,"No Permission to use the Camera services", Toast.LENGTH_SHORT).show();
                        }
                     requestPermissions(new String[] {android.Manifest.permission.CAMERA},REQUEST_CAMERA_RESULT);
                 }
             }
             else
             {
             cameraManager.openCamera(mCameraId, mCameraDeviceStateCallback, mBackgroundHandler);
             }
         }
         catch (CameraAccessException e)
         {
         e.printStackTrace();
         }
     }


     */

}
