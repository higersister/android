package cn.cmy.popuwindowdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button mBtnPopupBottom;

    private Button mBtnPopupLeft;

    private Button mBtnPopupRight;

    private Button mBtnPopupTop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

    }

    private void init() {
        View view = getLayoutInflater().inflate(R.layout.popup_layout1, null);
        View view1 = getLayoutInflater().inflate(R.layout.popup_layout2, null);
        view1.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int mShowPopupWindowWidth = -view1.getMeasuredWidth();
        int mShowPopupWindowHeight = -view1.getMeasuredHeight();
       /* int[] location =new int[2];
        view1.getLocationOnScreen(location);*/
        mBtnPopupBottom = findViewById(R.id.btn_popu1);
        mBtnPopupBottom.setOnClickListener(v -> {
            //  mPopup.showAsDropDown(mBtnPopu);
            CustomPopWindow customPopWindow = new CustomPopWindow.PopupWindowBuilder(this).
                    setView(view).
                    setAnimationStyle(R.style.MyPopupWindow_anim_style).
                    create().
                    showAtLocation(mBtnPopupBottom, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        });
        mBtnPopupLeft = findViewById(R.id.btn_popu2);
        mBtnPopupLeft.setOnClickListener(v -> {

            CustomPopWindow customPopWindow = new CustomPopWindow.PopupWindowBuilder(this).
                    setView(view1).
                    size(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT).
                    setAnimationStyle(R.style.MyPopupWindow_anim_style).
                    create().
                    showAsDropDown(mBtnPopupLeft, mShowPopupWindowWidth, mShowPopupWindowHeight);

        });

        mBtnPopupRight = findViewById(R.id.btn_popu3);
        mBtnPopupRight.setOnClickListener(v -> {
            Log.i("************", "OnClickListener: mShowPopupWindowWidth:" + -mShowPopupWindowWidth + ",mShowPopupWindowHeight:" + mShowPopupWindowHeight);
            CustomPopWindow customPopWindow = new CustomPopWindow.PopupWindowBuilder(this).
                    setView(view1).
                    size(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT).
                    setAnimationStyle(R.style.MyPopupWindow_anim_style).
                    create().
                    showAsDropDown(mBtnPopupRight, -mShowPopupWindowWidth + mBtnPopupRight.getWidth(), mShowPopupWindowHeight);
            //   showAtLocation(mBtnPopupRight,Gravity.NO_GRAVITY,location[0] +mBtnPopupRight.getWidth() , location[1]);
            // showAsDropDown(mBtnPopupRight, -mShowPopupWindowWidth, -mShowPopupWindowHeight);
            /*  Log.i("************", "init: location[0]:" + location[0] + ",location[1]:" + location[1]);
            Log.i("************", "init: location[0] + mBtnPopupRight.getWidth:" + location[0] + mBtnPopupRight.getWidth() +  ",location[1]:" + location[1]);
*/
        });

        mBtnPopupTop = findViewById(R.id.btn_popu4);
        mBtnPopupTop.setOnClickListener(V -> {
            int[] location = new int[2];
            mBtnPopupTop.getLocationOnScreen(location);
            Log.i("**********", "setOnClickListener: locatioin[0]:" + location[0] + ",location[1]:" + location[1]);
            CustomPopWindow customPopWindow = new CustomPopWindow.PopupWindowBuilder(this).
                    setView(view1).
                    size(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT).
                    setAnimationStyle(R.style.MyPopupWindow_anim_style).
                    create().
                    showAtLocation(mBtnPopupTop, Gravity.NO_GRAVITY, (location[0] + mBtnPopupTop.getWidth() / 2) - mShowPopupWindowWidth / -2, location[1] - -mShowPopupWindowHeight);

        });
    }
}
