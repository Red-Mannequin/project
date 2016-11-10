package com.redmannequin.resonance.Views;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;

import com.redmannequin.resonance.AudioWaveView;
import com.redmannequin.resonance.R;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class RecordTrackView extends AppCompatActivity {

    private String path;
    private int status;

    private Button record;
    private Button save;

    private Chronometer clock;

    private AudioTrack audioTrack;
    private AudioRecord audioRecord;
    private Thread thread;
    private FileOutputStream outputStream;

    private int freq;
    private int channel;
    private int format;
    private int source;
    private int output;
    private int mode;
    private int bufferSize;
    private byte[] buffer;

    private AudioWaveView waveView;
    private Handler handle;
    private Runnable seek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        setTitle("Record");

        status = 0;
        path = getIntent().getStringExtra("path");

        clock = (Chronometer) findViewById(R.id.chronometer2);
        record = (Button) findViewById(R.id.record_button);
        save = (Button) findViewById(R.id.save_button);
        save.setEnabled(false);

        freq = 8000;
        channel = AudioFormat.CHANNEL_IN_STEREO;
        format = AudioFormat.ENCODING_PCM_16BIT;
        source = MediaRecorder.AudioSource.MIC;
        output = AudioManager.USE_DEFAULT_STREAM_TYPE;
        mode = AudioTrack.MODE_STREAM;

        bufferSize = AudioRecord.getMinBufferSize(freq, channel, format);
        audioRecord = new AudioRecord(source, freq, channel, format, bufferSize);
        audioTrack = new AudioTrack(output, freq, channel, format, bufferSize, mode);
        audioTrack.setPlaybackRate(freq);

        waveView = (AudioWaveView) findViewById(R.id.record_wave_view);

        handle = new Handler();
        seek = new Runnable() {
            @Override
            public void run() {
                if (status == 1) {
                    waveView.update(buffer);
                    handle.postDelayed(this, 200);
                }
            }
        };

        buffer = new byte[bufferSize];
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
                    handle.postDelayed(seek, 200);
                    record.setText("stop");
                } else if (status == 1) {
                    status = 2;
                    stopRecorder();
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

    private void initRecorder() {
        audioRecord.startRecording();;
        audioTrack.play();
        try {
            outputStream = new FileOutputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (status == 1) {
                    audioRecord.read(buffer, 0, bufferSize);
                    audioTrack.write(buffer, 0, buffer.length);
                    try {
                        outputStream.write(buffer, 0, buffer.length);
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
            audioTrack.stop();
            audioRecord.stop();
            audioRecord.release();
            outputStream.flush();
            outputStream.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
