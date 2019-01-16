package cn.cmy.view.view;

import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import cn.cmy.view.R;

import static cn.cmy.view.view.ClockDiscView.px2dp;
import static cn.cmy.view.view.StickView.TAG;

public class CheckView extends View {

    private Paint mPaintStroke;
    private Paint mPaintFill;
    private Paint mPaintWhite;
    private Paint mPaintLine;
    private Context mContext;
    private float sweepAngle;
    private float mRadius = -1;
    private PointF[] mPointFs;
    private volatile boolean mChecked;
    private int mCheckedColor;
    private int mUnCheckedColor;

    public CheckView(Context context) {
        this(context, null);
    }

    public CheckView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CheckView);
        mUnCheckedColor = a.getColor(R.styleable.CheckView_unCheckedColor, getResources().getColor(android.R.color.darker_gray));
        mCheckedColor = a.getColor(R.styleable.CheckView_checkedColor, getResources().getColor(R.color.colorAccent));
        a.recycle();
        this.mContext = context;
        mPointFs = new PointF[2];
        mPaintLine = new Paint();
        mPaintStroke = new Paint();
        mPaintFill = new Paint();
        mPaintWhite = new Paint();
        mPaintStroke.setAntiAlias(true);
        //mPaintStroke.setStrokeWidth(8f);
        mPaintStroke.setColor(mUnCheckedColor);
        mPaintStroke.setStyle(Paint.Style.STROKE);
        mPaintFill.setAntiAlias(true);
        mPaintFill.setStyle(Paint.Style.FILL);
        mPaintFill.setColor(mCheckedColor);
        mPaintWhite.setAntiAlias(true);
        mPaintWhite.setStyle(Paint.Style.FILL);
        mPaintWhite.setColor(Color.WHITE);
        mPaintWhite.setAntiAlias(true);
        mPaintLine.setStrokeCap(Paint.Cap.ROUND);
        mPaintLine.setStyle(Paint.Style.STROKE);
        mPaintLine.setStrokeWidth(15f);
        mPaintLine.setColor(mUnCheckedColor);
    }

    public synchronized boolean isChecked() {
        return mChecked;
    }

    public synchronized void setChecked(boolean mChecked) {
        if (mChecked) {
            mPaintStroke.setColor(mCheckedColor);
            mPaintLine.setColor(Color.WHITE);
            startAnim();
        } else {
            mPaintStroke.setColor(mUnCheckedColor);
            mPaintLine.setColor(mUnCheckedColor);
            postInvalidate();
        }
        this.mChecked = mChecked;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(
                widthMode == MeasureSpec.AT_MOST ? px2dp(mContext, 600) : widthSize,
                heightMode == MeasureSpec.AT_MOST ? px2dp(mContext, 600) : heightSize);
        mPointFs[1] = mPointFs[0] = new PointF(0, 0);
        startAnim();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mChecked) {
            Path path = new Path();
            RectF rectF = new RectF(0,
                    getMeasuredHeight() / 2 - getMeasuredWidth() / 2,
                    getMeasuredWidth(),
                    getMeasuredHeight() / 2 + getMeasuredWidth() / 2);
            path.addArc(rectF, 0, sweepAngle);
            canvas.drawPath(path, mPaintStroke);
            if (sweepAngle == 360f) {
                canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, getMeasuredWidth() / 2, mPaintFill);
                canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, mRadius, mPaintWhite);
                if (mRadius == 0) {
                    float radius = getMeasuredWidth() / 2.f;
                    Path path1 = new Path();
                    path1.moveTo(radius - radius / 5, getMeasuredHeight() / 2);
                    path1.lineTo(mPointFs[0].x, mPointFs[0].y);
                    path1.lineTo(mPointFs[1].x, mPointFs[1].y);
                    canvas.drawPath(path1, mPaintLine);
                }
            }
        } else {
            canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, getMeasuredWidth() / 2, mPaintStroke);
            float radius = getMeasuredWidth() / 2.f;
            Path path1 = new Path();
            path1.moveTo(radius - radius / 5, getMeasuredHeight() / 2);
            path1.lineTo(radius, getMeasuredHeight() / 2 + radius / 5);
            path1.lineTo(radius + 2 * radius / 5,
                    getMeasuredHeight() / 2 - 2 * radius / 5);
            canvas.drawPath(path1, mPaintLine);
        }


    }


    public void startAnim() {
        float radius = getMeasuredWidth() / 2.f;
        ValueAnimator animStroke = ObjectAnimator.ofObject(new SweepAngleEvaluator(),
                0f, 360f);
        animStroke.setDuration(600);
        animStroke.setInterpolator(new LinearInterpolator());
        animStroke.addUpdateListener(animation -> {
            sweepAngle = (float) animation.getAnimatedValue();
            postInvalidate();
        });
        animStroke.start();
        ValueAnimator animFill = ObjectAnimator.ofObject(new RadiusEvaluator(),
                getMeasuredWidth() / 2.f, 0f);
        animFill.setDuration(300);
        animFill.setInterpolator(new LinearInterpolator());
        animFill.addUpdateListener(animation -> {
            mRadius = (float) animation.getAnimatedValue();
            postInvalidate();
        });
        animFill.setStartDelay(600);
        animFill.start();
        ValueAnimator animPoint1 = ObjectAnimator.ofObject(new PointEvaluator(),
                new PointF(radius - radius / 5, getMeasuredHeight() / 2),
                new PointF(radius, getMeasuredHeight() / 2 + radius / 5));
        animPoint1.setDuration(300);
        animPoint1.setInterpolator(new LinearInterpolator());
        animPoint1.addUpdateListener(animation -> {
            mPointFs[1] = mPointFs[0] = (PointF) animation.getAnimatedValue();
            postInvalidate();
        });
        animPoint1.setStartDelay(900);
        animPoint1.start();
        ValueAnimator animPoint2 = ObjectAnimator.ofObject(new PointEvaluator(),
                new PointF(radius, getMeasuredHeight() / 2 + radius / 5),
                new PointF(radius + 2 * radius / 5,
                        getMeasuredHeight() / 2 - 2 * radius / 5));
        animPoint2.setDuration(300);
        animPoint2.setInterpolator(new LinearInterpolator());
        animPoint2.addUpdateListener(animation -> {
            mPointFs[1] = (PointF) animation.getAnimatedValue();
            postInvalidate();
        });
        animPoint2.setStartDelay(1200);
        animPoint2.start();
    }


    private class RadiusEvaluator implements TypeEvaluator<Float> {
        @Override
        public Float evaluate(float fraction, Float startValue, Float endValue) {
            return startValue + fraction * (endValue - startValue);
        }
    }

    private class SweepAngleEvaluator implements TypeEvaluator<Float> {

        @Override
        public Float evaluate(float fraction, Float startValue, Float endValue) {
            return startValue + fraction * (endValue - startValue);
        }
    }

    private class PointEvaluator implements TypeEvaluator<PointF> {
        @Override
        public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
            return new PointF(startValue.x + fraction * (endValue.x - startValue.x),
                    startValue.y + fraction * (endValue.y - startValue.y));
        }
    }
}
