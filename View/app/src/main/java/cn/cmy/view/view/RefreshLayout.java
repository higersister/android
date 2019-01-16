package cn.cmy.view.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Scroller;
import android.widget.TextView;

import cn.cmy.view.R;

public class RefreshLayout extends ViewGroup {

    private static final int UP = 1;
    private static final int DOWN = 0;
    private Scroller mScroller;
    private ViewGroup mVp;
    private ImageView mImgIcon;
    private TextView mTvDesc;
    private ProgressBar mProgress;
    private int mWidth;
    private int mHeight;
    private float mLastY;
    private float mDownY;
    private int mOrientation;
    private PointF mPointF;

    public RefreshLayout(Context context) {
        this(context, null);
    }

    public RefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context);
        mPointF = new PointF();

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mVp = (ViewGroup) getChildAt(0);
        mImgIcon = (ImageView) mVp.getChildAt(0);
        mTvDesc = (TextView) mVp.getChildAt(2);
        mProgress = (ProgressBar) mVp.getChildAt(3);
        mProgress.setVisibility(GONE);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mVp.layout(l, -b, r, 0);
        getChildAt(1).layout(l, t, r, b);
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
                mDownY = mLastY = event.getY();
                mPointF.set(0, event.getY());
                break;
            }
            //move
            case 2: {
                float dy = event.getY();
                if (getScrollY() + mLastY - dy < -getMeasuredHeight() ||
                        getScrollY() + mLastY - dy > 0) {
                    mLastY = dy;
                    break;
                }
                scrollBy(0, (int) (Math.abs(dy - mDownY) / getMeasuredHeight() * (mLastY - dy)));
                mLastY = dy;
                if (mPointF.y - dy < 0) {
                    mOrientation = DOWN;
                } else {
                    mOrientation = UP;
                }
                postInvalidate();
                break;
            }
            //up
            case 1: {
                if (-getScrollY() > mVp.getMeasuredHeight() * 0.2) {
                    if (getScrollY() < 0) {
                        mScroller.startScroll(0, getScrollY(), 0, (int) (-mVp.getMeasuredHeight() * 0.3 - getScrollY()));
                    } else {
                        mScroller.startScroll(0, getScrollY(), 0, -getScrollY());
                    }
                } else {
                    mScroller.startScroll(0, getScrollY(), 0, -getScrollY());
                }

                postInvalidate();
                break;
            }

        }

        return true;
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

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (-t == mVp.getMeasuredHeight() * 0.3) {
            mTvDesc.setText("loading...");
            mImgIcon.setVisibility(GONE);
            mProgress.setVisibility(VISIBLE);

        } else {
            mTvDesc.setText("refresh...");
            mProgress.setVisibility(GONE);
            mImgIcon.setVisibility(VISIBLE);
            if (mOrientation == UP) {
                mImgIcon.setImageResource(R.drawable.ic_arrow_upward_white_24dp);
            } else {
                mImgIcon.setImageResource(R.drawable.ic_arrow_downward_black_24dp);
            }
        }
    }
}
