package cn.cmy.view.view;

import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.BounceInterpolator;

public class StickView extends View {

    public static final String TAG = "$$$$$$$$$$$$$$$";
    private PointF mPoint;
    private Paint mPaint;
    private Paint mTxtPaint;
    private Paint mLinePaint;

    public StickView(Context context) {
        this(context, null);
    }

    public StickView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPoint = new PointF();
        mPaint = new Paint();
        mTxtPaint = new Paint();
        mTxtPaint.setAntiAlias(true);
        mTxtPaint.setStyle(Paint.Style.FILL);
        mTxtPaint.setTextSize(25f);
        mTxtPaint.setColor(Color.WHITE);
        mLinePaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(getResources().getColor(android.R.color.holo_red_light));
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStyle(Paint.Style.FILL);
        mLinePaint.setColor(getResources().getColor(android.R.color.holo_red_light));

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width = widthMode == MeasureSpec.EXACTLY ? widthSize : 100;
        int height = heightMode == MeasureSpec.EXACTLY ? heightSize : 100;
        setMeasuredDimension(width, height);
        mPoint.set(getMeasuredWidth() / 2, getMeasuredHeight() / 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Path path = new Path();
        int radius = 36;
        String txt = "+99";
        float txtMeasure = mTxtPaint.measureText(txt);
        canvas.drawCircle(mPoint.x, mPoint.y, radius, mPaint);
        canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, radius / 2, mPaint);
        float distance = (float) Math.sqrt((mPoint.x - getMeasuredWidth() / 2) * (mPoint.x - getMeasuredWidth() / 2) + (mPoint.y - getMeasuredHeight() / 2) * (mPoint.y - getMeasuredHeight() / 2));
        float sin = (mPoint.y - getMeasuredHeight() / 2) / distance;
        float cos = (mPoint.x - getMeasuredWidth() / 2) / distance;
        PointF pA = new PointF(getMeasuredWidth() / 2 - radius / 2 * sin, getMeasuredHeight() / 2 + radius / 2 * cos);
        PointF pB = new PointF(mPoint.x - radius * sin, mPoint.y + radius * cos);
        PointF pC = new PointF(mPoint.x + radius * sin, mPoint.y - radius * cos);
        PointF pD = new PointF(getMeasuredWidth() / 2 + radius / 2 * sin, getMeasuredHeight() / 2 - radius / 2 * cos);
        path.moveTo(pA.x, pA.y);
        //  Log.i(TAG, "onDraw: pA(" + pA.x + "," + pA.y + ")");
        // Log.i(TAG, "onDraw: pD(" + pD.x + "," + pD.y + ")");

        path.quadTo(
                (getMeasuredWidth() / 2 + mPoint.x) / 2,
                (getMeasuredHeight() / 2 + mPoint.y) / 2,
                pB.x, pB.y);
        path.lineTo(pC.x, pC.y);
        path.quadTo(
                (getMeasuredWidth() / 2 + mPoint.x) / 2,
                (getMeasuredHeight() / 2 + mPoint.y) / 2,
                pD.x, pD.y);
        canvas.drawPath(path, mPaint);
        canvas.drawText(txt, mPoint.x - txtMeasure / 2,
                mPoint.y + txtMeasure / 4, mTxtPaint);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case 2: {
                mPoint.set(event.getX(), event.getY());
                postInvalidate();
                break;
            }
            case 1: {
                // mPoint.set(getMeasuredWidth() / 2, getMeasuredHeight() / 2);
                //startAnimation(mAnim);
                ValueAnimator anim = ObjectAnimator.ofObject(new ReturnEvaluator(), new PointF(getMeasuredWidth() / 2, getMeasuredHeight() / 2),
                        mPoint);
                anim.setDuration(200);
                anim.setInterpolator(new BounceInterpolator());
                anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        // Log.i(TAG, "onAnimationUpdate: " + animation.getAnimatedFraction());
                        mPoint = (PointF) animation.getAnimatedValue();
                        postInvalidate();
                    }
                });
                anim.start();
                break;
            }
            default: {
                break;
            }
        }
        return true;
    }

    private class ReturnEvaluator implements TypeEvaluator<PointF> {
        @Override
        public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
            return new PointF(endValue.x + fraction * (startValue.x - endValue.x), endValue.y + fraction * (startValue.y - endValue.y));
        }
    }

}
