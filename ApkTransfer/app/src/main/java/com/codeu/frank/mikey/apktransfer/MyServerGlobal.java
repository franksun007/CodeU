package com.codeu.frank.mikey.apktransfer;


import android.util.Log;

/**
 * Created by chenfs on 8/6/15.
 */
public class MyServerGlobal {

    private static MyServer server = null;
    private static Boolean running = null;
    private static String serverInfo = null;

//    private static MyServerGlobal mInstance = null;
//
    public static final String TAG = "ApkTransfer";
//
//    private String storagePath = null;
//    private Integer port = null;
//    private String ip = null;
//
//    private static NanoHTTPD server = null;
//

    public static boolean getServerStatus() {
        return running;
    }

    public static String getServerInfo() {
        return serverInfo;
    }

    public static void startServer(String storagePath, String ipAddr, int port) {
        try {
            running = true;
            server.start();
            serverInfo = "Server Info: \n" +
                    "IP: " + ipAddr + " \n" +
                    "PORT: " + port + "\n" +
                    "SAMPLE COMMAND: \n" +
                    "curl -v -F upload=@[Path to Your File] \\\n" +
                    "\"http://" + ipAddr + ":" + port + "\"";
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            serverInfo = "Server Start Failed";
        }
    }

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

    private static MyServer setUpServer(String storagePath, String ip, int port) {
        try {
            return new MyServer(storagePath, port, ip);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }
//
//    public static NanoHTTPD getServer() {
//        return server;
//    }
//

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
            if (!unchanged) {
                try {
                    server.stop();
                } catch (Exception e) {
                    Log.e(TAG, "Seriously, shit happened");
                }
                server = setUpServer(storagePath, ip, port);
            }
        } else {
            server = setUpServer(storagePath, ip, port);
        }
        if (running == null) {
            running = false;
        }
        if (serverInfo == null) {
            serverInfo = "Server Info: \n" +
                    "[Server not running]";
        }

        Log.i(TAG, "Current server info");
        Log.i(TAG, "running? " + running);
        Log.i(TAG, "Server info: " + serverInfo);
    }

//    public void init(String storagePath, int port, String ip) {
//        if (this.storagePath == null && this.port == null && this.ip == null) {
//            this.storagePath = storagePath;
//            this.port = port;
//            this.ip = ip;
//            server = fireUpServer(this.storagePath, this.port, this.ip);
//        } else {
//            boolean unchanged = true;
//
//            if(!this.storagePath.equals(storagePath))
//                unchanged = stopServer();
//
//            if ((this.port) != port)
//                unchanged = stopServer();
//
//            if(!this.ip.equals(ip))
//                unchanged = stopServer();
//
//            if (!unchanged) {
//                this.storagePath = storagePath;
//                this.port = port;
//                this.ip = ip;
//            }
//        }
//    }
//
//    public boolean stopServer() {
//        try {
//            server.stop();
//        } catch (Exception e) {
//            Log.e(TAG, "Seriously, shit happened");
//        }
//        return false;
//    }
}
