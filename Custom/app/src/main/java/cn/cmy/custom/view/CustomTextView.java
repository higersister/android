package cn.cmy.custom.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.Log;

public class CustomTextView extends AppCompatTextView {

    private static final String TAG = "$$$$$$$$$$$";
    private LinearGradient mLg;
    private Matrix matrix;
    private Paint mPaint;
    private int mTranslate;
    private int mWidth;

    public CustomTextView(Context context) {
        super(context);

    }

    public CustomTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.i(TAG, "onMeasure: widthmode:" + MeasureSpec.getMode(widthMeasureSpec));
        Log.i(TAG, "onMeasure: heightmode:" + MeasureSpec.getMode(heightMeasureSpec));
        Log.i(TAG, "onMeasure: width:" + MeasureSpec.getSize(widthMeasureSpec));
        Log.i(TAG, "onMeasure: height:" + MeasureSpec.getSize(heightMeasureSpec));

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.i(TAG, "onSizeChanged: w:" + w);
        Log.i(TAG, "onSizeChanged: h:" + h);
        Log.i(TAG, "onSizeChanged: measurewidth:" + getMeasuredWidth());
        Log.i(TAG, "onSizeChanged: measureHeight:" + getMeasuredHeight());
        mPaint = getPaint();
        mWidth = getMeasuredWidth();
        mLg = new LinearGradient(0, 0,
                mWidth, 0,
                new int[]{Color.BLUE, 0xffffffff, Color.BLUE},
                null, Shader.TileMode.CLAMP);
        mPaint.setShader(mLg);
        matrix = new Matrix();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
        //canvas.restore();
        /*if (null != matrix) {
            mTranslate += mWidth / 5;
            if (mTranslate > mWidth) {
                mTranslate = -mWidth;
            }
            matrix.setTranslate(mTranslate, 0);
            mLg.setLocalMatrix(matrix);
            postInvalidateDelayed(80);

        }*/

    }
}
