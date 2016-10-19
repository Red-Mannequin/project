package com.redmannequin.resonance;

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

import com.redmannequin.resonance.Effects.Effect1;
import com.redmannequin.resonance.Effects.Effect2;
import com.redmannequin.resonance.Effects.Effect3;

public class MainActivity extends AppCompatActivity {

    private MainActivity.SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        final Button stop_button = (Button) findViewById(R.id.stop_button);
        stop_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                audio.stop();
            }
        });

        final Button record_button = (Button) findViewById(R.id.record_button);
        record_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                audio.toggleRecord();
            }
        });


    }

    //Returns pages/effects specified by the ViewPager
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment out;
            if(position == 0) {
                out = Effect1.getFragment();
            } else if(position == 1) {
                out = Effect2.getFragment();
            } else {
                out = Effect3.getFragment();
            }
            return out;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }
}
