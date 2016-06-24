package com.example.android.laurentianvrapp;

import com.example.android.laurentianvrapp.R;
import com.google.vr.sdk.widgets.video.VrVideoEventListener;
import com.google.vr.sdk.widgets.video.VrVideoView;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Timer;
import java.io.IOException;


public class SimpleVrVideoActivityWithInput extends Activity {
    private static final String TAG = SimpleVrVideoActivityWithInput.class.getSimpleName();

    /**
     * Preserve the video's state when rotating the phone.
     */
    private static final String STATE_IS_PAUSED = "isPaused";
    private static final String STATE_PROGRESS_TIME = "progressTime";
    /**
     * The video duration doesn't need to be preserved, but it is saved in this example. This allows
     * the seekBar to be configured during {@link #onRestoreInstanceState(Bundle)} rather than waiting
     * for the video to be reloaded and analyzed. This avoid UI jank.
     */
    private static final String STATE_VIDEO_DURATION = "videoDuration";
    int i = 0;
    int j = 0;
    int x = 0;
    private boolean firstLoadCheck = false;
    /**
     * Arbitrary constants and variable to track load status. In this example, this variable should
     * only be accessed on the UI thread. In a real app, this variable would be code that performs
     * some UI actions when the video is fully loaded.
     */
    public static final int LOAD_VIDEO_STATUS_UNKNOWN = 0;
    public static final int LOAD_VIDEO_STATUS_SUCCESS = 1;
    public static final int LOAD_VIDEO_STATUS_ERROR = 2;

    private int loadVideoStatus = LOAD_VIDEO_STATUS_UNKNOWN;

    public int getLoadVideoStatus() {
        return loadVideoStatus;
    }

    /** Tracks the file to be loaded across the lifetime of this app. **/
    private Uri fileUri;
    private VideoLoaderTask backgroundVideoLoaderTask;

    private VrVideoView videoWidgetView;

    private SeekBar seekBar;
    private long ms;
    private long startTime;
    private boolean start = false;
    /**
     * By default, the video will start playing as soon as it is loaded. This can be changed by using
     * {@link VrVideoView#pauseVideo()} after loading the video.
     */
    private boolean isPaused = false;

