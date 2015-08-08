package com.codeu.frank.mikey.apktransfer;

import android.content.Context;
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
    public static final String FOLDER_NAME = "apktransfer";
    public static final int PORT = 6379;

    public static Context mContext;
    private boolean flipflop;
    private NanoHTTPD webserver;
    private String storagePath;

    public MainActivity() {
        flipflop = true;
        webserver = null;
        storagePath = "";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String basePath = Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    .getAbsolutePath();
            storagePath = basePath + "/" + FOLDER_NAME;
            File directory = new File(storagePath);
            if(!directory.isDirectory())
                directory.mkdirs();
        }

        mContext = getBaseContext();
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


    public void startServer(View view) {
        Button serverButton = (Button) view.findViewById(R.id.button);
        if (flipflop) {
            // Set the server button to display that server is running
            // Next click will turn it off
            serverButton.setText("Stop Server");
            webserver = createServer();
            try {
                webserver.start();
            } catch(Exception e) {
                Log.i(TAG, e.toString());
            }
        } else {
            // Set the server button to display that server is off
            // Next click will turn it on
            serverButton.setText("Start Server");
            webserver.stop();
            TextView tv = (TextView) findViewById(R.id.sinfo);
            tv.setText(R.string.server_info);
        }
        flipflop = !flipflop;
    }

    public NanoHTTPD createServer() {
        try {
            TextView tv = (TextView) findViewById(R.id.sinfo);
            WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
            String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
            String tvtext = "Server Info: \n" +
                    "IP: " + ip + " \n" +
                    "PORT: " + PORT + "\n" +
                    "SAMPLE COMMAND: \n" +
                    "curl -v -F upload=@<File> \\\n" +
                    "\"http://" + ip + ":" + PORT + "\"";
            tv.setText(tvtext);
            return new MyServer(storagePath, PORT, ip);
        } catch (Exception e){
            Log.e(TAG, e.toString());
        }
        return null;
    }

    public void startFileManager(View view) {
        
    }

    public static Context getContext() {
        return mContext;
    }
}
