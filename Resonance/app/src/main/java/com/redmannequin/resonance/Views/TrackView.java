package com.redmannequin.resonance.Views;

import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.redmannequin.resonance.AudioWaveView;
import com.redmannequin.resonance.Backend.Backend;
import com.redmannequin.resonance.Backend.Project;
import com.redmannequin.resonance.Backend.Track;
import com.redmannequin.resonance.Effects.Effect1;
import com.redmannequin.resonance.Effects.Effect2;
import com.redmannequin.resonance.Effects.Effect3;
import com.redmannequin.resonance.R;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

public class TrackView extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    // ui elements
    private Button play_button;
    private Button stop_button;
    private AudioWaveView waveView;

    // backend
    private int trackID;
    private int projectID;
    private Track track;
    private Project project;
    private Backend backend;

    // audio player
    private RandomAccessFile randomAccessFile;
    private AudioTrack audioTrack;
    private Thread thread;

    private Handler handle;
    private Runnable seek;

    private int freq;
    private int channel;
    private int format;
    private int output;
    private int mode;
    private int bufferSize;
    private byte[] buffer;
    private boolean playing;
    private boolean ended;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);

        trackID = getIntent().getIntExtra("trackID", 0);
        projectID = getIntent().getIntExtra("projectID", 0);
        backend = getIntent().getParcelableExtra("backend");

        project = backend.getProject(projectID);
        track = project.getTrack(trackID);

        setTitle(track.getName());

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        // Create the adapter that will return a fragment for each of the effect windows.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // wave view
        waveView = (AudioWaveView) findViewById(R.id.track_wave_view);

        // audio playback settings
        freq = 8000;
        channel = AudioFormat.CHANNEL_IN_STEREO;
        format = AudioFormat.ENCODING_PCM_16BIT;
        output = AudioManager.USE_DEFAULT_STREAM_TYPE;
        mode = AudioTrack.MODE_STREAM;

        bufferSize = AudioRecord.getMinBufferSize(freq, channel, format);
        audioTrack = new AudioTrack(output, freq, channel, format, bufferSize, mode);
        audioTrack.setPlaybackRate(freq);
        buffer = new byte[bufferSize];
        playing = false;
        ended = false;

        try {
            randomAccessFile = new RandomAccessFile(track.getPath(), "r");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        handle = new Handler();
        seek = new Runnable() {
            @Override
            public void run() {
                if (playing) {
                    waveView.update(buffer);
                    handle.postDelayed(this, 200);
                }
            }
        };

        // set up buttons
        play_button = (Button) findViewById(R.id.play_button);
        stop_button = (Button) findViewById(R.id.stop_button);
        setupListeners();

    }

    private void setupListeners() {
        play_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                togglePlay();
            }
        });

        stop_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                stop();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (audioTrack.getPlayState() == AudioTrack.PLAYSTATE_PLAYING) {
            stop();
        } else {
            closePlay();
            Intent intent = new Intent();
            intent.putExtra("projectID", projectID);
            intent.putExtra("backend", backend);
            setResult(RESULT_OK, intent);
            super.onBackPressed();
        }
    }

    private void togglePlay() {
        if (playing == false) {
            handle.postDelayed(seek, 200);
            if (ended){
                try {
                    randomAccessFile.seek(0);
                    ended = false;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            audioTrack.play();
            initPlay();
        } else {
            playing = false;
            try {
                thread.join();
                if (ended) {
                    randomAccessFile.seek(0);
                    ended = false;
                }
            } catch (InterruptedException e) {
                    e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void stop(){
        playing = false;
        try {
            thread.join();
            audioTrack.pause();
            audioTrack.flush();
            randomAccessFile.seek(0);
            ended = false;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closePlay() {
        audioTrack.stop();
        audioTrack.release();
        try {
            randomAccessFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void initPlay() {
        playing = true;
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while ( playing) {
                        if (randomAccessFile.read(buffer, 0, bufferSize) > -1) {
                            audioTrack.write(buffer, 0, buffer.length);
                        } else {
                            ended = true;
                            playing = false;
                        }
                    }
                    audioTrack.pause();
                } catch (IOException e) {
                }
            }
        });
        thread.start();
    }

    //Returns pages/effects specified by the ViewPager
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment[] effects;
            effects = new Fragment[3];
            effects[0] = Effect1.getFragment();
            effects[1] = Effect2.getFragment();
            effects[2] = Effect3.getFragment();
            return effects[position];
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }

}
