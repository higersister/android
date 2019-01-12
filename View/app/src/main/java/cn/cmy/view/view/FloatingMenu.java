package cn.cmy.view.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.BounceInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import cn.cmy.view.R;

import static cn.cmy.view.view.StickView.TAG;

public class FloatingMenu extends ViewGroup {

    private static final int RADIUS = 250;
    private volatile boolean mStatus = true;//false:open ,true:close
    private ImageView[] mImg;
    private int mWidth;
    private int mHeight;
    private OnSubMenuClickListener mSubMenuListener;

    public FloatingMenu(Context context) {
        this(context, null);
    }

    public FloatingMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
    }

    public void setSubMenuListener(OnSubMenuClickListener mSubMenuListener) {
        this.mSubMenuListener = mSubMenuListener;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        getChildAt(0).layout(l, t, r, b);
        for (int i = 0; i < mImg.length; i++) {
            if (i == 0) {
                mImg[i].layout(r - mImg[i].getMeasuredWidth() - 10,
                        b - mImg[i].getMeasuredHeight() - 20,
                        r - 10,
                        b - 20);
            } else {
                mImg[i].layout(r - mImg[i].getMeasuredWidth() - 10,
                        b - mImg[i].getMeasuredHeight() - 20,
                        r - 10,
                        b - 20);
                mImg[i].setVisibility(INVISIBLE);
            }


        }
        initAnim();


    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.i(TAG, "onMeasure: ");
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
    protected void onFinishInflate() {
        super.onFinishInflate();
        Log.i(TAG, "onFinishInflate: ");
        if (!(getChildAt(0) instanceof ViewGroup)) {
            throw new IllegalArgumentException("child at 0 must be view group.");
        }
        mImg = new ImageView[getChildCount() - 1];
        for (int i = 0; i < mImg.length; i++) {
            View v = getChildAt(i + 1);
            if (v instanceof ImageView) {
                mImg[i] = (ImageView) v;
            }
        }
    }

    private void initAnim() {
        mImg[0].setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: status:" + mStatus);
                mStatus = !mStatus;
                ObjectAnimator rAnim = mStatus ?
                        ObjectAnimator.ofFloat(v, "rotation", 45, 0) :
                        ObjectAnimator.ofFloat(v, "rotation", 0, 45);
                rAnim.setInterpolator(new BounceInterpolator());
                rAnim.setDuration(500);
                rAnim.start();
                for (int i = 1; i < mImg.length; i++) {
                    final int finalI = i;
                    float degree = 90 / (mImg.length - 1) * i;
                    int dx = (int) (RADIUS * Math.cos(degree * Math.PI / 180));
                    int dy = (int) (RADIUS * Math.sin(degree * Math.PI / 180));
                    AnimatorSet sSet = new AnimatorSet();
                    sSet.setInterpolator(new BounceInterpolator());
                    ObjectAnimator tx;
                    ObjectAnimator ty;
                    ObjectAnimator alpha;
                    if (mStatus) {
                        tx = ObjectAnimator.ofFloat(mImg[i],
                                "TranslationX", -dx, 0);
                        ty = ObjectAnimator.ofFloat(mImg[i],
                                "TranslationY", -dy, 0);
                        alpha = ObjectAnimator.ofFloat(mImg[i],
                                "alpha", 1, 0);
                        sSet.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                mImg[finalI].setVisibility(GONE);
                            }
                        });

                    } else {
                        mImg[finalI].setVisibility(VISIBLE);
                        tx = ObjectAnimator.ofFloat(mImg[i],
                                "TranslationX", 0, -dx);
                        ty = ObjectAnimator.ofFloat(mImg[i],
                                "TranslationY", 0, -dy);
                        alpha = ObjectAnimator.ofFloat(mImg[i],
                                "alpha", 0, 1);
                    }
                    sSet.setDuration(500);
                    sSet.play(tx).with(ty).with(alpha);
                    sSet.start();
                }

            }
        });
        for (int i = 1; i < mImg.length; i++) {
            final int finalI = i;
            mImg[i].setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG, "onClick: sub");
                    if (null != mSubMenuListener) {
                        mSubMenuListener.onSubMenuClick(v, finalI);
                    }
                }
            });
        }

    }


    public interface OnSubMenuClickListener {

        void onSubMenuClick(View view, int position);

    }


}
