package com.redmannequin.resonance.Audio;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaRecorder;

public class Config {

    public static int FREQUENCY = 44100;
    public static int CHANNEL_IN  = AudioFormat.CHANNEL_IN_STEREO;
    public static int CHANNEL_OUT = AudioFormat.CHANNEL_OUT_STEREO;
    public static int FORMAT    = AudioFormat.ENCODING_PCM_16BIT;
    public static int INPUT     = MediaRecorder.AudioSource.MIC;
    public static int OUTPUT    = AudioManager.STREAM_MUSIC;
    public static int MODE      = AudioTrack.MODE_STREAM;
}
