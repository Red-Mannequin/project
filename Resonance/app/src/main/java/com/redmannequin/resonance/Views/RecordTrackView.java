package com.redmannequin.resonance.Views;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;

import com.redmannequin.resonance.Audio.AudioHelper;
import com.redmannequin.resonance.Audio.Player;
import com.redmannequin.resonance.Audio.Recorder;
import com.redmannequin.resonance.AudioWaveView;
import com.redmannequin.resonance.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class RecordTrackView extends AppCompatActivity {

    private int status;

    private Button record;
    private Button save;

    private Chronometer clock;

    private Recorder recorder;
    private Player player;
    private short buffer[];

    private Thread thread;
    private FileOutputStream outputStream;

    private AudioWaveView waveView;
    private Handler handle;
    private Runnable seek;

    private boolean isConnected;
    private HeadsetReceiver hr;

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

        recorder = new Recorder();
        player = new Player();
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
        isConnected = false;
        hr = new HeadsetReceiver();
        setListeners();
    }

    private void setListeners() {
        record.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
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

        registerReceiver(hr, new IntentFilter(Intent.ACTION_HEADSET_PLUG));
    }

    @Override
    public void onBackPressed() {
        status = 2;
        stopRecorder();
        unregisterReceiver(hr);
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
                    if (isConnected)player.write(buffer);
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
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    private class HeadsetReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra("state")){
                if (isConnected && intent.getIntExtra("state", 0) == 0){
                    isConnected = false;
                } else if (!isConnected && intent.getIntExtra("state", 0) == 1){
                    isConnected = true;
                }
            }
        }
    }


    // loads the json file for testing only
    //Input projects to open projects json file
    //Input tracks to open tracks json file
    private String loadJson(String name) {
        StringBuilder text = new StringBuilder();
        try {
            File file = new File(this.getFilesDir(), name+".json");
            if (!file.exists()) {
                file.createNewFile();
            } else {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;
                while ((line = br.readLine()) != null) {
                    text.append(line);
                    text.append('\n');
                }
                br.close();
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        return text.toString();
    }
}
