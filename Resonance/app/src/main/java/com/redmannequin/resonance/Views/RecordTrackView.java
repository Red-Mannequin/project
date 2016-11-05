package com.redmannequin.resonance.Views;

import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import com.redmannequin.resonance.R;

import java.io.File;
import java.io.IOException;

public class RecordTrackView extends AppCompatActivity {

    private String path;
    private int status;
    private MediaRecorder recorder;

    private Button record;
    private Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        setTitle("Record");

        status = 0;
        path = getIntent().getStringExtra("path");

        record = (Button) findViewById(R.id.record_button);
        save = (Button) findViewById(R.id.save_button);
        setListeners();

    }

    private void setListeners() {

        record.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.w("blah", "record buttn pressed");
                if (status == 0 ) {
                    initRecorder();
                    status = 1;
                } else if (status == 1) {
                    recorder.stop();
                    recorder.reset();
                    recorder.release();
                    status = 2;
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initRecorder() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        recorder.setOutputFile(path);
        try {
            recorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        recorder.start();
    }

}
