/*
 * Copyright 2015 Google Inc. All Rights Reserved.

 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.laurentianvrapp;

import com.google.vr.sdk.widgets.pano.VrPanoramaEventListener;
import com.google.vr.sdk.widgets.pano.VrPanoramaView;
import com.google.vr.sdk.widgets.pano.VrPanoramaView.Options;


import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import com.google.vr.sdk.base.GvrActivity;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A basic PanoWidget Activity to load panorama images from disk. It will load a test image by
 * default. It can also load an arbitrary image from disk using:
 *   adb shell am start -a "android.intent.action.VIEW" \
 *     -n "com.google.vr.sdk.samples.simplepanowidget/.SimpleVrPanoramaActivity" \
 *     -d "/sdcard/FILENAME.JPG"
 *
 * To load stereo images, "--ei inputType 2" can be used to pass in an integer extra which will set
 * VrPanoramaView.Options.inputType.
 */
public class SimpleVrPanoramaActivityWithInput extends GvrActivity   {

    private static final String TAG = SimpleVrPanoramaActivity.class.getSimpleName();
    String panoFile = "test5.JPG";

    boolean notFirst = false;
    int arraySize;
    String[] panoFileArray;
    int panoFileCounter = 0;

    int buf = 0;
    int bufL = 0;
    Bitmap currentImage;
    Bitmap[] BitmapRecycler = new Bitmap[5];
    ImageLoaderTask currentImageLoader;
    ImageLoaderTask[] ImageLoaderRecycler = new ImageLoaderTask[5];
    Timer timer;

    private Vibrator vibrator;


    /** Actual panorama widget. **/
    private VrPanoramaView panoWidgetView;

    /**
     * Arbitrary variable to track load status. In this example, this variable should only be accessed
     * on the UI thread. In a real app, this variable would be code that performs some UI actions when
     * the panorama is fully loaded.
     */
    private boolean loadImageSuccessful;
    /** Tracks the file to be loaded across the lifetime of this app. **/
    private Uri fileUri;
    /** Configuration information for the panorama. **/
    private Options panoOptions = new Options();
    private ImageLoaderTask backgroundImageLoaderTask;


    /**
     * Called when the app is launched via the app icon or an intent using the adb command above. This
     * initializes the app and loads the image to render.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initRecyclers();
        Log.v("This", "onCreate-----------------------------------------------------");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pano_layout);

        Intent intent = getIntent();

        panoFileArray = intent.getStringArrayExtra("listOfFiles");
        arraySize = panoFileArray.length;

        panoWidgetView = (VrPanoramaView) findViewById(R.id.pano_view);
        panoWidgetView.setFullscreenButtonEnabled(false);
        panoWidgetView.setVrModeButtonEnabled(true);
        panoWidgetView.setEventListener(new ActivityEventListener());

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);


        // Initial launch of the app or an Activity recreation due to rotation.
        handleIntent(getIntent());
    }



    /**
     * Called when the Activity is already running and it's given a new intent.
     */
    @Override
    protected void onNewIntent(Intent intent) {
        initRecyclers();
        Log.v("This", "onNewIntent-----------------------------------------------------");
        Log.i(TAG, this.hashCode() + ".onNewIntent()");
        // Save the intent. This allows the getIntent() call in onCreate() to use this new Intent during
        // future invocations.
        setIntent(intent);
        // Load the new image.
        handleIntent(intent);
    }

    void initRecyclers(){
        buf = 0;
        bufL = 0;
        for(int i=0; i<5; i++){
            if(BitmapRecycler[i]!=null){
                BitmapRecycler[i].recycle();
            }
            if(ImageLoaderRecycler[i]!=null){
                ImageLoaderRecycler[i].cancel(true);
            }
        }
    }

    void BitmapRecycler(Bitmap recycleThis){
        int previousBuf = 0;
        if(buf > 4){
            buf = 0;
        }
        BitmapRecycler[buf] = recycleThis;
        if(buf == 0){
            previousBuf = 3;
        }else if (buf == 1){
            previousBuf = 4;
        }else{
            previousBuf = buf - 2;
        }
        if (BitmapRecycler[previousBuf] != null){
            BitmapRecycler[previousBuf].recycle();
        }
        buf++;
    }

    void ImageLoaderRecycler(ImageLoaderTask recycleThis){
        int previousBuf = 0;
        if(bufL > 4){
            bufL = 0;
        }
        ImageLoaderRecycler[bufL] = recycleThis;
        if(bufL == 0){
            previousBuf = 3;
        }else if (bufL == 1){
            previousBuf = 4;
        }else{
            previousBuf = bufL - 2;
        }
        if (ImageLoaderRecycler[previousBuf] != null){
            ImageLoaderRecycler[previousBuf].cancel(true);
        }
        bufL++;
    }


    /**
     * Load custom images based on the Intent or load the default image. See the Javadoc for this
     * class for information on generating a custom intent via adb.
     */
    private void handleIntent(Intent intent) {
        // Determine if the Intent contains a file to load.
        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            Log.i(TAG, "ACTION_VIEW Intent recieved");

            fileUri = intent.getData();
            if (fileUri == null) {
                Log.w(TAG, "No data uri specified. Use \"-d /path/filename\".");
            } else {
                Log.i(TAG, "Using file " + fileUri.toString());
            }

            panoOptions.inputType = intent.getIntExtra("inputType", Options.TYPE_MONO);
            Log.i(TAG, "Options.inputType = " + panoOptions.inputType);
        } else {
            Log.i(TAG, "Intent is not ACTION_VIEW. Using default pano image.");
            fileUri = null;
            panoOptions.inputType = Options.TYPE_MONO;
        }

