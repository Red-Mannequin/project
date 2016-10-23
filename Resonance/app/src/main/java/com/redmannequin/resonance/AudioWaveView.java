package com.redmannequin.resonance;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public class AudioWaveView extends View {

    private Paint paint;
    private Paint waveBrush;

    private Rect mRect;
    private int length;
    private int curr;
    private float b;

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

        mRect = new Rect();

    }

    public void setLength(int l) {length = l;}

    public void update(float c) {
        b = c;
        invalidate();
    }

    @Override
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);
        canvas.drawRect(0,0,getWidth(),getHeight(), paint);
        canvas.drawRect(0,0, getWidth()*b, getHeight(), waveBrush);
    }

}