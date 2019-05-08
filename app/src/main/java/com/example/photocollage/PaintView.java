package com.example.photocollage;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;


public class PaintView extends View {

    public static int BRUSH_SIZE = 20;
    public static final int DEFAULT_COLOR = Color.TRANSPARENT;
    private static final float TOUCH_TOLERANCE = 4;
    private float mX, mY;
    private Path mPath;
    private Paint mPaint;
    public ArrayList<FingerPath> paths = new ArrayList<>();
    public ArrayList<Bitmap> bitmaps = new ArrayList<>();
    public ArrayList<Bitmap> undobitmaps = new ArrayList<>();
    public ArrayList<Bitmap> redobitmaps = new ArrayList<>();
    private int currentColor;
    private int strokeWidth;
    private boolean emboss;
    private boolean blur;
    private MaskFilter mEmboss;
    private MaskFilter mBlur;
    public Bitmap mBitmap;
    private Canvas mCanvas;
    private int screenheight;
    private int screenwidth;
    private int bitheight;
    private int bitwidth;
    private int x_adj;
    private int y_adj;
    private int x_adjust;
    private int y_adjust;
    private Paint mBitmapPaint = new Paint(Paint.DITHER_FLAG);

    public PaintView(Context context) {
        this(context, null);
    }

    public PaintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(DEFAULT_COLOR);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setXfermode(null);
        mPaint.setAlpha(0xff);

        mEmboss = new EmbossMaskFilter(new float[] {1, 1, 1}, 0.4f, 6, 3.5f);
        mBlur = new BlurMaskFilter(5, BlurMaskFilter.Blur.NORMAL);
    }

    // prepares the screenspace for the collage frame
    public void init(Bitmap bm) {
        mBitmap = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(), Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        mCanvas.drawBitmap(bm, 0,0,null);
        currentColor = DEFAULT_COLOR;
        strokeWidth = BRUSH_SIZE;
    }


    // sets the selected camera picture or stored image in the collage frame
    public void frameinit(Bitmap bm, int tpixelx, int tpixely, int bpixelx, int bpixely){
        Rect viewRect = new Rect(tpixelx, tpixely, bpixelx, bpixely);


        // scale down bitmap to fit in the frame
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bm, viewRect.width(), viewRect.height(), false);

        // draw bitmap to according pixel area
        mCanvas.drawBitmap(scaledBitmap, tpixelx, tpixely,null);

        currentColor = DEFAULT_COLOR;
        strokeWidth = BRUSH_SIZE;
    }

    // draws the collage frame in the designated screenspace
    public void setBitmap(Bitmap bm) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        Rect viewRect = new Rect(0, 0, width, width);

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bm, viewRect.width(), viewRect.height(), false);
        mBitmap = Bitmap.createBitmap(viewRect.width(), viewRect.height(), Bitmap.Config.ARGB_8888);

        mCanvas = new Canvas(mBitmap);
        mCanvas.drawBitmap(scaledBitmap, 0,0,null);

        currentColor = DEFAULT_COLOR;
        strokeWidth = BRUSH_SIZE;
    }

    // gets the paintviews current Bitmap
    public Bitmap getBitmap() {
        return mBitmap;
    }

    // sets the paint brush back to the default color -- transparent
    public void resetBrush(){
        currentColor = DEFAULT_COLOR;
    }

    public void normal() {
        emboss = false;
        blur = false;
    }

    public void emboss() {
        emboss = true;
        if (blur == true)
            blur = true;
        else
            blur = false;
    }

    public void blur() {
        blur = true;
        if (emboss == true)
            emboss = true;
        else
            emboss = false;
    }

    public void clear() {
        paths.clear();
        normal();
        invalidate();
    }

    // sets the paint brush color
    public void setBrushColor(int r, int g, int b)
    {
        currentColor = Color.argb(255,r,g,b);
    }

    // sets the paint brush thickness
    public void setBrushThickness(int size)
    {
        strokeWidth = size;
    }

    public void setmBitmap(Bitmap bm) {
        mBitmap = bm;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.drawColor(Color.TRANSPARENT);

        for (FingerPath fp : paths) {
            mPaint.setColor(fp.color);
            mPaint.setStrokeWidth(fp.strokeWidth);
            mPaint.setMaskFilter(null);

            if (fp.emboss)
                mPaint.setMaskFilter(mEmboss);
            else if (fp.blur)
                mPaint.setMaskFilter(mBlur);

            mCanvas.drawPath(fp.path, mPaint);

        }
        //mCanvas.drawPath(mPath, mPaint);
        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        canvas.restore();

    }

    private void touchStart(float x, float y) {
        mPath = new Path();
        bitmaps.add(mBitmap);
        FingerPath fp = new FingerPath(currentColor, emboss, blur, strokeWidth, mPath);
        paths.add(fp);

        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void touchMove(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);

        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }

    private void touchUp() {
        mPath.lineTo(mX, mY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN :
                touchStart(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE :
                touchMove(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP :
                touchUp();
                invalidate();
                break;
        }
        return true;
    }
}