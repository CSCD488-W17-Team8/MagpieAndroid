package com.magpie.magpie;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.magpie.magpie.Obtainable_loc;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import junit.framework.Test;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getTitle());
        setSupportActionBar(toolbar);

        Button loginButton = (Button)findViewById(R.id.loginButton);
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

                } else if(emailEditText.getText().toString().equals("zacharyadmin") &&
                        passwordEditText.getText().toString().equals("admin")) {
                    // Goes to Zachary's tester
                    Toast.makeText(getApplicationContext(), "Zachary admin logging in...", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(v.getContext(), Obtainable_loc.class);
                    startActivity(i);
                }

                // TODO: implement Google SSO and create more secure login system.
            }
        });
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