    //Variables to cycle videos
    String[] listOfVideos;
    String videoName;
    int videoNumber = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_layout);

        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        seekBar = (SeekBar) findViewById(R.id.seek_bar);

        //Pulling array of videos from Main
        Intent intent = getIntent();
        listOfVideos = intent.getStringArrayExtra("listOfFiles");
        videoName = listOfVideos[videoNumber];
        // Bind input and output objects for the view.
        videoWidgetView = (VrVideoView) findViewById(R.id.video_view);
        videoWidgetView.setFullscreenButtonEnabled(false);
        videoWidgetView.setVrModeButtonEnabled(true);
        videoWidgetView.setEventListener(new ActivityEventListener());

        loadVideoStatus = LOAD_VIDEO_STATUS_UNKNOWN;

                    handleIntent(getIntent());

        // Initial launch of the app or an Activity recreation due to rotation.

    }

    /**
     * Called when the Activity is already running and it's given a new intent.
     */
    @Override
    protected void onNewIntent(Intent intent) {
        Log.i(TAG, this.hashCode() + ".onNewIntent()");
        // Save the intent. This allows the getIntent() call in onCreate() to use this new Intent during
        // future invocations.
        setIntent(intent);
        // Load the new image.
        handleIntent(intent);
    }

    /**
     * Load custom videos based on the Intent or load the default video. See the Javadoc for this
     * class for information on generating a custom intent via adb.
     */
    private void handleIntent(Intent intent) {
        // Determine if the Intent contains a file to load.
        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            Log.i(TAG, "ACTION_VIEW Intent received");

            fileUri = intent.getData();
            if (fileUri == null) {
                Log.w(TAG, "No data uri specified. Use \"-d /path/filename\".");
            } else {
                Log.i(TAG, "Using file " + fileUri.toString());
            }
        } else {
            Log.i(TAG, "Intent is not ACTION_VIEW. Using the default video.");
            fileUri = null;
        }

        // Load the bitmap in a background thread to avoid blocking the UI thread. This operation can
        // take 100s of milliseconds.
        if (backgroundVideoLoaderTask != null) {
            // Cancel any task from a previous intent sent to this activity.
            Log.v("h", "Here4: " + j  );
            backgroundVideoLoaderTask.cancel(true);
            Log.v("h", "Here4Pair: " + j  );
        }
        Log.v("h", "Here5Pair: " + j  );
        backgroundVideoLoaderTask = new VideoLoaderTask();
        Log.v("h", "Here5Pair: " + j  );

        Log.v("h", "Here6: " + j  );
        backgroundVideoLoaderTask.execute(fileUri);
        Log.v("h", "Here6Pair: " + j  );


    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        Log.v("h", "onSaveInstanceState1: " + j  );
        savedInstanceState.putLong(STATE_PROGRESS_TIME, videoWidgetView.getCurrentPosition());
        Log.v("h", "onSaveInstanceState2: " + j  );
        savedInstanceState.putLong(STATE_VIDEO_DURATION, videoWidgetView.getDuration());
        Log.v("h", "onSaveInstanceState3: " + j  );
        savedInstanceState.putBoolean(STATE_IS_PAUSED, isPaused);
        Log.v("h", "onSaveInstanceState4: " + j  );
        super.onSaveInstanceState(savedInstanceState);
        Log.v("h", "onSaveInstanceState5: " + j  );
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);
        long progressTime = savedInstanceState.getLong(STATE_PROGRESS_TIME);
        videoWidgetView.seekTo(progressTime);
        seekBar.setMax((int) savedInstanceState.getLong(STATE_VIDEO_DURATION));
        seekBar.setProgress((int) progressTime);


        isPaused = savedInstanceState.getBoolean(STATE_IS_PAUSED);
        if (isPaused) {
            videoWidgetView.pauseVideo();
        }
    }

    @Override
    protected void onPause() {
        Log.v("h", "onPause: " + j  );
        super.onPause();
        // Prevent the view from rendering continuously when in the background.
        videoWidgetView.pauseRendering();
        // If the video is playing when onPause() is called, the default behavior will be to pause
        // the video and keep it paused when onResume() is called.
        isPaused = true;
    }

    @Override
    protected void onResume() {
        Log.v("h", "onResume: " + j  );
        super.onResume();
        // Resume the 3D rendering.
        if (firstLoadCheck) {
            videoWidgetView.resumeRendering();
        }
        // Update the text to account for the paused video in onPause().
        //updateStatusText();
    }

    @Override
    protected void onDestroy() {
        Log.v("h", "onDestroy: " + j  );
        // Destroy the widget and free memory.
        videoWidgetView.shutdown();
        super.onDestroy();
    }

    private void togglePause() {
        Log.v("h", "togglePause: " + j  );
        if (isPaused) {
            videoWidgetView.playVideo();
        } else {
            videoWidgetView.pauseVideo();
        }
        isPaused = !isPaused;
        //updateStatusText();
    }

    /**
     * Listen to the important events from widget.
     */
    private class ActivityEventListener extends VrVideoEventListener  {
        /**
         * Called by video widget on the UI thread when it's done loading the video.
         */
        @Override
        public void onLoadSuccess() {
            Log.i(TAG, "Successfully loaded video " + videoWidgetView.getDuration());
            loadVideoStatus = LOAD_VIDEO_STATUS_SUCCESS;
            seekBar.setMax((int) videoWidgetView.getDuration());
            //updateStatusText();
        }

        /**
         * Called by video widget on the UI thread on any asynchronous error.
         */
        @Override
        public void onLoadError(String errorMessage) {
            // An error here is normally due to being unable to decode the video format.
            loadVideoStatus = LOAD_VIDEO_STATUS_ERROR;
            Toast.makeText(
                    SimpleVrVideoActivityWithInput.this, "Error loading video: " + errorMessage, Toast.LENGTH_LONG)
                    .show();
            Log.e(TAG, "Error loading video: " + errorMessage);

        }

        @Override
        public void onClick() {
            // create timer HERE!!!

            //togglePause();

            if (backgroundVideoLoaderTask != null) {
                // Cancel any task from a previous intent sent to this activity.
                Log.v("h", "Here8: " + j  );
                backgroundVideoLoaderTask.cancel(true);
                Log.v("h", "Here8Pair: " + j  );
            }

            Log.v("h", "Here9: " + j  );
            backgroundVideoLoaderTask = new VideoLoaderTask();
            Log.v("h", "Here9Pair: " + j  );

            Log.v("h", "Here10: " + j  );
            backgroundVideoLoaderTask.execute(fileUri);
            Log.v("h", "Here10Pair: " + j++  );

        }

        /**
         * Update the UI every frame.
         */
        @Override
        public void onNewFrame() {
            //updateStatusText();
            seekBar.setProgress((int) videoWidgetView.getCurrentPosition());
        }

        /**
         * Make the video play in a loop. This method could also be used to move to the next video in
         * a playlist.
         */
        @Override
        public void onCompletion() {
            videoWidgetView.seekTo(0);
        }
    }

    /**
     * Helper class to manage threading.
     */
    void cycleVideos(){
        if (videoNumber > listOfVideos.length-1){videoNumber = 0;}
        videoName = listOfVideos[videoNumber];
        videoNumber++;
    }

    class VideoLoaderTask extends AsyncTask<Uri, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Uri... uri) {

            try {
                if (uri == null || uri.length < 1 || uri[0] == null) {

                    Log.v("h", "Here1: " + j  );
                    Log.v("h", "Here1Pair: " + j++  );
                    // videoWidgetView.playVideo();
                    Log.v("h", "Here2: " + i  );
                    if (videoWidgetView != null) {
                        videoWidgetView.loadVideoFromAsset(videoName);
                        Log.v("h", "Here2PairThen" + i );
                    }
                    Log.v("h", "Here2Pair2: " + i++  );

                } else {

                    videoWidgetView.loadVideo(uri[0]);

                }

            } catch (IOException e) {
                Log.v("h", "Here3: " + x  );
                // An error here is normally due to being unable to locate the file.
                loadVideoStatus = LOAD_VIDEO_STATUS_ERROR;
                // Since this is a background thread, we need to switch to the main thread to show a toast.
                videoWidgetView.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast
                                .makeText(SimpleVrVideoActivityWithInput.this, "Error opening file. ", Toast.LENGTH_LONG)
                                .show();
                    }
                });
                Log.e(TAG, "Could not open video: " + e);
                Log.v("h", "Here3Pair: " + x++  );
            }
            Log.v("h", "Here3Pair: " + x++  );
            firstLoadCheck = true;
            cycleVideos();
            return true;
        }
    }
}
