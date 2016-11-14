package com.redmannequin.resonance.Audio;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class MediaPlayer {

    private RandomAccessFile randomAccessFile;
    private Thread thread;
    private Player player;

    private byte buffer[];

    private boolean isPlaying;
    private boolean hasEnded;


    public MediaPlayer() {
        randomAccessFile = null;
        thread = null;
        player = null;
        buffer = null;
        isPlaying = false;
        hasEnded = false;
    }

    // audio playback settings
    public void init(String path) {
        player = new Player();
        player.init();
        isPlaying = false;
        hasEnded = true;
        try {
            randomAccessFile = new RandomAccessFile(path, "r");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public byte[] getBuffer() {
        return buffer;
    }

    public void play() {
        player.start();
        isPlaying = true;
        if(hasEnded) {
            try {
                randomAccessFile.seek(0);
                hasEnded = false;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (isPlaying && !hasEnded) {
                    buffer = player.getEmptyByteBuffer();
                    try {
                        if (randomAccessFile.read(buffer, 0, buffer.length) == -1) {
                            hasEnded = true;
                            isPlaying = false;
                        }
                        player.write(buffer);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }

    public void pause() {
        isPlaying = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        isPlaying = false;
        hasEnded = true;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void destroy() {
        try {
            player.release();
            randomAccessFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
