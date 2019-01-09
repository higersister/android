package cn.cmy.view.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Scroller;

public class SlidingMenu extends ViewGroup {

    private Scroller mScroller;
    private LinearLayout mMenuLayout;
    private FrameLayout mMainLayout;
    private int mWidth;
    private int mHeight;
    private float mLastX;
    private PointF mPointF = new PointF();

    public SlidingMenu(Context context) {
        this(context, null);
    }

    public SlidingMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context);
        setBackgroundColor(Color.parseColor("#FF53FAE9"));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mMenuLayout.layout(-r, t, l, b);
        mMainLayout.layout(l, t, r, b);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (!(getChildAt(0) instanceof LinearLayout)) {
            throw new IllegalStateException("child at 0 must be LinearLayout");
        }
        if (!(getChildAt(1) instanceof FrameLayout)) {
            throw new IllegalStateException("chile at 1 must be FrameLayout");
        }
        mMenuLayout = (LinearLayout) getChildAt(0);
        mMainLayout = (FrameLayout) getChildAt(1);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        mWidth = widthMode == MeasureSpec.EXACTLY ? widthSize : getResources().getDisplayMetrics().widthPixels;
        mHeight = heightMode == MeasureSpec.EXACTLY ? heightSize : getResources().getDisplayMetrics().heightPixels;
        setMeasuredDimension(mWidth, mHeight);


    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            //down
            case 0: {
                mLastX = event.getX();
                mPointF.set(event.getX(), event.getY());
                break;
            }
            //move
            case 2: {
                float dx = event.getX();
                if (getScrollX() + mLastX - dx < -mWidth * 0.5
                        || getScrollX() + mLastX - dx > 0) {
                    mLastX = dx;
                    break;
                }
                scrollBy((int) (mLastX - dx), 0);
                mLastX = dx;
                break;
            }
            //up
            case 1: {
                if (-getScrollX() > mWidth * 0.3) {
                    if (getScrollX() < 0) {
                        mScroller.startScroll(getScrollX(), 0, (int) (-mWidth * 0.5 - getScrollX()), 0);
                    } else {
                        mScroller.startScroll(getScrollX(), 0, -getScrollX(), 0);
                    }
                } else {
                    if (getScrollX() > mWidth * 0.3) {
                        mScroller.startScroll(getScrollX(), 0, (int) (mWidth * 0.5 + getScrollX()), 0);
                    } else {
                        mScroller.startScroll(getScrollX(), 0, -getScrollX(), 0);
                    }
                }
                postInvalidate();
                break;
            }

        }
        return true;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        double rate = l * 1.0d / (double) mWidth;
        double mainScale = 1 - 0.5d * (1 - rate) + 0.5;
        mMainLayout.setScaleX((float) mainScale);
        mMainLayout.setScaleY((float) mainScale);

    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        } else {
            super.computeScroll();
        }
    }
}
