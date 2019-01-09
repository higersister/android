package cn.cmy.custom.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import cn.cmy.custom.R;

public class FloatingMenu extends ViewGroup {

    private final static int OPEN = 1;
    private final static int CLOSE = 2;
    private final int RADIUS = 180;
    private ImageView mMenu;
    private int mState = 2;


    public FloatingMenu(Context context) {
        this(context, null);
    }

    public FloatingMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        mMenu = new ImageView(context);
        mMenu.setImageResource(R.drawable.ic_add_circle_outline_black_24dp1);
        addView(mMenu);
        initListener();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        setMeasuredDimension(widthMode == MeasureSpec.EXACTLY ? width : 300,
                heightMode == MeasureSpec.EXACTLY ? height : 300);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mMenu.layout(getMeasuredWidth() - mMenu.getMeasuredWidth() - 50,
                getMeasuredHeight() - mMenu.getMeasuredHeight() - 50,
                getMeasuredWidth() - 50,
                getMeasuredHeight() - 50);
        int size = getChildCount();
        int hd = 90 / size - 1;
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        for (int i = 1; i < size; i++) {
            View view = getChildAt(i);
            int x = (int) (RADIUS * Math.cos(i * hd * Math.PI / 180));
            int y = (int) (RADIUS * Math.sin(i * hd * Math.PI / 180));
            view.layout(width - x - view.getMeasuredWidth(),
                    height - y - view.getMeasuredHeight(),
                    width - x,
                    height - y);

        }


    }

    private void initListener() {


    }
}
