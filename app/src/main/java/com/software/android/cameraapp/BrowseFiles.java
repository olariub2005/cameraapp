package com.software.android.cameraapp;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewParent;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.software.android.entities.Privacy;
import com.software.android.entities.Video;
import com.software.android.utils.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class BrowseFiles extends ListActivity {

    private String path;
    final static String OUTPUT_DIR = "CrazySpeedRecorder";
    private String fileName;
    private String fileNamePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_files);
        //getActionBar().setDisplayHomeAsUpEnabled(true);

        // Use the current directory as title
        path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + OUTPUT_DIR;
        if (getIntent().hasExtra("path")) {
            path = getIntent().getStringExtra("path");
        }
        setTitle(path);

        String permissions = Environment.getExternalStorageState(); //.equals(Environment.MEDIA_MOUNTED)

        // Read all files sorted into the values-array
        List values = new ArrayList();
        File dir = new File(path);
        if (!dir.canRead()) {
            setTitle(getTitle() + " (inaccessible)");
        }
        String[] list = dir.list();
        if (list != null) {
            for (String file : list) {
                if (!file.startsWith(".")) {
                    values.add(file);
                }
            }
        }
        Collections.sort(values);

        // Put the data into the list
        ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_2, android.R.id.text1, values);
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        String [] videoTypes = {"3g2", "3gp", "3gpp", "asf", "avi", "dat", "divx", "dv", "f4v", "flv", "gif", "m2ts", "m4v", "mkv", "mod", "mov", "mp4", "mpe", "mpeg", "mpeg4",
                "mpg", "mts", "nsv", "ogm", "ogv", "qt", "tod", "ts", "vob", "wmv"};
        fileName = (String) getListAdapter().getItem(position);

        String [] tempFileName = fileName.split("\\.");
        if (path.endsWith(File.separator)) {
            fileNamePath = path + fileName;
        } else {
            fileNamePath = path + File.separator + fileName;
        }
        if (new File(fileName).isDirectory()) {
            Intent intent = new Intent(this, BrowseFiles.class);
            intent.putExtra("path", fileName);
            startActivity(intent);
         } else {
            if ( Arrays.asList(videoTypes).contains(tempFileName[1])) {
                Toast.makeText(this, fileName + " Publishing video to Facebook ...", Toast.LENGTH_LONG).show();
                //push video file to Facebook
                pushMovieToFacebook(fileName);
                //close view
                finish();
            }
            else {
                Toast.makeText(this, fileName + " - this is not a video file", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void pushMovieToFacebook(String fileName){

        InputStream inputStream = null;
        byte[] videoBytes = new byte[] {};

        try {
            inputStream = new FileInputStream(this.fileNamePath);
            videoBytes = Utils.getBytes(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // set privacy
        Privacy privacy = new Privacy.Builder().setPrivacySettings(Privacy.PrivacySettings.SELF).build();

        // create Photo instance and add some properties
        Video video = new Video.Builder()
                .setVideo(fileName, videoBytes)
                .setTitle("Video generated with CrazySpeedRecorder")
                .setPrivacy(privacy)
                .build();

        SimpleFacebook.getInstance().publish(videoBytes, video);

    }
private void openAlert(View view, String message) {
    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(BrowseFiles.this);
    alertDialogBuilder.setTitle("Information dialog");
    alertDialogBuilder.setMessage(message);
    // set positive button: Yes message
//        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//
//            public void onClick(DialogInterface dialog, int id) {
//                dialog.cancel();
//            }
//
//        });
    final AlertDialog alertDialog = alertDialogBuilder.create();
    // show alert
    alertDialog.show();
    final Timer t = new Timer();
    t.schedule(new TimerTask() {
        public void run() {
            alertDialog.dismiss(); // when the task active then close the dialog
            t.cancel(); // also just top the timer thread, otherwise, you may receive a crash report
        }
    }, 2000); // after 2 second (or 2000 miliseconds), the task will be active.

}
}
