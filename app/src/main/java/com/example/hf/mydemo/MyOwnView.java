package com.example.hf.mydemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import static android.content.ContentValues.TAG;

/**
 * Created by HF on 2017/5/13.
 */

public class MyOwnView extends View {
    private static final  int COLOR_GRAY = 1;
    private static final  int COLOR_BLUE = 0;
    private final Context mContext;
    private Paint mPaint;
    private RectF oval;
    private int angle_o;
    private int color_style=COLOR_BLUE;

    public MyOwnView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
        this.mContext = context;
        for (int i = 0; i < attrs.getAttributeCount(); i++) {
            Log.d(TAG, "onMyOwnView: " + attrs.getAttributeName(i));
            Log.d(TAG, "onMyOwnView: " + attrs.getAttributeValue(i));
        }
        float density = context.getResources().getDisplayMetrics().density;
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        float density1 = metrics.density;
        int widthPixels = metrics.widthPixels;
        int heightPixels = metrics.heightPixels;

        Log.d(TAG, "density=" + density + " density1=" + density1);
        Log.d(TAG, "widthPixels=" + widthPixels + " heightPixels=" + heightPixels);

        Log.d(TAG, "widthdip=" + px2dip(widthPixels) + " heightdip=" + px2dip(heightPixels));
        Log.d(TAG, "onMyOwnView: " + attrs.getAttributeIntValue("http://schemas.android.com/apk/res/android", "layout_width", -1));
        Log.d(TAG, "onMyOwnView: " + attrs.getAttributeIntValue("http://schemas.android.com/apk/res/android", "layout_height", -1));
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    angle_o += 1;
                    postInvalidate();
                    SystemClock.sleep(20);
                    if (angle_o == 360) {
                        changeColorStyle();
                        angle_o=0;
                    }

                }
            }
        }).start();


    }

    private void changeColorStyle() {
        color_style=color_style==COLOR_GRAY?COLOR_BLUE:COLOR_GRAY;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        Log.d(TAG, "onDraw: ");
        super.onDraw(canvas);
        if(color_style==COLOR_GRAY){
            mPaint.setColor(Color.BLUE);
        }else if (color_style==COLOR_BLUE){
            mPaint.setColor(Color.GRAY);
        }
        // FILL填充, STROKE描边,FILL_AND_STROKE填充和描边
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        int with = getWidth();
        int height = getHeight();
        Log.e(TAG, "onDraw---->" + with + "*" + height);
        float radius = with / 4;
        canvas.drawCircle(with / 2, with / 2, radius, mPaint);
        mPaint.setColor(Color.BLUE);
        oval.set(with / 2 - radius, with / 2 - radius, with / 2
                + radius, with / 2 + radius);//用于定义的圆弧的形状和大小的界限
        if(color_style==COLOR_GRAY){
            mPaint.setColor(Color.GRAY);
        }else if (color_style==COLOR_BLUE){
            mPaint.setColor(Color.BLUE);
        }
        canvas.drawArc(oval, 270, angle_o, true, mPaint);  //根据进度画圆弧


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d(TAG, "onMeasure: widthMeasureSpec=" + widthMeasureSpec + " heightMeasureSpec=" + heightMeasureSpec);
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize_w = MeasureSpec.getSize(widthMeasureSpec);
        int specSize_h = MeasureSpec.getSize(heightMeasureSpec);
        Log.d(TAG, "onMeasure: specMode=" + specMode + " specSize_w=" + specSize_w + " specSize_h=" + specSize_h);

        switch (specMode) {
            case MeasureSpec.AT_MOST:
                Log.d(TAG, "onMeasure: AT_MOST");
                break;
            case MeasureSpec.EXACTLY:
                Log.d(TAG, "onMeasure: EXACTLY");
                break;
            case MeasureSpec.UNSPECIFIED:
                Log.d(TAG, "onMeasure: UNSPECIFIED");
                break;
        }
        setMeasuredDimension(specSize_w, specSize_h);

//        Log.d(TAG, "onMeasure: width"+getWidth()+" getHeight="+getHeight());
//        Log.d(TAG, "onMeasure: getMeasuredHeight"+getMeasuredWidth()+" getMeasuredHeight="+getMeasuredHeight());
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        Log.d(TAG, "onMeasure2: width"+getWidth()+" getHeight="+getHeight());
//        Log.d(TAG, "onMeasure2: getMeasuredHeight"+getMeasuredWidth()+" getMeasuredHeight="+getMeasuredHeight());
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        Log.d(TAG, "onLayout: width" + getWidth() + " getHeight=" + getHeight());
        Log.d(TAG, "onLayout: getMeasuredHeight" + getMeasuredWidth() + " getMeasuredHeight=" + getMeasuredHeight());
        super.onLayout(changed, left, top, right, bottom);
    }

    private Bitmap getBitMap() {

        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), getResId());
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int viewWidth = getMeasuredWidth();
        int viewHeight = getMeasuredHeight();
        float scale_w = ((float) viewWidth) / width;
        float scale_h = ((float) viewHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scale_w, scale_h);
        Bitmap fullBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);

        return fullBitmap;
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        oval = new RectF();
    }

    private int getResId() {
        return mContext.getResources().getIdentifier("bg", "mipmap", mContext.getPackageName());
    }

    public int px2dip(int px) {
        float density = getResources().getDisplayMetrics().density;
        return (int) (px / density + 0.5);
    }

    public int dip2px(int dip) {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        float density = metrics.density;
        return (int) (dip * density + 0.5);

    }
}
