package cn.cmy.custom.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;

import cn.cmy.custom.R;

public class RefreshLayout extends ViewGroup {

    private LinearLayout mHeadLayout;
    private FrameLayout mBodyLayout;
    private ImageView mImg;
    private TextView mTv;
    private Scroller mScroller;
    private float mLastPoint;

    public RefreshLayout(Context context) {
        this(context, null);
    }

    public RefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context);
        mHeadLayout = new LinearLayout(context);
        mHeadLayout.setBackgroundColor(Color.GRAY);
        mHeadLayout.setOrientation(LinearLayout.HORIZONTAL);
        mBodyLayout = new FrameLayout(context);
        mImg = new ImageView(context);
        mTv = new TextView(context);
        mImg.setImageResource(R.drawable.ic_arrow_downward_black_24dp);
        LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(32, 32);
        imgParams.leftMargin = 20;
        imgParams.gravity = Gravity.CENTER_VERTICAL;
        mImg.setLayoutParams(imgParams);
        mTv.setText("下拉刷新...");
        mTv.setTextSize(20f);
        mTv.setTextColor(Color.BLACK);
        mTv.setBackgroundColor(Color.WHITE);
        LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        tvParams.leftMargin = 20;
        tvParams.gravity = Gravity.CENTER_VERTICAL;
        mTv.setLayoutParams(tvParams);
        mHeadLayout.addView(mImg);
        mHeadLayout.addView(mTv);
        addView(mHeadLayout);
        addView(mBodyLayout);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mHeadLayout.layout(0, -300, getMeasuredWidth(), 0);
        mBodyLayout.layout(l, t, r, b);

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                mLastPoint = event.getY();
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                float dy = event.getY();
                scrollBy(0, (int) (mLastPoint - dy));
                mLastPoint = dy;
                break;
            }

            case MotionEvent.ACTION_UP: {
                //down
                if (-getScrollY() > 150) {
                    mScroller.startScroll(0, getScrollY(), 0, -getScrollY() - 300);
                    postInvalidate();
                } else {
                    //up
                    if (getScrollY() < 0) {
                        mScroller.startScroll(0, getScrollY(), 0, -getScrollY());
                        postInvalidate();
                    } else {
                        mScroller.startScroll(0, getScrollY(), 0, -getScrollY());
                        postInvalidate();
                    }
                }

                break;
            }

        }
        return true;
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(0, mScroller.getCurrY());
            postInvalidate();
        } else {
            super.computeScroll();
        }
    }
}
