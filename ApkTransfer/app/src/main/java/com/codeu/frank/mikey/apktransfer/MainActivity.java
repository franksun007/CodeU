package com.codeu.frank.mikey.apktransfer;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;

import fi.iki.elonen.NanoHTTPD;


public class MainActivity extends ActionBarActivity {

    public static final String TAG = "ApkTransfer";
    // The folder that we will store our data/file
    public static final String FOLDER_NAME = "apktransfer";
    // The port that the server will use
    public static final int PORT = 6379;

    public static Context mContext;
    private boolean flipflop;
    private NanoHTTPD webserver;
    // Ip addr of the device
    private String ipAddr;
    // The storage path
    private String storagePath;

    public MainActivity() {
        storagePath = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                .getAbsolutePath() + "/" + FOLDER_NAME;
        ipAddr = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Is there a folder exist? If not we create one. If yes we dont do anything
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File directory = new File(storagePath);
            if (!directory.isDirectory())
                directory.mkdirs();
        }

        mContext = getBaseContext();
        // Get the ip address of the device
        WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
        ipAddr = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());

        // Initialize our server.
        MyServerGlobal.init(storagePath, ipAddr, PORT);

        // Something like a initial setting
        TextView tv = (TextView) findViewById(R.id.sinfo);
        Button serverButton = (Button) findViewById(R.id.button);
        tv.setText(MyServerGlobal.getServerInfo());
        if (MyServerGlobal.getServerStatus()) {
            serverButton.setText("Stop Server");
        } else {
            serverButton.setText("Start Server");
        }
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


    public void controlServer(View view) {
        Button serverButton = (Button) findViewById(R.id.button);
        TextView tv = (TextView) findViewById(R.id.sinfo);
        // If the server is not running -
        if (!MyServerGlobal.getServerStatus()) {
            // Set the server button to display that server is running
            // Next click will turn it off
            Log.i(TAG, "Server is running");
            serverButton.setText("Stop Server");

            // For switching the networks
            WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
            ipAddr = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
            MyServerGlobal.init(storagePath, ipAddr, PORT);

            MyServerGlobal.startServer(storagePath, ipAddr, PORT);
        } else {
            // Set the server button to display that server is off
            // Next click will turn it on
            Log.i(TAG, "Server stopped");
            serverButton.setText("Start Server");
            MyServerGlobal.stopServer();
        }
        tv.setText(MyServerGlobal.getServerInfo());
    }

    public void startFileManager(View view) {
        Log.i(TAG, "start FileManager");
        Intent intent = new Intent(this, FileManager.class);
        String serverStatus = "Server is running!";
        if (!MyServerGlobal.getServerStatus())
            serverStatus = "Server is not running";
        intent.putExtra(getString(R.string.server_status_intent_bridge), serverStatus);
        intent.putExtra("storagePath", storagePath);
        startActivity(intent);
    }

    public static Context getContext() {
        return mContext;
    }
}
