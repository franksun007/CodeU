package com.codeu.frank.mikey.apktransfer;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import fi.iki.elonen.NanoHTTPD;

/**
 * Created by chenfs on 8/3/15.
 */
public class MyServer extends NanoHTTPD{
    public static final int PORT = 6379;
    public static final String TAG = "ApkTransfer";

    private String storagePath;

    public MyServer(String storagePath) throws IOException {
        super(PORT);
        this.storagePath = storagePath;
    }

    public Response serve(String uri,
                          Method method,
                          Map<String, String> headers,
                          Map<String, String> parms,
                          Map<String, String> files) {

        String path = files.get("upload");
        String fileName = parms.get("upload");
        Log.i(TAG, "File Original Name is " + fileName);
        Log.i(TAG, "PATH of the temp file is at " + path);
        File src = new File(path);

        File dst = new File(storagePath + "/" + fileName);
        try {
            copy(src, dst);
        } catch (Exception e) {
            Log.e(TAG, "Copying file failed");
            Log.e(TAG, e.toString());
        }

        return new Response(Response.Status.OK, NanoHTTPD.MIME_PLAINTEXT, "File Received");
    }

    // Copy file from src to dst
    public void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }
}
