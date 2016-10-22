package com.redmannequin.resonance;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.redmannequin.resonance.BackendTesting.TrackB;
import com.redmannequin.resonance.Effects.Effect1;
import com.redmannequin.resonance.Effects.Effect2;
import com.redmannequin.resonance.Effects.Effect3;

import com.redmannequin.resonance.BackendTesting.Backend;
import com.redmannequin.resonance.BackendTesting.Project;
import com.redmannequin.resonance.BackendTesting.TrackB;

public class MainActivity extends AppCompatActivity {

    private MainActivity.SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    private Button play_button;
    private Button stop_button;
    private AudioWaveView waveView;
    private Intent intent;

    private TrackB track;
    private Project project;
    private Backend backend;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        track = getIntent().getParcelableExtra("track");
        project = getIntent().getParcelableExtra("project");
        backend = getIntent().getParcelableExtra("backend");
        setTitle(track.getName());

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        // Create the adapter that will return a fragment for each of the effect windows.
        mSectionsPagerAdapter = new MainActivity.SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        final Track audio = new Track();

        final Button play_button = (Button) findViewById(R.id.play_button);
        play_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                audio.togglePlay();
            }
        });

        // listener for the stop button
        stop_button = (Button) findViewById(R.id.stop_button);
        stop_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                audio.stop();
            }
        });

        // used to update waveView
        waveView = (AudioWaveView) findViewById(R.id.track_wave_view);
    }

    @Override
    public void onBackPressed() {
        finish();
        // send changes back to previous activity
        intent = new Intent(this, TrackListView.class);
        intent.putExtra("project", project);
        intent.putExtra("backend", backend);
        startActivity(intent);
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
