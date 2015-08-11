package com.codeu.frank.mikey.apktransfer;


import android.util.Log;

/**
 * Created by chenfs on 8/6/15.
 */
public class MyServerGlobal {

    // Our glorious server
    private static MyServer server = null;
    // Whether the server is running
    private static Boolean running = null;
    // Get the server information
    private static String serverInfo = null;
    // Random String Generator
    private static RandomStringGenerator randomStringGenerator = null;

    public static final String TAG = "ApkTransfer";

    // Return whether the server is running to the user.
    public static boolean getServerStatus() {
        return running;
    }

    // Return the current server information to the user.
    public static String getServerInfo() {
        return serverInfo;
    }

    // Start the glorious server.
    public static void startServer(String storagePath, String ipAddr, int port) {
        try {
            running = true;
            String security = randomStringGenerator.generateString();
            // Function update the security code of the server.
            server.updateSecurity(security);
            server.start();
            serverInfo = "Server Info: \n" +
                    "IP: " + ipAddr + " \n" +
                    "PORT: " + port + "\n" +
                    "SAMPLE COMMAND: \n" +
                    "curl -v -F upload=@[Path to Your File] \\\n" +
                    "-F security=\"" + security + "\" \\\n" +
                    "\"http://" + ipAddr + ":" + port + "\"";
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            serverInfo = "Server Start Failed";
        }
    }

    // Stop the glorious server
    public static void stopServer() {
        try {
            running = false;
            server.stop();
            serverInfo = "Server Info: \n" +
                    "[Server not running]";
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            serverInfo = "Server Stop Failed";
        }
    }

    // Initialize the server with the essential parameters
    private static MyServer setUpServer(String storagePath, String ip, int port) {
        try {
            return new MyServer(storagePath, port, ip);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }

    // User end initialize call.
    public static void init(String storagePath, String ip, int port) {
        if (server != null) {
            boolean unchanged = true;
            String port_string = "" + port;
            String[] init = server.getParms();
            String[] parms = {storagePath, ip, port_string};
            for (int i = 0; i < parms.length; i++) {
                if (!init[i].equals(parms[i])) {
                    unchanged = !unchanged;
                    break;
                }
            }

            // If something has changed like IP ADDR, then we update the server
            // basically we shut down the current one and then set a new one up
            // ** Mem usage is totally given to Garbage Collector. Just pray
            if (!unchanged) {
                try {
                    server.stop();
                } catch (Exception e) {
                    Log.e(TAG, "Seriously, shit happened");
                }
                server = setUpServer(storagePath, ip, port);
                startServer(storagePath, ip, port);
                serverInfo += "\n" +
                        "Server configuration just changed\n" +
                        "The info above might now be accurate\n" +
                        "Please restart the server";
            }
        } else {
            // If there is no server, we set one up.
            server = setUpServer(storagePath, ip, port);
            randomStringGenerator = new RandomStringGenerator();
        }

        // If there is no status of the server, then server is not running
        if (running == null) {
            running = false;
        }

        // If there is no server info, we initialize the server info
        if (serverInfo == null) {
            serverInfo = "Server Info: \n" +
                    "[Server not running]";
        }

        Log.i(TAG, "Current server info");
        Log.i(TAG, "running? " + running);
        Log.i(TAG, "Server info: " + serverInfo);
    }
}
