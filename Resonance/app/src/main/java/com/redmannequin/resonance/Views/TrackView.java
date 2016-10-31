package com.redmannequin.resonance.Views;

import android.content.Intent;
import android.media.AudioTrack;
import android.media.MediaPlayer;
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
    private MediaPlayer mPlayer;
    private Handler handle;
    private Runnable seek;

    private AudioTrack audio;

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

        mPlayer = MediaPlayer.create(this, R.raw.rapping2u);
        waveView = (AudioWaveView) findViewById(R.id.track_wave_view);
        waveView.setLength(mPlayer.getDuration());

        play_button = (Button) findViewById(R.id.play_button);
        stop_button = (Button) findViewById(R.id.stop_button);
        setupListeners();

        handle = new Handler();
        seek = new Runnable() {
            @Override
            public void run() {
                if (mPlayer.isPlaying()) {
                    float b = mPlayer.getCurrentPosition() / (float)mPlayer.getDuration();
                    waveView.update(b);
                    handle.postDelayed(this, 200);
                }
            }
        };

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
        if (mPlayer.isPlaying()) {
            stop();
        } else {
            mPlayer.stop();
            mPlayer.reset();
            mPlayer.release();
            Intent intent = new Intent();
            intent.putExtra("projectID", projectID);
            intent.putExtra("backend", backend);
            setResult(RESULT_OK, intent);
            super.onBackPressed();
        }
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

    public void togglePlay() {

        if (!mPlayer.isPlaying()) {
            mPlayer.start();
            handle.postDelayed(seek, 100);
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
        mPlayer.seekTo(0);
        mPlayer.pause();
        waveView.update(0);
    }

}
