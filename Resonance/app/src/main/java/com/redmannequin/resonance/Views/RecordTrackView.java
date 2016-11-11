package com.redmannequin.resonance.Views;

import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;

import com.redmannequin.resonance.Audio.AudioHelper;
import com.redmannequin.resonance.Audio.Play;
import com.redmannequin.resonance.Audio.Record;
import com.redmannequin.resonance.AudioWaveView;
import com.redmannequin.resonance.R;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class RecordTrackView extends AppCompatActivity {

    private int status;

    private Button record;
    private Button save;

    private Chronometer clock;

    private Record recorder;
    private Play player;
    private short buffer[];

    private Thread thread;
    private FileOutputStream outputStream;

    private AudioWaveView waveView;
    private Handler handle;
    private Runnable seek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        setTitle("Record");

        status = 0;
        try {
            outputStream = new FileOutputStream(getIntent().getStringExtra("path"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        clock = (Chronometer) findViewById(R.id.chronometer2);
        record = (Button) findViewById(R.id.record_button);
        save = (Button) findViewById(R.id.save_button);
        save.setEnabled(false);

        recorder = new Record();
        player = new Play();
        recorder.init();
        player.init();

        waveView = (AudioWaveView) findViewById(R.id.record_wave_view);

        handle = new Handler();
        seek = new Runnable() {
            @Override
            public void run() {
                if (status == 1) {
                    waveView.update(buffer);
                    handle.postDelayed(this, 50);
                }
            }
        };
        setListeners();
    }

    private void setListeners() {
        record.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.w("blah", "record buttn pressed");
                if (status == 0 ) {
                    status = 1;
                    initRecorder();
                    clock.start();
                    handle.postDelayed(seek, 50);
                    record.setText("stop");
                } else if (status == 1) {
                    status = 2;
                    clock.stop();
                    save.setEnabled(true);

                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        status = 2;
        stopRecorder();
        super.onBackPressed();

    }

    private void initRecorder() {
        recorder.start();
        player.start();
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (status == 1) {
                    buffer = recorder.read();
                    player.write(buffer);
                    byte temp[] = AudioHelper.short2byte(buffer);
                    try {
                        outputStream.write(temp, 0, temp.length);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }

    private void stopRecorder() {
        try {
            thread.join();
            recorder.release();
            player.release();
            outputStream.flush();
            outputStream.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
