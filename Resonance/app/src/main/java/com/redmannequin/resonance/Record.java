package com.redmannequin.resonance;

import android.media.MediaRecorder;
import android.media.MediaPlayer;
import java.io.IOException;
import android.util.Log;

/**
 * Created by Matthew on 10/8/2016.
 */

public class Record {

    private static final String LOG_TAG = "Recording";

    private MediaRecorder recorder = null;
    private MediaPlayer   mPlayer = null;

    public int status = 0;

    //toggleRecord will start an audio recording if it's not already running
    //It will end a recording if it is already recording
    //Returns true when recording is done - this is so track can be loaded into media player
    public boolean toggleRecord(Track track) {
        if (status == 1) {
            endRecording();
            return true;
        } else {
            startRecording(track);
            return false;
        }
    }

    //Creates a new MediaRecorder object and starts the recording
    public void startRecording(Track track) {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile(track.track);

        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        recorder.start();
        status = 1;
    }

    //Ends the recording that was started in startRecording()
    public void endRecording() {
        recorder.stop();
        recorder.reset();   // You can reuse the object by going back to setAudioSource() step
        recorder.release();
        status = 0;
    }
}