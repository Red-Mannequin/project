package com.redmannequin.resonance.Views;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.redmannequin.resonance.Audio.AudioEffect;
import com.redmannequin.resonance.Audio.AudioHelper;
import com.redmannequin.resonance.Audio.MediaPlayer;
import com.redmannequin.resonance.AudioWaveView;
import com.redmannequin.resonance.Backend.Backend;
import com.redmannequin.resonance.Backend.Project;
import com.redmannequin.resonance.Backend.Track;
import com.redmannequin.resonance.Effects.Effect1;
import com.redmannequin.resonance.Effects.Effect2;
import com.redmannequin.resonance.Effects.Effect3;
import com.redmannequin.resonance.R;

import java.io.File;
import java.io.IOException;

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

    private MediaPlayer player;

    private Handler handle;
    private Runnable seek;

    private AudioEffect audioEffect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);

        trackID = getIntent().getIntExtra("trackID", 0);
        projectID = getIntent().getIntExtra("projectID", 0);
        backend = getIntent().getParcelableExtra("backend");

        project = backend.getProject(projectID);
        track = project.getTrack(trackID);
        player = new MediaPlayer();

        audioEffect = new AudioEffect();
        audioEffect.init(track);
        audioEffect.DelayEffect(0.1, 0);

        player.init(track.getPath() + File.separator + track.getName() + "_delay.wav");

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

        handle = new Handler();
        seek = new Runnable() {
            @Override
            public void run() {
                if (player.isPlaying()) {
                    short temp[] = AudioHelper.byte2short(player.getBuffer());
                    waveView.update(temp);
                    handle.postDelayed(this, 50);
                } else {
                    play_button.setText("play");
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
                if(play_button.getText().toString().equals("play")) {
                    player.play();
                    play_button.setText("pause");
                    handle.postDelayed(seek, 50);
                } else {
                    player.pause();
                    play_button.setText("play");
                }
            }
        });

        stop_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                player.stop();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (player.isPlaying()) {
            player.stop();
        } else {
            player.destroy();
            Intent intent = new Intent();
            intent.putExtra("projectID", projectID);
            intent.putExtra("backend", backend);
            setResult(RESULT_OK, intent);
            super.onBackPressed();
        }
    }

    public void setDelay(double input) {
        audioEffect.DelayEffect(input, 0.5);
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
