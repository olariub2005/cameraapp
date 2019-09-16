package com.software.android.cameraapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Camera;
import android.location.Location;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.facebook.appevents.AppEventsLogger;
import com.software.android.interfaces.Constants;
import com.software.android.interfaces.GPSCallback;
import com.software.android.managers.GPSManager;
import com.software.android.settings.AppSettings;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class CameraAppActivity extends AppCompatActivity implements GPSCallback{

    //GPS declarations
    private GPSManager gpsManager = null;
    private double speed = 0.0;
    private long time = 0;
    private int measurement_index = Constants.INDEX_KM;
    private AbsoluteSizeSpan sizeSpanLarge = null;
    private AbsoluteSizeSpan sizeSpanSmall = null;
    private volatile boolean startGPS = false;

    //UI declarations
    private Button recordVideoSpeedButton;
    private Button stopVideoSpeedButton;
    private Button recordVideoButton;
    private Button stopVideoButton;
    private Button playButton;
    private Button publishToFacebook;
    private VideoView videoView;
    private Camera camera;
    private CameraSurfaceView cameraSurfaceView;
    private volatile MediaRecorder mediaRecorder;

    //IO declarations
    private File file = null;
    private FileWriter fw = null;
    private BufferedWriter bw = null;
    int i;
    final static long tRef = 79200000;
    private long fileExtension;
    DateFormat formatExtension;

    private volatile boolean recordingVideoSpeedFlag = false;
    private volatile boolean recordingVideoFlag = false;
    public static int orientation;
    private long [] locationTime = new long [10000];
    private String [] locationSpeed = new String [10000];
    int arrayLength = 0;
    Color recordButtonColor;

    private String outputSubtitleFile = new String();
    final static int FRAME_RATE = 30;
    final static String OUTPUT_DIR = "CrazySpeedRecorder";
    public static final int DEFAULT_HIGH_QUALITY = 3000000;// 3 megapixel
    public static final int AUDIO_CHANNELS = 2; //stereo
    public static final int SAMPLE_RATE = 96000; //AAC audio coding standard ranges from 8 to 96 kHz

    private String outputVideoFile = new String();
    public final static String EXTRA_OUTPUT_VIDEO_FILE = "com.software.android.cameraapp.EXTRA_OUTPUT_VIDEO_FILE";
    public final static String EXTRA_OUTPUT_SUBTITLE_FILE = "com.software.android.cameraapp.EXTRA_OUTPUT_SUBTITLE_FILE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_app);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
       /*      FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

        recordingVideoSpeedFlag = false;
        recordingVideoFlag = false;
        startGPS = false;

         //set GPS
        gpsManager = new GPSManager();
        gpsManager.startListening(getApplicationContext());
        gpsManager.setGPSCallback(CameraAppActivity.this);
        ((TextView)findViewById(R.id.info_message)).setText(getString(R.string.info));
        measurement_index = AppSettings.getMeasureUnit(CameraAppActivity.this);

//        //Get Camera for preview
//        camera = getCameraInstance();
//        if(camera == null){
//            Toast.makeText(CameraAppActivity.this,
//                    "Fail to get Camera",
//                    Toast.LENGTH_LONG).show();
//        }

        //preview management class
//        cameraSurfaceView = new CameraSurfaceView(this, camera,this);
//        FrameLayout cameraPreview = (FrameLayout) findViewById (R.id.SampleVideoView);
//        cameraPreview.addView(cameraSurfaceView);

        //buttons
        recordVideoSpeedButton = (Button) findViewById(R.id.RecordVideoSpeed);
        stopVideoSpeedButton = (Button) findViewById(R.id.StopVideoSpeed);

        recordVideoButton = (Button) findViewById(R.id.RecordVideo);
        stopVideoButton = (Button) findViewById(R.id.StopVideo);

        //playButton = (Button) findViewById(R.id.Play);
        publishToFacebook = (Button) findViewById(R.id.PublishToFacebook);
        videoView = (VideoView) findViewById (R.id.videoView);
    }

    public synchronized void startVideoSpeedRecording(View view ) {
        if ((!recordingVideoSpeedFlag) && (!recordingVideoFlag) && (startGPS)) {
            Date date = new Date();
            fileExtension = date.getTime();
            formatExtension = new SimpleDateFormat("dd-mm-yyyy_HH-mm-ss");
            (new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + OUTPUT_DIR)).mkdirs();
            try {
                releaseCamera();
                if(!prepareMediaRecorder()){
                    Toast.makeText(CameraAppActivity.this,
                            "Fail in prepareMediaRecorder()!\n - Ended -",
                            Toast.LENGTH_LONG).show();
                    finish();
                }
                mediaRecorder.start();
                openAlert(view, "Recording path " + Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + OUTPUT_DIR);
                recordVideoSpeedButton.setBackgroundColor(Color.RED);
            } catch (IllegalStateException e) {
                e.printStackTrace();
                openAlert(view, "Illegal state exception in startRecording method !");
            }
             recordingVideoSpeedFlag = true;
        }
        else {
            openAlert(view, "GPS signal is not aquired ! ");
        }
    }
    public synchronized void startVideoRecording(View view ) {
        if ( (!recordingVideoFlag) && (!recordingVideoSpeedFlag)) {
            Date date = new Date();
            fileExtension = date.getTime();
            formatExtension = new SimpleDateFormat("dd-mm-yyyy_HH-mm-ss");
            (new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + OUTPUT_DIR)).mkdirs();
            try {
                releaseCamera();
                if(!prepareMediaRecorder()){
                    Toast.makeText(CameraAppActivity.this,
                            "Fail in prepareMediaRecorder()!\n - Ended -",
                            Toast.LENGTH_LONG).show();
                    finish();
                }
                mediaRecorder.start();
                openAlert(view, "Recording path " + Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + OUTPUT_DIR);
                recordVideoButton.setBackgroundColor(Color.RED);
            } catch (IllegalStateException e) {
                e.printStackTrace();
                openAlert(view, "Illegal state exception in startRecording method !");
            }
            recordingVideoFlag = true;
        }
    }
    public synchronized void stopVideoSpeedRecording(View view) {
        if (recordingVideoSpeedFlag) {
            try {
                // stop recording and release camera
                mediaRecorder.stop();  // stop the recording
                releaseMediaRecorder(); // release the MediaRecorder object
                recordVideoSpeedButton.setBackgroundColor(Color.LTGRAY);
            } catch (IllegalStateException e) {
                openAlert(view, "Illegal state exception in stopRecording method !");
            }

            recordingVideoSpeedFlag = false;
            generateSRT();
        }
        else {
            openAlert(view, "Recording Video/Speed already stopped !");
        }
    }
    public synchronized void stopVideoRecording(View view) {
        if (recordingVideoFlag) {
            try {
                // stop recording and release camera
                mediaRecorder.stop();  // stop the recording
                releaseMediaRecorder(); // release the MediaRecorder object
                recordVideoButton.setBackgroundColor(Color.LTGRAY);
            } catch (IllegalStateException e) {
                openAlert(view, "Illegal state exception in stopRecording method !");
            }

            recordingVideoFlag = false;
        }
        else {
            openAlert(view, "Recording Video already stopped !");
        }
    }
    public void playRecording(View view){
        Intent intent = new Intent(this, VideoMediaPlayer.class);
        startActivity(intent);
    }
    public void publishToFacebook(View view){
        Intent intent = new Intent(this, PublishToFacebook.class);
        startActivity(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean result = true;
        switch(item.getItemId())
        {
            case R.id.menu_about:
            {
                displayAboutDialog();
                break;
            }
            case R.id.unit_km:
            {
                measurement_index = 0;
                AppSettings.setMeasureUnit(this, 0);
                break;
            }
            case R.id.unit_miles:
            {
                measurement_index = 1;
                AppSettings.setMeasureUnit(this, 1);
                break;
            }
            default:
            {
                result = super.onOptionsItemSelected(item);
                break;
            }
        }
        return result;
    }

    private void displayAboutDialog()
    {
        final LayoutInflater inflator = LayoutInflater.from(this);
        final View settingsview = inflator.inflate(R.layout.about, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(getString(R.string.app_name));
        builder.setView(settingsview);

        builder.setPositiveButton(android.R.string.ok, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.create().show();
    }
    private void openAlert(View view, String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CameraAppActivity.this);
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
    public void write(String fcontent){
        try {
            bw.append(fcontent);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(CameraAppActivity.this,
                    "Fail to write in *.srt file!",
                    Toast.LENGTH_LONG).show();
        }
    }
    private Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open();
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
            Toast.makeText(CameraAppActivity.this,
                    "Camera is not available (in use or does not exist)",
                    Toast.LENGTH_LONG).show();
        }
        return c; // returns null if camera is unavailable
    }
    public static void setCameraDisplayOrientation(Activity activity,
                                                   int cameraId, Camera camera) {
        Camera.CameraInfo info =
                new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0: degrees = 0; break;
            case Surface.ROTATION_90: degrees = 90; break;
            case Surface.ROTATION_180: degrees = 180; break;
            case Surface.ROTATION_270: degrees = 270; break;
        }
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        CameraAppActivity.orientation=result;
        camera.setDisplayOrientation(result);
    }
    public void generateSRT(){

        outputSubtitleFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + OUTPUT_DIR + "/video_" + formatExtension.format(fileExtension) + ".srt";
        DateFormat format = new SimpleDateFormat("HH:mm:ss,SSS");
        file = new File(outputSubtitleFile);
        try {
            fw = new FileWriter(file.getAbsoluteFile());
            bw = new BufferedWriter(fw);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(CameraAppActivity.this,
                    "Fail to generate SRT file",
                    Toast.LENGTH_LONG).show();
        }
        long t1 = 0;
//        for (int j = 0; j < arrayLength-1; j++) {
//            if ( (locationTime[j+1] - locationTime[j]) == 1000) {
//                write(String.valueOf(j+1) + "\n");
//                write(format.format(t1 + tRef) + " --> " + format.format(t1 + tRef + locationTime[j + 1] - locationTime[j]) + "\n");
//                write(locationSpeed[j] + "\n\n");
//            }
//            t1 = t1 + locationTime[j+1] - locationTime[j];
//        }
        int k = 0;
        boolean even = true;
        boolean odd = true;

        if ( (arrayLength & 1) == 0) {
            arrayLength = arrayLength - 1;
        }
        for (int j = 0; j < arrayLength - 1; j++) {
            if ( (locationTime[j+1] - locationTime[j]) == 1000) {
                write(String.valueOf(j+1) + "\n");
                write(format.format(t1 + tRef) + " --> " + format.format(t1 + tRef + locationTime[j + 1] - locationTime[j]) + "\n");
                write(locationSpeed[j] + "\n\n");

                   
            }
            t1 = t1 + locationTime[j+1] - locationTime[j];
        }
        try {
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(CameraAppActivity.this,
                    "Cannot stop BufferWriter !",
                    Toast.LENGTH_LONG).show();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseMediaRecorder();       // if you are using MediaRecorder, release it first
        releaseCamera();              // release the camera immediately on pause event
        cameraSurfaceView = null;
        gpsManager.stopListening();
        gpsManager.setGPSCallback(null);
        gpsManager = null;
    }
    //Class implemented to manage preview
    public class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
        private SurfaceHolder holder;
        private Camera camera;
        private Activity activity;
        private Context context;
        public CameraSurfaceView(Context context, Camera camera,Activity activity) {
            super(context);
            this.camera = camera;
            this.activity=activity;
            this.context= context;
            // Install a SurfaceHolder.Callback so we get notified when the
            // underlying surface is created and destroyed.
            holder = getHolder();
            holder.addCallback(this);
            // deprecated setting, but required on Android versions prior to 3.0
            holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
        {
            try {
                setCameraDisplayOrientation(activity, 0, this.camera);
                previewCamera();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(CameraAppActivity.this,
                        "Surface Chenged Error !",
                        Toast.LENGTH_LONG).show();
            }
        }
        public void previewCamera()
        {
            try
            {
                camera.setPreviewDisplay(holder);
                camera.startPreview();
            }
            catch(Exception e)
            {
                Toast.makeText(CameraAppActivity.this,
                        "Cannot start preview!",
                        Toast.LENGTH_LONG).show();
            }
        }
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            // TODO Auto-generated method stub
            // The Surface has been created, now tell the camera where to draw the preview.
            try {
                camera.setPreviewDisplay(holder);
                camera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            // TODO Auto-generated method stub
        }
    }
    //GPS methods
    int j = 0;
    @Override
    public void onGPSUpdate(final Location location)
    {
        //event started in new thread
        new Thread(new Runnable() {
            public void run() {
                final DateFormat format = new SimpleDateFormat("HH:mm:ss,SSS");
                final DateFormat showFormat = new SimpleDateFormat("HH:mm:ss");

                startGPS = true;

                location.getLatitude();
                location.getLongitude();
                speed = location.getSpeed();
                time = location.getTime();
                final String speedString = "" + roundDecimal(convertSpeed(speed), 2);
                final String unitString = measurementUnitString(measurement_index);

                //send data to UI
                videoView.post(new Runnable() {
                    public void run() {
                        setSpeedText(R.id.info_message, speedString + " " + unitString + " " + showFormat.format(time));
                    }
                });


                if (recordingVideoSpeedFlag) {
                    locationTime[j] = time;
                    locationSpeed[j] = speedString + " " + unitString + " " + showFormat.format(time);
                    j++;
                    arrayLength = j;
                }
            }

         }).start();
    }
    private double convertSpeed(double speed){
        return ((speed * Constants.HOUR_MULTIPLIER) * Constants.UNIT_MULTIPLIERS[measurement_index]);
    }
    private String measurementUnitString(int unitIndex){
        String string = "";
        switch(unitIndex)
        {
            case Constants.INDEX_KM:                string = "km/h";        break;
            case Constants.INDEX_MILES:     string = "mi/h";        break;
        }
        return string;
    }
    private double roundDecimal(double value, final int decimalPlace)
    {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(decimalPlace, RoundingMode.HALF_UP);
        value = bd.doubleValue();
        return value;
    }
    private void setSpeedText(int textid, String text) {
        Spannable span = new SpannableString(text);
        int firstPos = text.indexOf(32);
        span.setSpan(sizeSpanLarge, 0, firstPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(sizeSpanSmall, firstPos + 1, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        TextView tv = ((TextView)findViewById(textid));
        tv.setHighlightColor(Color.GRAY);
        tv.setText(span);
    }
    @Override
    protected void onPause() {
        super.onPause();
        recordVideoSpeedButton.setBackgroundColor(Color.LTGRAY);
        recordVideoButton.setBackgroundColor(Color.LTGRAY);
        releaseMediaRecorder();       // if you are using MediaRecorder, release it first
        releaseCamera();              // release the camera immediately on pause event

        if ( arrayLength > 0 ) {
            generateSRT();
        }
        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }
    @Override
    protected void onResume() {
        super.onResume();
        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);

//        try {
//
//            if (gpsManager == null) {
//                gpsManager = new GPSManager();
//                gpsManager.startListening(getApplicationContext());
//                gpsManager.setGPSCallback(CameraAppActivity.this);
//                ((TextView) findViewById(R.id.info_message)).setText(getString(R.string.info));
//                measurement_index = AppSettings.getMeasureUnit(CameraAppActivity.this);
//            }

//            //Get Camera for preview
//            if (camera == null) {
//                camera = getCameraInstance();
//            }
//            if (camera == null) {
//                Toast.makeText(CameraAppActivity.this,
//                        "Fail to get Camera",
//                        Toast.LENGTH_LONG).show();
//            }

            //preview management class
            //cameraSurfaceView = null;
//            cameraSurfaceView = new CameraSurfaceView(this, camera, this);
//            FrameLayout cameraPreview = (FrameLayout) findViewById(R.id.SampleVideoView);
//            cameraPreview.addView(cameraSurfaceView);

//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        if  ( (recordingVideoSpeedFlag == true) || (recordingVideoFlag == true) ) {
            try {
                releaseCamera();
                if (!prepareMediaRecorder()) {
                    Toast.makeText(CameraAppActivity.this,
                            "Fail in prepareMediaRecorder()!\n - Ended -",
                            Toast.LENGTH_LONG).show();
                    finish();
                }
                mediaRecorder.start();
                recordVideoSpeedButton.setBackgroundColor(Color.RED);
                recordVideoButton.setBackgroundColor(Color.RED);
            } catch (IllegalStateException e) {
                e.printStackTrace();
                Toast.makeText(CameraAppActivity.this,
                        "Illegal state exception in start recording onResume method !",
                        Toast.LENGTH_LONG).show();
            }
        }
    }
    private boolean prepareMediaRecorder(){
        camera = getCameraInstance();
        // set the orientation here to ene portrait recording.
        setCameraDisplayOrientation(this,0,camera);
        List<Camera.Size> cameraVideoSizeList = new ArrayList<Camera.Size>();

        if (camera.getParameters().getSupportedVideoSizes() != null) {
            cameraVideoSizeList = camera.getParameters().getSupportedVideoSizes();
        } else {
            // Video sizes may be null, which indicates that all the supported
            // preview sizes are supported for video recording.
            cameraVideoSizeList = camera.getParameters().getSupportedPreviewSizes();
        }
        mediaRecorder = new MediaRecorder();
        camera.unlock();
        mediaRecorder.setCamera(camera);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);

        Collections.sort(cameraVideoSizeList, new Comparator<Camera.Size>() {

            public int compare(final Camera.Size a, final Camera.Size b) {
                return a.width * a.height - b.width * b.height;
            }
        });
        mediaRecorder.setVideoSize(cameraVideoSizeList.get(cameraVideoSizeList.size() - 1).width, cameraVideoSizeList.get(cameraVideoSizeList.size() - 1).height);

        // Frame rate
       mediaRecorder.setVideoFrameRate(FRAME_RATE);
       mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
       mediaRecorder.setVideoEncodingBitRate(DEFAULT_HIGH_QUALITY);

       mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
       mediaRecorder.setAudioChannels(AUDIO_CHANNELS);
       mediaRecorder.setAudioSamplingRate(SAMPLE_RATE);
        //mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));

        File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + OUTPUT_DIR + "/video_" + formatExtension.format(fileExtension) + ".mkv");
        if(f.exists() && !f.isDirectory()) {
            }
        else {
            outputVideoFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + OUTPUT_DIR + "/video_" + formatExtension.format(fileExtension) + ".mkv";
            mediaRecorder.setOutputFile(outputVideoFile);
        }

        mediaRecorder.setMaxDuration(3600000); // Set max duration 1 hour
        mediaRecorder.setMaxFileSize(500000000); // Set max file size 500 Mb
        //mediaRecorder.setPreviewDisplay(cameraSurfaceView.getHolder().getSurface());
        mediaRecorder.setPreviewDisplay(videoView.getHolder().getSurface());
        //mediaRecorder.setOrientationHint(CameraAppActivity.orientation);
        try {
            mediaRecorder.prepare();
        } catch (IllegalStateException e) {
            releaseMediaRecorder();
            Toast.makeText(CameraAppActivity.this,
                    "Fail in prepareMediaRecorder - IllegalStateException",
                    Toast.LENGTH_LONG).show();
            return false;
        } catch (IOException e) {
            releaseMediaRecorder();
            Toast.makeText(CameraAppActivity.this,
                    "Fail in prepareMediaRecorder - IOException!",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
    private void releaseMediaRecorder(){
        if (mediaRecorder != null) {
            mediaRecorder.reset();   // clear recorder configuration
            mediaRecorder.release(); // release the recorder object
            mediaRecorder = null;
//            mediaRecorder = new MediaRecorder();
//            camera.lock();           // lock camera for later use
        }
    }
    private void releaseCamera(){
        if (camera != null){
            camera.release();        // release the camera for other applications
            camera = null;
        }
    }
}