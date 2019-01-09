package cn.cmy.view.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FlowLayout extends ViewGroup {


    public FlowLayout(Context context) {
        super(context);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int totalHeight = 0;
        int height = 0;
        int useWidth = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (useWidth + view.getMeasuredWidth() >= getMeasuredWidth()) {
                useWidth = 0;
                totalHeight += height;
            }
            view.layout(useWidth, totalHeight, useWidth + view.getMeasuredWidth(), totalHeight + view.getMeasuredHeight());
            useWidth += view.getMeasuredWidth();
            height = view.getMeasuredHeight();
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int totalWidth = getResources().getDisplayMetrics().widthPixels;
        int totalHeight = getResources().getDisplayMetrics().heightPixels;
        int useWidth = 0;
        int useHeight = 0;
        int childWidth = 0;
        int childHeight = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            measureChild(view, widthMeasureSpec, heightMeasureSpec);
            useWidth += view.getMeasuredWidth();
            childWidth = Math.max(childWidth, useWidth);
            childHeight = view.getMeasuredHeight();
            if (useWidth >= totalWidth) {
                useWidth = view.getMeasuredWidth();
                useHeight += childHeight;
                childWidth = totalWidth;
            }
        }
        int width = widthMode == MeasureSpec.EXACTLY ? widthSize : Math.min(childWidth, totalWidth);
        int height = heightMode == MeasureSpec.EXACTLY ? heightSize : Math.min(totalHeight, childHeight + useHeight);
        setMeasuredDimension(width, height);


    }
}
