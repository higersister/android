package cn.cmy.view.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import cn.cmy.view.R;

import static cn.cmy.view.view.StickView.TAG;

public class ClockDiscView extends View {

    private Paint mPaint;
    private Context mContext;

    public ClockDiscView(Context context) {
        this(context, null);
    }

    public ClockDiscView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(getResources().getColor(R.color.colorAccent));
        mPaint.setStrokeWidth(4f);
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

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int min = Math.min(getMeasuredHeight(), getMeasuredWidth());
        canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, min / 2, mPaint);

        for (int i = 1; i <= 12; i++) {
            canvas.rotate(30, getMeasuredWidth() / 2, getMeasuredHeight() / 2);
            if (i % 3 == 0) {
                mPaint.setTextSize(px2sp(mContext, 60));
            } else {
                mPaint.setTextSize(px2sp(mContext, 50));
            }
            canvas.drawLine(getMeasuredWidth() / 2, getMeasuredHeight() / 2 - min / 2,
                    getMeasuredWidth() / 2, getMeasuredHeight() / 2 - min / 2 + 60, mPaint);

            String txt = i + "";
            float txtMeasure = mPaint.measureText(txt);
            canvas.drawText(txt, getMeasuredWidth() / 2 - txtMeasure / 2,
                    getMeasuredHeight() / 2 - min / 2 + 90, mPaint);
        }


    }

    public static int px2dp(Context context, int pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int px2sp(Context context, int pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

}
