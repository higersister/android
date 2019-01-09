package cn.cmy.custom.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class LuckyView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    public static final String[] lucky = new String[]{"特等奖", "一等奖", "二等奖", "三等奖", "幸运奖", "谢谢参与"};
    private static final Integer[] colors = new Integer[]{Color.RED, Color.WHITE, Color.BLUE, Color.GREEN, Color.GRAY, Color.CYAN};
    private Thread t;
    private Paint mPaintTxt;
    private Paint mPaintArc;
    private boolean mStart;
    private int mSpeed;


    public LuckyView(Context context) {
        this(context, null);
    }

    public LuckyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaintArc = new Paint();
        mPaintTxt = new Paint();
        mPaintTxt.setAntiAlias(true);
        mPaintTxt.setTextSize(25f);
        mPaintTxt.setColor(Color.BLACK);
        mPaintArc.setAntiAlias(true);
        this.getHolder().addCallback(this);
        setBackgroundColor(Color.TRANSPARENT);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mStart = true;
        t = new Thread(this::run);
        t.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mStart = false;
    }

    @Override
    public void run() {

        while (mStart) {
            drawLucky();
        }
    }

    private void drawLucky() {
        Canvas canvas = getHolder().lockCanvas();
        RectF rectF = new RectF(0, 0, getMeasuredWidth(), getMeasuredWidth());
        canvas.translate(0, getMeasuredWidth() / 4);
        for (int i = 0; i < colors.length; i++) {
            mPaintArc.setColor(colors[i]);
            canvas.drawArc(rectF, i * 60 + mSpeed, 60, true, mPaintArc);
            Path path = new Path();
            path.addArc(rectF, i * 60 + mSpeed, 60);
            int rl = (int) (60 * Math.PI * getMeasuredWidth() / 360);
            canvas.drawTextOnPath(lucky[i], path, rl / 2 - mPaintTxt.measureText(lucky[i]) / 2, 50, mPaintTxt);
        }
        canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredWidth() / 2, 100, mPaintArc);
        mSpeed += 10;
        getHolder().unlockCanvasAndPost(canvas);
    }


}
