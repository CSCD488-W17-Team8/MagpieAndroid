package com.example.jacob.share_camera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

public class ShareActivity extends AppCompatActivity implements View.OnClickListener
{

    static final int REQUEST_IMAGE_CAPTURE = 1;

    ImageView upload_image;
    TextView dialogBox;
    Button btn_pic;
    RadioButton rbtn_twit;
    RadioButton rbtn_fb;
    RadioButton tbtn_instg;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);



        upload_image = (ImageView)findViewById(R.id.upload_image);
        dialogBox = (TextView)findViewById(R.id.enter_textview);

        btn_pic = (Button)findViewById(R.id.btn_camera);
        btn_pic.setOnClickListener(this);



    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            upload_image.setImageBitmap(imageBitmap);
        }
    }

    @Override
    public void onClick(View v)
    {

        if(v.getId() == btn_pic.getId())
        {
            dispatchTakePictureIntent();
            Intent tweet = new Intent(Intent.ACTION_VIEW);
            String message = String.valueOf(dialogBox.getText());
            tweet.setData(Uri.parse("http://twitter.com/?status=" + Uri.encode(message)));//where message is your string message
            startActivity(tweet);
        }
    }

    public void sendTweet(View v) {

    }
}
