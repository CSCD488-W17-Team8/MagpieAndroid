package com.magpie.magpie;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class MenuActivity extends AppCompatActivity implements AdapterView.OnItemClickListener
{

    DrawerLayout drawer_layout;
    ListView list_view;
    String[] menu_titles;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        drawer_layout = (DrawerLayout)findViewById(R.id.drawer_layout);
        list_view = (ListView)findViewById(R.id.left_drawer);
        int[] array = getResources().getIntArray(R.array.Icon_list);

        menu_titles = getResources().getStringArray(R.array.Nav_list);
        MenuAdapter adapter = new MenuAdapter(this, R.layout.nav_list_row,
                R.id.menu_title, menu_titles);
        adapter.addAll();


        list_view.setAdapter(adapter);
        list_view.setOnItemClickListener(this);




    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {

        if(menu_titles[position].compareTo(menu_titles[0]) == 0)
        {
            Toast.makeText(getApplicationContext(), menu_titles[0], Toast.LENGTH_SHORT).show();
        }
        else if(menu_titles[position].compareTo(menu_titles[1]) == 0)
        {
            Toast.makeText(getApplicationContext(), menu_titles[1], Toast.LENGTH_SHORT).show();
        }
        else if(menu_titles[position].compareTo(menu_titles[2]) == 0)
        {
            Toast.makeText(getApplicationContext(), menu_titles[2], Toast.LENGTH_SHORT).show();
        }
        else if(menu_titles[position].compareTo(menu_titles[3]) == 0)
        {
            Toast.makeText(getApplicationContext(), menu_titles[3], Toast.LENGTH_SHORT).show();
        }
        else if(menu_titles[position].compareTo(menu_titles[4]) == 0)
        {
            Toast.makeText(getApplicationContext(), menu_titles[4], Toast.LENGTH_SHORT).show();
        }
        else if(menu_titles[position].compareTo(menu_titles[5]) == 0)
        {
            Toast.makeText(getApplicationContext(), menu_titles[5], Toast.LENGTH_SHORT).show();
        }


    }
}
