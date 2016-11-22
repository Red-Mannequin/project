package com.redmannequin.resonance.Audio;

import com.redmannequin.resonance.Backend.Track;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.TarsosDSPAudioFloatConverter;
import be.tarsos.dsp.io.TarsosDSPAudioFormat;

public class MergeAudio implements AudioProcessor {

    private byte[] buffer;
    private RandomAccessFile audio;
    private TarsosDSPAudioFormat audioFormat;
    private TarsosDSPAudioFloatConverter converter;

    public MergeAudio(Track track) {
        audioFormat = new TarsosDSPAudioFormat(track.getSampleRate(), 16, 1, true, false);
        converter = TarsosDSPAudioFloatConverter.getConverter(audioFormat);
        try {
            audio = new RandomAccessFile(track.getPath() + File.separator + track.getName() + ".pcm", "r");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean process(AudioEvent audioEvent) {
        float[] input = audioEvent.getFloatBuffer();
        buffer = new byte[input.length*2];
        float[] output = new float[input.length];
        try {
            audio.read(buffer, 0, buffer.length);
            converter.toFloatArray(buffer, output, output.length);
            for (int i=0; i < input.length; ++i) {
                input[i] += output[i];
                //input[i] /= 2;
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void processingFinished() {
        try {
            audio.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
