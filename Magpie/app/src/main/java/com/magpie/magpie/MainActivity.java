package com.magpie.magpie;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;


public class MainActivity extends AppCompatActivity {

    private GoogleApiClient mGoogleApiClient;

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

        Button sessionTestButton = (Button)findViewById(R.id.sessionTestButton);
        Button mapTestButton = (Button)findViewById(R.id.mapViewTestButton);

        SignInButton googleSignInButton = (SignInButton)findViewById(R.id.sign_in_button);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, null)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        Button signOutButton = (Button)findViewById(R.id.sign_out_button);


    }

    public void onClick(View v) {

        Intent i;

        switch (v.getId()) {
            case R.id.sessionTestButton:
                Toast.makeText(getApplicationContext(), "Starting session test", Toast.LENGTH_SHORT).show();

                //Fragment fr = new Local_loc();
                //android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
                //android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
                //ft.replace(R.id.Main_Activity, fr);
                //ft.commit();

                i = new Intent(v.getContext(), NavActivity.class);
                startActivity(i);
                finish();
                break;
            case R.id.mapViewTestButton:
                Toast.makeText(getApplicationContext(), "Starting Map test", Toast.LENGTH_SHORT).show();
                i = new Intent(v.getContext(), MapsActivity.class);
                //Bundle b = new Bundle();
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
                //...
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 9001) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {

        //Log.d(TAG, "handleSignInResult:" + result.isSuccess()); TODO: look in to Log
        if (result.isSuccess()) {
            // Signed in successfully, take authentication action
            GoogleSignInAccount acct = result.getSignInAccount();
            String idToken = acct.getIdToken();
            // TODO: send token to server and validate server-side
            //beginSession(); Not ready for this here yet TODO: test sign-in functionality first
        }
    }

    private void beginSession() {

        Intent i = new Intent(this, NavActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
