package cn.cmy.view.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class TPath extends View {

    private Paint paint;

    public TPath(Context context) {
        this(context, null);
    }

    public TPath(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setColor(Color.parseColor("#FFC810F1"));
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(10f);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        Path path = new Path();
        int width = getMeasuredWidth() / 2;
        int height = getMeasuredHeight() / 2;
        path.moveTo(width - 20, height + 100);
        path.lineTo(width + 20, height + 100);
        path.lineTo(width, height - 50);
        path.lineTo(width - 20, height + 100);
        /*
        path.lineTo(width + 50, height - 50);
        path.lineTo(width - 100, height + 100);*/
        path.close();
        canvas.drawPath(path, paint);
    }
}
