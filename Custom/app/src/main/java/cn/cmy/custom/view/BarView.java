package cn.cmy.custom.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.Random;

public class BarView extends View {

    private static final String TAG = "$$$$$$$$$$$$$$$$";
    private int mWidth;
    private int mHeight;
    private Paint mPaint;
    private LinearGradient linearGradient;
    private Matrix matrix;
    private int translate;

    public BarView(Context context) {
        super(context);
    }

    public BarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        matrix = new Matrix();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(20f);
        mPaint.setColor(Color.BLUE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        mWidth = widthMode == MeasureSpec.AT_MOST ? 400 : MeasureSpec.getSize(widthMeasureSpec);
        mHeight = heightMode == MeasureSpec.AT_MOST ? 500 : MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);
        linearGradient = new LinearGradient(0, 0, mWidth / 35, mHeight, new int[]{Color.YELLOW, Color.GREEN}, null, Shader.TileMode.CLAMP);
        mPaint.setShader(linearGradient);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int part = mWidth / 35;
        int left = 10;
        int right = part + 10;
        for (int i = 0; i < 28; i++) {
            int top = new Random().nextInt(mHeight);
            Rect rect = new Rect(left, top, right, mHeight);

            // canvas.save();
            canvas.drawRect(rect, mPaint);
            left = right + 5;
            right += part + 5;

        }
      /*  translate += mHeight / 7;
        if (translate > mHeight) {
            translate = -mHeight;
        }
        //canvas.save();
        matrix.setTranslate(0, translate);
        linearGradient.setLocalMatrix(matrix);
        Log.i(TAG, "onDraw: translate:" + translate);
        */// canvas.restore();
        postInvalidateDelayed(100);

    }
}
