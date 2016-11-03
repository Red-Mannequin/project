package com.redmannequin.resonance.Views;

import android.content.Intent;
import android.media.MediaRecorder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import com.redmannequin.resonance.R;

import java.io.IOException;

public class RecordTrackView extends AppCompatActivity {

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
        recorder = new MediaRecorder();
        initRecorder("path");

        try {
            recorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        record = (Button) findViewById(R.id.record_button);
        save = (Button) findViewById(R.id.save_button);
        setListeners();

    }

    private void setListeners() {

        record.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (status == 0 ) {
                    recorder.start();
                    status = 1;
                } else if (status == 1) {
                    recorder.stop();
                    status = 2;
                } else {
                    recorder.reset();
                    initRecorder("path");
                    recorder.start();
                    status = 1;
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                recorder.reset();
                recorder.release();
                onBackPressed();
            }
        });
    }

    private void initRecorder(String path) {
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile(path);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("path", "path");
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }

}
