package com.redmannequin.resonance.Views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.redmannequin.resonance.Audio.Play;
import com.redmannequin.resonance.Backend.Backend;
import com.redmannequin.resonance.Backend.Project;
import com.redmannequin.resonance.R;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class TrackListView extends AppCompatActivity {

    // backend
    private int projectID;
    private Project project;
    private Backend backend;

    // listview
    private ArrayAdapter adapter;
    private ArrayList<String> trackList;

    private ListView trackView;

    // player
    private Play player;
    private RandomAccessFile[] randomAccessFile;
    private Thread thread;
    private boolean playing;
    private byte[] buffer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_load);

        // load backend
        backend = getIntent().getParcelableExtra("backend");
        projectID = getIntent().getIntExtra("projectID", 0);
        project = backend.getProject(projectID);

        setTitle(project.getName());

        // list view stuff
        trackList = new ArrayList<String>();
        trackList.addAll(project.getTrackNames());
        trackList.add("+");
        trackList.add("play");

        adapter = new ArrayAdapter(this, R.layout.activity_list, trackList);
        trackView = (ListView) findViewById(R.id.list);

        randomAccessFile = new RandomAccessFile[project.getTrackListSize()];
        for (int i=0; i < project.getTrackListSize(); ++i) {
            try {
                randomAccessFile[i] = new RandomAccessFile(project.getTrack(i).getPath(), "r");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        playing = false;
        player = new Play();
        player.init();

        // wait for track to pressed and load TrackView or NewTrack
        trackView.setAdapter(adapter);
        setListeners();

        if(project.getTrackListSize() == 0) {
            Intent intent = new Intent();
            intent = new Intent(getApplicationContext(), NewTrackView.class);
            intent.putExtra("projectID", projectID);
            intent.putExtra("backend", backend);
            startActivityForResult(intent, 0);
        }
    }

    public void setListeners() {
        trackView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == parent.getCount()-2) {
                    // switch to NewTrackView and passes project and backend
                    Intent intent = new Intent();
                    intent = new Intent(getApplicationContext(), NewTrackView.class);
                    intent.putExtra("projectID", projectID);
                    intent.putExtra("backend", backend);
                    startActivityForResult(intent, 0);
                } else if (position == parent.getCount()-1) {
                    if (trackList.get(position).equals("play")) {
                        trackList.set(position, "stop");
                        adapter.notifyDataSetChanged();
                        play();
                    } else {
                        trackList.set(position, "play");
                        adapter.notifyDataSetChanged();
                        stop();
                    }

                } else {
                    // switch to MainActivity and passes project and backend
                    Intent intent = new Intent();
                    intent = new Intent(getApplicationContext(), TrackView.class);
                    intent.putExtra("trackID", position);
                    intent.putExtra("projectID", projectID);
                    intent.putExtra("backend", backend);
                    startActivityForResult(intent, 0);
                }
            }
        });
    }

    // wait for project to pressed and load TrackListView
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            backend = data.getParcelableExtra("backend");
            projectID = data.getIntExtra("projectID", 0);
            project = backend.getProject(projectID);

            trackList.clear();
            trackList.addAll(project.getTrackNames());
            trackList.add("+");
            trackList.add("play");
            adapter.notifyDataSetChanged();
        }
    }

    // get backend from activity stack
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("backend", backend);
        setResult(RESULT_OK, intent);
        super.onBackPressed();

    }

    private void play() {
        player.start();
        playing = true;
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(playing) {
                    buffer = player.getEmptyByteBuffer();
                    for (int i=0; i < randomAccessFile.length; ++i) {
                        try {
                            byte temp[] = player.getEmptyByteBuffer();
                            randomAccessFile[i].read(temp, 0, temp.length);
                            for(int j=0; j < buffer.length; ++i) {
                                buffer[j] = temp[i];
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    player.write(buffer);
                }
            }
        });
    }

    private void stop() {

    }
}
