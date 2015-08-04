package com.codeu.frank.mikey.apktransfer;

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

import java.io.File;

import fi.iki.elonen.SimpleWebServer;

public class MainActivity extends ActionBarActivity {

    public static final String TAG = "ApkTransfer";
    public static final int PORT = 6379;

    private boolean flipflop;
    public SimpleWebServer webserver;

    public MainActivity() {
        flipflop = true;
        webserver = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        }
        flipflop = !flipflop;
    }

    public SimpleWebServer createServer() {
        WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        File f = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        Log.i(TAG, ip);
        Log.i(TAG, f.toString());
        return new SimpleWebServer(ip, PORT, f, false);
    }
}
