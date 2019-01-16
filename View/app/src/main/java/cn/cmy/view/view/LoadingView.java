package cn.cmy.view.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.TextView;

import cn.cmy.view.R;

import static cn.cmy.view.view.StickView.TAG;

public class LoadingView extends View {

    private static final int RADIUS = 50;
    private Paint mPaint;

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LoadingView);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(15f);
        mPaint.setColor(a.getColor(R.styleable.LoadingView_loadingBackground, Color.parseColor("#FFD81B60")));
        if (a.getBoolean(R.styleable.LoadingView_isRound, true)) {
            mPaint.setStrokeCap(Paint.Cap.ROUND);
        }
        a.recycle();
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

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int num = 360 / 15;
        for (int i = 0; i < num; i++) {
            float degree = num * i;
            Log.i(TAG, "onDraw: radius:" + RADIUS);
            int dx = (int) (RADIUS * Math.cos(degree * Math.PI / 180));
            int dy = (int) (RADIUS * Math.sin(degree * Math.PI / 180));
            canvas.drawLine(getMeasuredWidth() / 2, getMeasuredHeight() / 2,
                    getMeasuredWidth() / 2 + dx, getMeasuredHeight() / 2 + dy, mPaint);
        }

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator rAnim = ObjectAnimator.ofFloat(this,
                "rotation", 0, 360);
        rAnim.setRepeatCount(-1);
        ObjectAnimator sxAnim = ObjectAnimator.ofFloat(this,
                "scaleX", 1, 0.3f, 1);
        sxAnim.setRepeatCount(-1);
        ObjectAnimator syAnim = ObjectAnimator.ofFloat(this,
                "scaleY", 1, 0.3f, 1);
        syAnim.setRepeatCount(-1);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(this,
                "alpha", 1, 0.3f, 1);
        alpha.setRepeatCount(-1);
        set.setDuration(1500);
        set.play(rAnim).with(sxAnim).with(syAnim).with(alpha);
        set.start();

    }
}
