package com.magpie.magpie;

import android.content.Intent;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.magpie.magpie.CollectionUtils.Collection;

public class MainActivity extends FragmentActivity { // changed to FragmentActivity from AppCompatActivity

    private GoogleApiClient mGoogleApiClient;

    /**
     * Initializes the first view in the app for the user. User will be presented with the login
     * screen, from which they will be directed to other vieews in the app.
     * @param savedInstanceState the saved instance state from which any necessary data will be
     *                           restored.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button sessionTestButton = (Button)findViewById(R.id.jsonTestViewButton);
        sessionTestButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "Beginning session test", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(v.getContext(), Local_loc.class);
                startActivity(i);
                finish();
            }
        });

        Button mapTestButton = (Button)findViewById(R.id.mapViewTestButton);
        mapTestButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "Beginning Map test", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(v.getContext(), MapsActivity.class);
                //Bundle b = new Bundle();
                startActivity(i);
                finish();
            }
        });

        Button loginButton = (Button)findViewById(R.id.loginTestButton);
        loginButton.setOnClickListener(new View.OnClickListener() {

            /**
             * Until a more secure login system is implemented, this button is used to test the app
             * in its intended functionality as if a user but when user credentials are not available.
             * NOT INTEDNED TO BE INCLUDED IN FINAL RELEASE
             * @param v
             */
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Not ready yet...", Toast.LENGTH_LONG).show();
            }
        });

        SignInButton googleSignInButton = (SignInButton)findViewById(R.id.sign_in_button);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, null)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
    }

    private void buttonOnClick(View v) {

        switch (v.getId()) {
            case R.id.loginTestButton:
                // do something
                break;
            case R.id.sign_in_button:
                // do something
                break;
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, 9001); // figure out what the 9001 is for; defined as RC_SIGN_IN in the example
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
