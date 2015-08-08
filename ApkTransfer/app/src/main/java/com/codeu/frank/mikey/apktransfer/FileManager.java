package com.codeu.frank.mikey.apktransfer;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileManager extends ActionBarActivity {

    private String serverStatus;
    private String storagePath;
    public static final String TAG = "ApkTransfer";


    public FileManager() {
        serverStatus = null;
        storagePath = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_manager);

        serverStatus = getIntent().getStringExtra(getString(R.string.server_status_intent_bridge));
        storagePath = getIntent().getStringExtra("storagePath");

        Log.i(TAG, "Finding Warning TextView");
        TextView warning = (TextView) findViewById(R.id.warning_server_status);
        warning.setText("Info - " + serverStatus);
        Log.i(TAG, "Finish Finding Warning TextView");

        Log.i(TAG, "Grabbing the files");
        File f = new File(storagePath);
        Log.i(TAG, "Listing the files");
        File files[] = f.listFiles();

        List<String> filename = new ArrayList<String>();
        for (File file : files)
            filename.add(file.getName());

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, filename.toArray(new String[filename.size()]));

        ListView listView = (ListView) findViewById(R.id.list_file);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_file_manager, menu);
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
