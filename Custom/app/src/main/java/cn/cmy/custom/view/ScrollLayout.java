package cn.cmy.custom.view;

import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

public class ScrollLayout extends ViewGroup {
    private static final String TAG = "$$$$$$$$$$$$$$$$";

    private Scroller mScroller;
    private int height;
    private Point point;
    private int mStart;
    private int mCur;

    public ScrollLayout(Context context) {
        super(context);
    }

    public ScrollLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        point = new Point();
        mScroller = new Scroller(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View viewChild = getChildAt(i);
            measureChild(viewChild, widthMeasureSpec, heightMeasureSpec);
        }

    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        int count = getChildCount();
        MarginLayoutParams lp = (MarginLayoutParams) getLayoutParams();
        height = getMeasuredHeight();
        lp.height = height * getChildCount();
        setLayoutParams(lp);
        for (int j = 0; j < count; j++) {
            View viewChild = getChildAt(j);
            if (View.GONE != viewChild.getVisibility()) {
                viewChild.layout(i, j * height, i2, (j + 1) * height);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int y = (int) event.getY();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: {
                point.y = y;
                mStart = getScrollY();
                break;
            }
            case MotionEvent.ACTION_UP: {
                mCur = getScrollY();
                int mEnd = mCur - mStart;
                if (mEnd > 0) {
                    if (mEnd > height / 2) {
                        mScroller.startScroll(0, mCur, 0, height - mEnd);
                    } else {
                        mScroller.startScroll(0, mCur, 0, -mCur);
                    }
                } else {
                    if (-mEnd > height / 2) {
                        mScroller.startScroll(0, mStart, 0, -height);
                    } else {
                        mScroller.startScroll(0, mCur, 0, -mEnd);
                    }
                }


                break;
            }
            case MotionEvent.ACTION_MOVE: {
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                int scrollY = point.y - y;
                int curY = getScrollY();
                if (curY + scrollY < 0 || scrollY + curY > height) {
                    scrollY = 0;
                }
                scrollBy(0, scrollY);
                point.y = y;
                break;
            }
        }
        postInvalidate();
        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            scrollTo(0, mScroller.getCurrY());
            postInvalidate();
        }
    }
}