        // Load the bitmap in a background thread to avoid blocking the UI thread. This operation can
        // take 100s of milliseconds.
        if (backgroundImageLoaderTask != null) {
            // Cancel any task from a previous intent sent to this activity.
            backgroundImageLoaderTask.cancel(true);
        }

        timer = new Timer();
        timer.scheduleAtFixedRate(new imageTimerTask(), 0,580000);
    }

    class imageTimerTask extends TimerTask {
        public void run(){

            if (notFirst) {
                ImageLoaderRecycler(backgroundImageLoaderTask);
                Log.v("notFirst", "Made it notFirst!!");
            }

            backgroundImageLoaderTask = new ImageLoaderTask();
            backgroundImageLoaderTask.execute(Pair.create(fileUri, panoOptions));
            notFirst = true;
        }
    }

    public boolean isLoadImageSuccessful() {
        Log.v("This", "isLoadImageSuccess-----------------------------------------------------");
        return loadImageSuccessful;
    }

    @Override
    protected void onPause() {
        //initRecyclers();
        Log.v("This", "onPause-----------------------------------------------------");

        panoWidgetView.pauseRendering();
        super.onPause();
    }

    @Override
    protected void onResume() {
        // initRecyclers();
        Log.v("This", "onResume---------------------------------------------------");
        super.onResume();
        panoWidgetView.resumeRendering();
    }

    @Override
    protected void onDestroy() {
        timer.cancel();

        // initRecyclers();
        Log.v("This", "onDestroy-------------------------------------------------");

        // Destroy the widget and free memory.
        panoWidgetView.shutdown();

        // The background task has a 5 second timeout so it can potentially stay alive for 5 seconds
        // after the activity is destroyed unless it is explicitly cancelled.
        if (backgroundImageLoaderTask != null) {
            backgroundImageLoaderTask.cancel(true);
        }

        super.onDestroy();

    }

    /**
     * Helper class to manage threading.
     */
    class ImageLoaderTask extends AsyncTask<Pair<Uri, Options>, Void, Boolean> {

        /**
         * Reads the bitmap from disk in the background and waits until it's loaded by pano widget.
         */
        @Override
        protected Boolean doInBackground(Pair<Uri, Options>... fileInformation) {
            Options panoOptions = null;  // It's safe to use null VrPanoramaView.Options.
            InputStream istr = null;
            if (fileInformation == null || fileInformation.length < 1
                    || fileInformation[0] == null || fileInformation[0].first == null) {
                AssetManager assetManager = getAssets();
                try {

                    panoImageRotater();

                    istr = assetManager.open(panoFile);
                    panoOptions = new Options();
                    panoOptions.inputType = Options.TYPE_MONO;
                } catch (IOException e) {
                    Log.e(TAG, "Could not decode default bitmap: " + e);
                    return false;
                }
            } else {
                try {
                    istr = new FileInputStream(new File(fileInformation[0].first.getPath()));
                    panoOptions = fileInformation[0].second;
                } catch (IOException e) {
                    Log.e(TAG, "Could not load file: " + e);
                    return false;
                }
            }

            currentImage = BitmapFactory.decodeStream(istr);
            BitmapRecycler(currentImage);
            panoWidgetView.loadImageFromBitmap(currentImage, panoOptions);

            try {
                istr.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close input stream: " + e);
            }

            return true;
        }
    }

    void panoImageRotater(){
        /*panoFileArray[0] = "test1.JPG";
        panoFileArray[1] = "test20.JPG";
        panoFileArray[2] = "test3.JPG";
        panoFileArray[3] = "test4.JPG";
        panoFileArray[4] = "test5.JPG";
        panoFileArray[5] = "test19.JPG";
        panoFileArray[6] = "test7.JPG";
        panoFileArray[7] = "test8.JPG";
        panoFileArray[8] = "test9.JPG";
        panoFileArray[9] = "test10.JPG";
        panoFileArray[10] = "test11.JPG";
        panoFileArray[11] = "test12.JPG";
        panoFileArray[12] = "test13.JPG";
        panoFileArray[13] = "test14.JPG";
        panoFileArray[14] = "test15.JPG";
        panoFileArray[15] = "test16.JPG";
        panoFileArray[16] = "test17.JPG";
        panoFileArray[17] = "test18.JPG";*/

        if (panoFileArray.length-1 == panoFileCounter){
            panoFileCounter = 0;

        }else{
            panoFileCounter ++;

        }
        panoFile = panoFileArray[panoFileCounter];
    }


    /**
     * Listen to the important events from widget.
     */
    private class ActivityEventListener extends VrPanoramaEventListener {
        /**
         * Called by pano widget on the UI thread when it's done loading the image.
         */

        @Override
        public void onLoadSuccess() {
            loadImageSuccessful = true;
        }

        /**
         * Called by pano widget on the UI thread on any asynchronous error.
         */
        @Override
        public void onLoadError(String errorMessage) {
            loadImageSuccessful = false;
            Toast.makeText(
                    SimpleVrPanoramaActivityWithInput.this, "Error loading pano: " + errorMessage, Toast.LENGTH_LONG)
                    .show();
            Log.e(TAG, "Error loading pano: " + errorMessage);
        }

        @Override
        public void onClick(){

            if (notFirst) {
                ImageLoaderRecycler(backgroundImageLoaderTask);
                Log.v("notFirst", "Made it notFirst!!");
            }

            backgroundImageLoaderTask = new ImageLoaderTask();
            backgroundImageLoaderTask.execute(Pair.create(fileUri, panoOptions));
            notFirst = true;



            Log.i(TAG, "onCardboardTrigger");
            // Always give user feedback
            vibrator.vibrate(50);
        }
    }
}
