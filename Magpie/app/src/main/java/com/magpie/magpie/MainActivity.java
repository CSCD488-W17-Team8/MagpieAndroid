package com.magpie.magpie;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.magpie.magpie.Obtainable_loc;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import junit.framework.Test;

public class MainActivity extends AppCompatActivity implements Local_loc.OnFragmentInteractionListener, BadgePage.OnFragmentInteractionListener {

    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setTitle(getTitle());
        //setSupportActionBar(toolbar);

        final Button loginButton = (Button)findViewById(R.id.loginButton);
        final EditText emailEditText = (EditText)findViewById(R.id.emailEditText);
        final EditText passwordEditText = (EditText)findViewById(R.id.passwordEditText);
        loginButton.setOnClickListener(new View.OnClickListener() {

            /**
             * Until a more secure login system is implemented, only admin passwords are used to
             * reach Views made for testing.
             * @param v
             */
            @Override
            public void onClick(View v) {

                if(emailEditText.getText().toString().equals("admin") &&
                        passwordEditText.getText().toString().equals("admin")) {
                    Toast.makeText(getApplicationContext(), "Admin logging in...", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(v.getContext(), MapsActivity.class);
                    startActivity(i);

                } else if(emailEditText.getText().toString().equals("zacharyadmin") &&
                        passwordEditText.getText().toString().equals("admin")) {
                    // Goes to Zachary's tester
                    emailEditText.setEnabled(false);
                    passwordEditText.setEnabled(false);
                    loginButton.setEnabled(false);
                    Toast.makeText(getApplicationContext(), "Zachary admin logging in...", Toast.LENGTH_LONG).show();
                    Fragment fr = new Local_loc();
                    android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
                    android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.Main_Activity, fr);
                    ft.commit();
                }

                // TODO: implement Google SSO and create more secure login system.
            }
        });

        SignInButton googleSignInButton = (SignInButton)findViewById(R.id.sign_in_button);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, null)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }

    private void buttonOnClick(View v) {

        switch (v.getId()) {
            case R.id.loginButton:
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

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
