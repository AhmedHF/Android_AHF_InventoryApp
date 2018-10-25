package com.ahf.inventoryapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.ahf.inventoryapp.Data.AndroidDatabaseManager;
import com.ahf.inventoryapp.Data.InventoryDBHelper;
import com.ahf.inventoryapp.Data.InventoryItem;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    ListView listView;
    InventoryDBHelper dbHelper;
    ArrayList<InventoryItem> inventoryItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddItem.class);
                startActivity(intent);
            }
        });
        dbHelper = new InventoryDBHelper(this);
        inventoryItems = dbHelper.getAllItems();
        listView = (ListView) findViewById(R.id.list);
        final ItemAdapter adapter = new ItemAdapter(this, inventoryItems);
        listView.setAdapter(adapter);

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
//                startActivity(intent);
//            }
//        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_settings:
                Intent intent = new Intent(MainActivity.this, AndroidDatabaseManager.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        inventoryItems = dbHelper.getAllItems();
        final ItemAdapter adapter = new ItemAdapter(this, inventoryItems);
        listView.setAdapter(adapter);
    }
}
