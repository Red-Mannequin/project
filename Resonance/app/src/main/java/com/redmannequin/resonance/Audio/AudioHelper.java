package com.redmannequin.resonance.Audio;


public class AudioHelper {

    public static byte[] short2byte(short[] buffer) {
        byte temp[] = new byte[buffer.length*2];
        for (int i=0; i < buffer.length; ++i) {
            temp[i*2] = (byte) (buffer[i] & 0xFF);
            temp[(i*2)+1] =  (byte) (buffer[i] >> 8);
        }
        return temp;
    }

    public static short[] byte2short(byte[] buffer) {
        short temp[] = new short[buffer.length/2];
        for (int i=0; i < temp.length; ++i) {
            temp[i] = (short)(buffer[i*2]&0xFF);
            temp[i] |= (short)((buffer[(i*2)+1]&0xFF)<<8);
        }
        return temp;
    }
}
