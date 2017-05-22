package com.example.hf.mydemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HF on 2017/5/12.
 */

public class MyView extends View {
    public Context mContext;

    public Thread t;

    //public int[] imageIds ;
    private List<Integer> imageIds = new ArrayList<Integer>();

    //test
    //test

    public int index = 0;

    private long time = 1;

    // 显示保持时长
    private int showLimit = 50;

    // 隐藏过程时长
    private int hideLimit = 10;

    // 第二张出现过程时长
    private int secondLimit = 10;

    // 当前所出状态，true表示保持显示，false表示切换图片
    private boolean statusFlag = true;

    private int alpha = 255;

    public static int bgCount = 1;

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        if (bgCount == 1) {
            int v = context.getResources().getIdentifier("lebian_main_background_normal", "drawable", context.getPackageName());
            imageIds.add(v);
            postInvalidate();
        } else if (bgCount > 1) {
            for (int i = 0; i < bgCount; i++) {
                int v = context.getResources().getIdentifier("lebian_main_background_normal_" + (i + 1), "drawable", context.getPackageName());
                imageIds.add(v);
            }
            t = new Thread() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        time++;
                        if (statusFlag) {
                            alpha = 255;
                            if (time >= showLimit) {
                                statusFlag = false;
                                time = 0;
                            }
                        } else {
                            alpha = 255 - (int) (255 * time / hideLimit);
                            if (time >= hideLimit) {
                                statusFlag = true;
                                time = 0;
                                index++;
                                if (index >= imageIds.size()) {
                                    index = 0;
                                }
                                alpha = 255;
                            }
                        }
                        postInvalidate();
                    }
                }
            };
            t.start();
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint p = new Paint();
        if (bgCount == 1) {
            canvas.drawBitmap(picture(0), 0, 0, p);

        } else if (bgCount > 1) {
            p.setAlpha(alpha);

            canvas.drawBitmap(picture(index), 0, 0, p);
            if (!statusFlag) {
                int indexTemp = index + 1;
                if (indexTemp == imageIds.size()) {
                    indexTemp = 0;
                }
                p.setAlpha(255 - alpha);
                canvas.drawBitmap(picture(indexTemp), 0, 0, p);
            }
        }
    }

    public Bitmap picture(int index) {
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), imageIds.get(index));
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        int screenwidth = wm.getDefaultDisplay().getWidth();
        int screenHeight = wm.getDefaultDisplay().getHeight();
        float scaleWidth = ((float) screenwidth) / width;
        float scaleHeight = ((float) screenHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        return bitmap;
    }


}
