package com.magpie.magpie;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private GoogleApiClient mGoogleApiClient;
    private int loc_permissionCheck,cam_permissionCheck;

    private final int MY_PERMISSIONS_REQUEST_CAMERA = 666,
            MY_REQUEST_PERMISSIONS_REQUEST_FINE_LOCATION = 42;

    private TextView mErrorTextView;

    /**
     * Initializes the first view in the app for the user. User will be presented with the login
     * screen, from which they will be directed to other views in the app.
     * @param savedInstanceState the saved instance state from which any necessary data will be
     *                           restored.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mErrorTextView = (TextView)findViewById(R.id.error_text_view);

        Button sessionTestButton = (Button)findViewById(R.id.sessionTestButton); // TODO: remove when testing is done
        Button mapTestButton = (Button)findViewById(R.id.mapViewTestButton); // TODO: remove when testing is done
        sessionTestButton.setOnClickListener(this);
        mapTestButton.setOnClickListener(this);

        SignInButton googleSignInButton = (SignInButton)findViewById(R.id.sign_in_button);
        googleSignInButton.setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, null)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        Button signOutButton = (Button)findViewById(R.id.sign_out_button); // TODO: remove when testing is done

    }

    @Override
    public void onClick(View v) {

        Intent i;

        switch (v.getId()) {

            case R.id.sessionTestButton: // TODO: ensure other fragments work well with NavActivity
                Toast.makeText(getApplicationContext(), "Starting session test", Toast.LENGTH_SHORT).show();
                i = new Intent(v.getContext(), NavActivity.class);
                startActivity(i);
                finish();
                break;

            case R.id.mapViewTestButton: // TODO: remove when testing is done
                Toast.makeText(getApplicationContext(), "Starting Map test", Toast.LENGTH_SHORT).show();
                i = new Intent(v.getContext(), NavActivity.class);
                i.putExtra("MAP_TEST", true);
                startActivity(i);
                finish();
                break;

            case R.id.sign_in_button:
                signIn();
                break;

            case R.id.sign_out_button:
                signOut();
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, 9001); // figure out what the 9001 is for; defined as RC_SIGN_IN in the example
    }

    /**
     * Temporary location for this
     * // TODO: move to appropriate location once working.
     */
    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                // TODO: implement sign out for testing purposes.
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 9001) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        } else {
            mErrorTextView.setText("Something went wrong!");
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {

        Log.d("Action: ", "handleSignInResult: " + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, take authentication action
            //GoogleSignInAccount acct = result.getSignInAccount();
            mErrorTextView.setTextColor(Color.BLACK);

            try {
                // Try to connect to the web server sign

                HttpURLConnection conn = (HttpURLConnection) new URL("http://magpiehunt.com/api/user/app").openConnection();

            } catch (Exception e) {
                Log.d("Error: ", e.getMessage());
            }

            // TODO: send token to server and validate server-side
            //beginSession(); Not ready for this here yet TODO: test sign-in functionality first
        } else {
            // Sign in failed
            mErrorTextView.setText("Sign-in failed!!");
        }
    }
}
