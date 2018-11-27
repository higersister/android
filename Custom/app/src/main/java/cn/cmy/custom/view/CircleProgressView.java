package cn.cmy.custom.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import cn.cmy.custom.R;

public class CircleProgressView extends View {
    private static final String TAG = "$$$$$$$$$$$$$$$$";
    private Paint mCirclePaint;
    private Paint mCirclePaint1;
    private Paint mTextPaint;
    private int mWidth;
    private int mHeight;
    private RectF mRectF;
    private int value;

    public CircleProgressView(Context context) {
        super(context);

    }

    public CircleProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressView);
        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeWidth(10f);
        mCirclePaint.setColor(typedArray.getColor(R.styleable.CircleProgressView_circleBackgroundColor, Color.parseColor("#FF40A9FF")));
        mCirclePaint1 = new Paint();
        mCirclePaint1.setAntiAlias(true);
        mCirclePaint1.setStyle(Paint.Style.STROKE);
        mCirclePaint1.setStrokeWidth(10f);
        mCirclePaint1.setColor(typedArray.getColor(R.styleable.CircleProgressView_circleForegroundColor, Color.parseColor("#FF50F2D7")));
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(typedArray.getColor(R.styleable.CircleProgressView_textColor, Color.BLACK));
        mTextPaint.setTextSize(typedArray.getFloat(R.styleable.CircleProgressView_textSize, 26f));
        value = typedArray.getInt(R.styleable.CircleProgressView_value, 0);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidth = MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.AT_MOST ? 200 : MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST ? 200 : MeasureSpec.getSize(heightMeasureSpec);
        mRectF = new RectF(10, 10, mWidth - 10, mHeight - 10);
        setMeasuredDimension(mWidth, mHeight);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //canvas.drawCircle(mWidth / 2, mHeight / 2, mWidth / 2 - 5, mCirclePaint);
        canvas.drawArc(mRectF, 120, 300, false, mCirclePaint);
        int sweepAngle = value * 300 / 100;
        canvas.drawArc(mRectF, 120, sweepAngle, false, mCirclePaint1);
        String txt = String.valueOf(value);
        float txtWidth = mTextPaint.measureText(txt);
        canvas.drawText(txt, (mWidth) / 2.0f - txtWidth / 2.0f, mHeight / 2, mTextPaint);
    }

    public void setTextColor(int color) {
        if (0 >= color) {
            return;
        }
        mTextPaint.setColor(color);
        invalidate();
    }

    public void setTextSize(float size) {
        if (0f >= size) {
            return;
        }
        mTextPaint.setTextSize(size);
        invalidate();
    }

    public void setCircleBackgroundColor(int color) {
        if (0 >= color) {
            return;
        }
        mCirclePaint.setColor(color);
        invalidate();
    }

    public void setCircleForegroundColor(int color) {
        if (0 >= color) {
            return;
        }
        mCirclePaint1.setColor(color);
        invalidate();
    }

    public void setValue(int value) {
        if (0 >= value) {
            return;
        }
        if (100 < value) {
            value = 100;
        }
        this.value = value;
        invalidate();
    }

    public int getValue() {
        return this.value;
    }
}
