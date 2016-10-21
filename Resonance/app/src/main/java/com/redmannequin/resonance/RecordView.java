package com.redmannequin.resonance;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class RecordView extends AppCompatActivity {

    private static final int ZERO = 0;
    private static final int RECORDING = 1;

    private Button record_button;
    private TextView timer;

    private int state;
    private long startTime;
    private Handler handle;

    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_view);

        state = 0;
        startTime = 0;
        handle = new Handler();

        runnable = new Runnable() {
            @Override
            public void run() {
                if(state == RECORDING) {
                    long millis = System.currentTimeMillis() - startTime;
                    long seconds = millis / 1000;
                    timer.setText(String.format("%02d:%02d:%02d", seconds/60, seconds%60, millis%100));
                }
            }
        };

        record_button = (Button) findViewById(R.id.record_button);
        record_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                switch (state) {
                    case ZERO:
                        state = RECORDING;
                        startTime = System.currentTimeMillis();
                        handle.postDelayed(runnable, 10L);
                        break;

                    case RECORDING:
                        state = ZERO;
                        handle.removeCallbacks(runnable);
                        break;
                    default:
                        break;
                }
            }
        });

        timer = (TextView) findViewById(R.id.record_timer);

    }
}
