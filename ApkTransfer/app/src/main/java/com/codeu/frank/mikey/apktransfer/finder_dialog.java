package com.codeu.frank.mikey.apktransfer;

import android.content.DialogInterface;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.content.Context;
import android.app.AlertDialog;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.content.DialogInterface.OnClickListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
/**
 * Created by michaelchapman on 8/11/15.
 */
public class finder_dialog implements OnItemClickListener, OnClickListener {
    List<File> m_entries = new ArrayList<File>();
    File m_currentDir;
    Context m_context;
    AlertDialog m_alertDialog;
    ListView m_list;
    ArrayAdapter<File> adapter;
    public String path;
    public static final String TAG = "ApkTransfer";

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position < 0 || position >= m_entries.size())
            return;

        if (m_entries.get(position).getName().equals(".."))
            m_currentDir = m_currentDir.getParentFile();
        else
            m_currentDir = m_entries.get(position);

        listDirs();
        DirAdapter adapter = new DirAdapter(m_context, m_entries);
        m_list.setAdapter(adapter);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

    }
    //Result m_result = null;

    public class DirAdapter extends ArrayAdapter<File> {
        public DirAdapter(Context context, List<File> files) {
            super(context, android.R.layout.simple_list_item_activated_1, files);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            File file = getItem(position);

            TextView tv = (TextView) super.getView(position, convertView, parent);

            if (file == null) {
                tv.setText("..");
                tv.setCompoundDrawablesWithIntrinsicBounds(m_context.getResources().getDrawable(R.drawable.parent), null, null, null);
            } else {
                tv.setText(file.getName());
                tv.setCompoundDrawablesWithIntrinsicBounds(m_context.getResources().getDrawable(R.drawable.child), null, null, null);
            }

            return tv;
        }
    }

    public finder_dialog(Context ctx, final String info, final String storagePath) {
        //adapter = new ArrayAdapter<File>(ctx, android.R.layout.simple_list_item_activated_1, m_entries);
        m_context = ctx;
        m_currentDir = Environment.getExternalStorageDirectory();
        listDirs();
        DirAdapter adapter = new DirAdapter(ctx, m_entries);

        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle(R.string.dialog_title);
        builder.setAdapter(adapter, this);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                path = m_currentDir.getAbsolutePath();
                Log.d(TAG, path);
                moveFile(path, info, storagePath);
                dialog.dismiss();
            }
        });

        builder.setNegativeButton( "Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();
        m_list = alertDialog.getListView();
        m_list.setOnItemClickListener(this);
        alertDialog.show();
    }

    private void moveFile(String path, String file, String storagePath) {
        InputStream in = null;
        OutputStream out = null;
        try {

            //create output directory if it doesn't exist
            File dir = new File(path);
            Log.d(TAG, path);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            in = new FileInputStream(storagePath + "/" + file);
            out = new FileOutputStream(path + "/" + file);

            Log.d(TAG, "copying over stuff");
            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }

            Log.d(TAG, "closing out stuff");
            in.close();
            in = null;

            // write the output file (You have now copied the file)
            out.flush();
            out.close();
            out = null;

            Log.d(TAG, "deleting old file");
            new File(storagePath + "/" + file).delete();
        }  catch (FileNotFoundException fnfe1) {
            Log.e("tag", fnfe1.getMessage());
        }
        catch (Exception e) {
            Log.e("tag", e.getMessage());
        }
    }

    private void listDirs() {
        m_entries.clear();

        // Get files
        File[] files = m_currentDir.listFiles();

        // Add the ".." entry
        if (m_currentDir.getParent() != null)
            m_entries.add( new File("..") );

        if ( files != null ) {
            for ( File file : files ) {
                if (!file.isDirectory())
                    continue;
                m_entries.add( file );
            }
        }

        Collections.sort(m_entries, new Comparator<File>() {
            public int compare(File f1, File f2) {
                return f1.getName().toLowerCase().compareTo( f2.getName().toLowerCase() );
            }
        });
    }


}
