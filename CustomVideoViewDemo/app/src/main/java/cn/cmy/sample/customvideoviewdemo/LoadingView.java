package cn.cmy.sample.customvideoviewdemo;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;

public class LoadingView extends LinearLayout {

    private String[] colors = {"#FFFF0000", "#FFFFD000", "#FFD500F9",
            "#FF00FF00", "#FF0000FF"};

    public LoadingView(Context context) {
        super(context);
        load(context);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        load(context);
    }

    private void load(Context context) {
        // 添加5条竖状矩形（波形）
        for (int i = 0; i < 5; i++) {
             View view = new View(context);
            LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams(dip2px(7),
                            dip2px(20));
            params.leftMargin = dip2px(2);
            view.setBackgroundColor(Color.parseColor(colors[i]));
            view.setLayoutParams(params);
            addView(view);
            //纵向旋转
            ObjectAnimator rotation = ObjectAnimator.ofFloat(view,
                    "rotationX", 0f, 360f);
            //设置重复次数
            rotation.setRepeatCount(ObjectAnimator.INFINITE);
            //设置重复模式
            rotation.setRepeatMode(ObjectAnimator.RESTART);
            // 避免循环一次就停顿一下的问题
            rotation.setInterpolator(new LinearInterpolator());
            rotation.setStartDelay(100 * i);
            //纵向伸缩
            ObjectAnimator scale = ObjectAnimator.ofFloat(view, "scaleY",
                    1.5f, 1.5f, 1.5f);
            scale.setRepeatMode(ObjectAnimator.RESTART);
            scale.setRepeatCount(ObjectAnimator.INFINITE);
            scale.setStartDelay(100 * i);

            //组合动画
            AnimatorSet set = new AnimatorSet();
            set.play(rotation).with(scale);
            set.setDuration(2500);
            set.start();

        }


    }

    private int dip2px(int dpValue) {
        return (int) (dpValue *
                getContext().getResources().getDisplayMetrics().density + 0.5f);
    }
}
