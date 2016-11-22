package com.redmannequin.resonance.Views;

// android imports
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
import com.redmannequin.resonance.R;

// java imports
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class TrackView extends AppCompatActivity {

    // fragments
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

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
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // thread for waveForm view
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
}
