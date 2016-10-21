package com.redmannequin.resonance;

import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import java.io.File;
import android.media.MediaPlayer;

/**
 * Created by Matthew on 10/20/2016.
 */

public class Track extends Activity {

    //Default filePath for new file
    public String filePath = (Environment.getExternalStorageDirectory() + File.separator + Environment.DIRECTORY_DCIM + File.separator);
    public String fileName = "input.mp4";
    public String track = (filePath + fileName);
    Record recording = new Record();

    MediaPlayer mPlayer = new MediaPlayer();

    int PICKFILE_REQUEST_CODE=3;

    //Records a track under the filepath + filename in this object
    public void toggleRecord () {
        if (recording.toggleRecord(this) == true) {
            save(filePath, fileName);
        }
    }

    //Takes file from default filepath and puts it in "path"
    public void save(String path) {
        File from = new File(track);
        File to = new File(path + fileName);
        track = path + fileName;
        from.renameTo(to);
        setTrack(track);
    }

    //Takes file from current filepath and puts it in "path" with new name
    public void save(String path, String name) {
        File from = new File(track);
        File to = new File(path + name);
        track = path + name;
        from.renameTo(to);
        setTrack(track);
    }

    public void setTrack(String track) {
        try {
            mPlayer.setDataSource(track);//Write your location here
            mPlayer.prepare();
            mPlayer.setLooping(true);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void togglePlay() {

        if (!mPlayer.isPlaying()) {
            mPlayer.start();
        }
        else {
            if (mPlayer.getDuration() == mPlayer.getCurrentPosition()) {
                stop();
            }
            else {
                mPlayer.pause();
            }
        }
    }

    public void stop() {
        mPlayer.pause();
        mPlayer.seekTo(0);
    }
}
