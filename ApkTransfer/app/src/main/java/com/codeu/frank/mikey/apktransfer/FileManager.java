package com.codeu.frank.mikey.apktransfer;

import android.app.ListActivity;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.AdapterContextMenuInfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class FileManager extends ActionBarActivity {

    private String serverStatus;
    private String storagePath;
    public static final String TAG = "ApkTransfer";

    ListView listView;
    ArrayAdapter<String> adapter;


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
        warning.setText(serverStatus);
        Log.i(TAG, "Finish Finding Warning TextView");

        setupFileListandBindListener();
    }

    public void setupFileListandBindListener() {
        Log.i(TAG, "Grabbing the files");
        File f = new File(storagePath);
        Log.i(TAG, "Listing the files");
        File files[] = f.listFiles();

        List<String> filename = new ArrayList<String>();
        for (File file : files)
            filename.add(file.getName());


        listView = (ListView) findViewById(R.id.list_file);
        registerForContextMenu(listView);

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_activated_1,
                filename.toArray(new String[filename.size()]));

        listView.setAdapter(adapter);

        Log.d(TAG, "here");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.showContextMenu();
            }
        });
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.id_delete:
                deleteItem((TextView) info.targetView);
                return true;
            case R.id.id_install:
                installItem(info.id);
                return true;
            case R.id.id_move:
                moveItem(info.id);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void deleteItem(TextView view) {
        String info = view.getText().toString();
        Log.d(TAG, info);

        try {
            // delete the original file
            new File(storagePath + "/" + info).delete();
        } catch (Exception e) {
            Log.e("tag", e.getMessage());
        }

        setupFileListandBindListener();
    }

    private void installItem(long id) {
        TextView tv = (TextView) listView.findViewById(android.R.id.text1);
        String info = tv.getText().toString();
        Log.d(TAG, info);
    }

    private void moveItem(long id) {
        TextView tv = (TextView) listView.findViewById(android.R.id.text1);
        String info = tv.getText().toString();
        Log.d(TAG, info);
        setupFileListandBindListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_file_manager, menu);
        return true;
    }


    public void onListItemClick(ListView l, View v, int position, long id)
    {
        v.showContextMenu();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater options_menu_inflater = getMenuInflater();
        options_menu_inflater.inflate(R.menu.options_menu, menu);
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
