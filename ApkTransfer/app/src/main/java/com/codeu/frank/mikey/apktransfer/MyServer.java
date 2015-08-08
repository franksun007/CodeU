package com.codeu.frank.mikey.apktransfer;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;

/**
 * Created by chenfs on 8/3/15.
 */
public class MyServer extends NanoHTTPD{
    public static final String TAG = "ApkTransfer";

    private String storagePath;
    private String ipAddr;
    private int port;

    public MyServer(String storagePath, int port, String ip) throws IOException {
        super(port);
        // cache the current config info
        this.port = port;
        this.storagePath = storagePath;
        this.ipAddr = ip;
    }

    // return the current config info
    public String[] getParms() {
        String port_string = "" + port;
        String[] result = {storagePath, ipAddr, port_string};
        return result;
    }

    // Override the serve method
    /** *********************************** REMEMBER TO ADD THE SECURITY PART *********************************** */
    @Override
    public Response serve(String uri,
                          Method method,
                          Map<String, String> headers,
                          Map<String, String> parms,
                          Map<String, String> files) {

        try {
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
                return new Response(Response.Status.INTERNAL_ERROR,
                        NanoHTTPD.MIME_PLAINTEXT, "File does not save successfully");
            }
            return new Response(Response.Status.OK, NanoHTTPD.MIME_PLAINTEXT, "File Received");
        } catch (Exception e) {
            Log.w(TAG, e.toString());
            StringBuffer html = new StringBuffer();
            html.append("<html>");
            html.append("<head>");
            html.append("<title>Instruction for ApkTransfer</title>");
            html.append("<style>body { font-family:courier } </style>");
            html.append("</head>");
            html.append("<body>");
            html.append("<h1>This is the Instruction for using ApkTransfer App</h1>");
            html.append("<h2>From a terminal/console, type a CURL command like:</h2>");
            html.append("<h2><pre>curl -v -F upload=@[Path to Your File] " +
                    "\"http://" + ipAddr + ":" + port + "\"</pre></h2>");
            html.append("<h2><pre>e.g.</pre></h2>");
            html.append("<h2><pre>curl -v -F " +
                    "upload=@/Users/dude/MyAwesomeCoolapp.apk " +
                    "\"http://" + ipAddr + ":" + port + "\"</pre></h2>");
            html.append("<h2>If you receive some response like \"File Received\" - </h2>");
            html.append("<h2>That means you are a hero.</h2>");
            html.append("<p>PS: How to install CURL - </p>");
            html.append("<p>Fedora Distribution: sudo yum install -y curl</p>");
            html.append("<p>Debian Distribution: sudo apt-get install -y curl</p>");
            html.append("<p>Mac OS - : Step One:  Install Homebrew first</p>");
            html.append("<p>Mac OS - : Step Two:  brew install curl</p>");
            html.append("<p>Windows: Ask Google - too many ways</p>");
            html.append("</body>");
            html.append("</html>");
            return new Response(Response.Status.BAD_REQUEST, NanoHTTPD.MIME_HTML, html.toString());
        }
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
