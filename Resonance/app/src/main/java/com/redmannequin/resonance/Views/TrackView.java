package com.redmannequin.resonance.Views;

// android imports
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.AnimRes;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

// project imports
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
import com.redmannequin.resonance.Effects.noEffect;
import com.redmannequin.resonance.R;

// java imports
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class TrackView extends AppCompatActivity {

    //Fragment elements
    private int numFragments;
    private boolean isEmpty;
    private ArrayList<Fragment> fragments;
    private FragmentPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    //Drawer elements
    private String[] mEffectTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    // ui elements
    private Button play_button;
    private Button stop_button;
    private AudioWaveView waveView;

    // paths
    private String projectJson;
    private String trackJson;

    // holds track and project ID's
    private int trackID;
    private int projectID;

    // holds backend objects
    private Track track;
    private Project project;
    private Backend backend;

    // player
    private MediaPlayer player;
    private Handler handle;
    private Runnable seek;

    // audio effect
    private AudioEffect audioEffect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);

        // load the backend
        trackJson   = loadJson("tracks");     // load track json
        projectJson = loadJson("projects");   // load project json
        backend     = new Backend(projectJson, trackJson); // init backend

        // get track and project
        trackID   = getIntent().getIntExtra("trackID", 0);
        projectID = getIntent().getIntExtra("projectID", 0);
        project   = backend.getProject(projectID);
        track     = project.getTrack(trackID);

        // set title
        setTitle(track.getName());

        // player
        player = new MediaPlayer();
        waveView = (AudioWaveView) findViewById(R.id.track_wave_view);

        // make wav file from pcm
        audioEffect = new AudioEffect(track);
        audioEffect.init();
        audioEffect.make();

        // init player with wav
        player.init(track.getPath() + File.separator + track.getName() + "_final.wav");

        // set adapter for effect fragments
        //initialize fragment list
        numFragments = 1;
        isEmpty = true;
        fragments = new ArrayList<Fragment>();
        fragments.add(noEffect.getFragment());
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        //initialize drawer elements
        setEffectTitles();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mDrawerList = (ListView) findViewById(R.id.effect_drawer);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.activity_list, mEffectTitles));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

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

    // sets up the listerners for the ui elements
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
        if (!player.isPlaying()) {
            player.destroy();

            String[] JSONfiles = backend.toWrite();
            outputToFile(JSONfiles[0], "projects");
            outputToFile(JSONfiles[1], "tracks");

            Intent intent = new Intent();
            intent.putExtra("projectID", projectID);
            setResult(RESULT_OK, intent);
            super.onBackPressed();
        } else {
            player.stop();
        }
    }

    public void setDelay(double del, double dec) {
        audioEffect.addDelayEffect(del, dec);
    }

    //Fragment management
    public void addFragment(Fragment effect) {
        if(isEmpty) {
            fragments.remove(0);
            fragments.add(effect);
            isEmpty = false;
        } else {
            fragments.add(effect);
            ++numFragments;
        }
        mSectionsPagerAdapter.notifyDataSetChanged();
    }

    public void deleteFragment(int position) {
        fragments.remove(position);
        --numFragments;
        if(numFragments == 0) {
            fragments.add(noEffect.getFragment());
            ++numFragments;
            isEmpty = true;
        }
    }

    //Returns pages/effects specified by the ViewPager
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            // Number of fragments to show
            return numFragments;
        }

    }

    private void outputToFile(String data, String name) {
        try {
            FileOutputStream file = this.openFileOutput(name + ".json", this.MODE_PRIVATE);
            file.write(data.getBytes());
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String loadJson(String name) {
        StringBuilder text = new StringBuilder();
        try {
            File file = new File(this.getFilesDir(), name+".json");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
        return text.toString();
    }

    //set Effect Titles
    public void setEffectTitles() {
        mEffectTitles = new String[3];
        mEffectTitles[0] = "Delay";
        mEffectTitles[1] = "Flanger";
        mEffectTitles[2] = "Pitch Shift";
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    /** Adds fragments in the main content view */
    private void selectItem(int position) {
        Fragment newfragment = Effect1.getFragment();
        addFragment(newfragment);
        mDrawerList.setItemChecked(position, true);
        mDrawerLayout.closeDrawer(mDrawerList);
    }
}
