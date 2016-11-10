package com.redmannequin.resonance;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import java.util.Arrays;

public class AudioWaveView extends View {

    private Paint paint;
    private Paint waveBrush;

    private Rect mRect;
    private byte[] buffer;
    private float[] mPoints;

    public AudioWaveView(Context context) {
        super(context);
        init();
    }

    public AudioWaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AudioWaveView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.GREEN);

        waveBrush = new Paint();
        waveBrush.setColor(Color.BLUE);
        waveBrush.setAntiAlias(true);

        mRect = new Rect(0,0,getWidth(), getHeight());

    }


    public void update(byte[] c) {
        buffer = new byte[c.length];
        System.arraycopy(c, 0, buffer, 0, c.length);
        invalidate();
    }

    @Override
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);
        canvas.drawRect(0,0,getWidth(),getHeight(), paint);

        if (buffer == null) return;

        if (mPoints == null || mPoints.length < buffer.length * 4) {
            mPoints = new float[buffer.length * 4];
        }

        for (int i = 0; i < buffer.length - 1; i++) {
            mPoints[i * 4 + 0] = getWidth() * i / (buffer.length - 1);
            mPoints[i * 4 + 1] = getHeight() / 2 + ((byte) (buffer[i] + 128)) * (getHeight() / 2) / 128;
            mPoints[i * 4 + 2] = getWidth() * (i + 1) / (buffer.length - 1);
            mPoints[i * 4 + 3] = getHeight() / 2 + ((byte) (buffer[i + 1] + 128)) * (getHeight() / 2) / 128;
        }
        canvas.drawLines(mPoints, waveBrush);
    }

}