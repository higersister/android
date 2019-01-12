package cn.cmy.view.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;
import cn.cmy.view.R;


public class BezierView extends View {

    private Paint mPaint;
    private Paint mCirclePaint;
    private Paint mTxtPaint;
    private double mPercent;
    private float mMaxValue = 100;
    private float mProgress;
    private float width = 50;
    private float height = 25;
    private float mDistance;
    private WaveAnimation mAnim;
    private Bitmap mBitmap;
    private Canvas mBitmapCanvas;
    private OnTextChangeListener mChangeListener;

    public BezierView(Context context) {
        this(context, null);
    }

    public BezierView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.BezierView);
        mAnim = new WaveAnimation();
        mPaint = new Paint();
        mTxtPaint = new Paint();
        mTxtPaint.setColor(Color.WHITE);
        mTxtPaint.setTextSize(50f);
        mTxtPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mTxtPaint.setAntiAlias(true);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        mCirclePaint = new Paint();
        mCirclePaint.setColor(array.getColor(R.styleable.BezierView_circleBackground, Color.parseColor("#FF878787")));
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(array.getColor(R.styleable.BezierView_progressBackground, getResources().getColor(android.R.color.holo_blue_dark)));
        mPaint.setStrokeWidth(4f);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mAnim.setRepeatCount(Animation.INFINITE);
        mAnim.setInterpolator(new LinearInterpolator());
        array.recycle();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width = widthMode == MeasureSpec.EXACTLY ? widthSize : 500;
        int height = heightMode == MeasureSpec.EXACTLY ? heightSize : 500;
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mBitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        mBitmapCanvas = new Canvas(mBitmap);
        float dy = getMeasuredHeight() / 2 - getMeasuredWidth() / 2;
        mBitmapCanvas.translate(0, dy);
        float min = Math.min(getMeasuredWidth(), getMeasuredHeight());
        float cx = min / 2;
        mBitmapCanvas.drawCircle(cx,
                cx,
                cx,
                mCirclePaint);
        Path path = new Path();
        path.moveTo(min, (float) ((min) * (1 - mPercent) - height));
        path.lineTo(min, min);
        path.lineTo(0, min);
        path.lineTo(-mDistance, (float) ((min) * (1 - mPercent) + height));
        for (int i = 0; i < getMeasuredWidth() / width; i++) {
            path.rQuadTo(width / 2, height, width, 0);
            path.rQuadTo(width / 2, -height, width, 0);
        }
        mBitmapCanvas.drawPath(path, mPaint);
        if (null != mChangeListener) {
            String txt = mChangeListener.onTextChange(mPercent, mMaxValue);
            float txtMeasure = mTxtPaint.measureText(txt);
            mBitmapCanvas.drawText(txt,
                    cx - txtMeasure / 2, cx, mTxtPaint);
        }
        canvas.drawBitmap(mBitmap, 0, 0, null);
    }

    private class WaveAnimation extends Animation {

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            mPercent = interpolatedTime * mProgress / mMaxValue;
            mDistance += 5;
            if (mDistance > getMeasuredWidth() - width / 2) {
                mDistance = 0;
            }
            postInvalidate();
        }
    }

    public void setProgress(float value) {
        mProgress = value;
        startAnimation(mAnim);
    }

    public void setTextChangeListener(OnTextChangeListener changeListener) {
        this.mChangeListener = changeListener;
    }

    public interface OnTextChangeListener {

        String onTextChange(double percent, float maxValue);

    }

}
