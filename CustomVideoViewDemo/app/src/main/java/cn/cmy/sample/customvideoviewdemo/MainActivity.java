package cn.cmy.sample.customvideoviewdemo;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private static final String MP4_URL = "http://download.yxybb.com/bbvideo/web/d1/d13/d11/d1/f11-web.mp4";
    private static final String TAG = "**********************";
    private static final int PROGRESS_MSG = 0x00;
    private static final int SHOWAUDIO_LAYOUT = 0x01;
    private static final int HIDEAUDIO_LAYOUT = 0x02;
    private static final int SHOWLIGHT_LAYOUT = 0x03;
    private static final int HIDELIGHT_LAYOUT = 0x04;

    private Button mBtnStartAndPause;

    private Button mBtnFullScreen;

    private ImageView mImgBack;

    private TextView mTvTitle;

    private CustomVideoView mCustomVideoView;

    private View mTopView, mBottomView;

    private SeekBar mSeekBar;

    private TextView mTvCurrentTimes, mTvTotalTimes;

    private int mCurrentPosition = 0;

    private LoadingView mLoadingView;

    private View mBlackView;

    private ImageView mImgError;

    private TextView mTVErrorText;

    private TextView mTvOr;

    private boolean isPause = true, isFullScreen, fromUser = true;

    private ImageView mImgSound;

    private SeekBar mSeekBarLight, mSeekBarSound;

    private LinearLayout mLinearLayoutSound, mLinearLayoutLight;

    private long mLastTime;

    private float mLastX;

    private float mLastY;

    private int screenWidth, screenHeight;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case PROGRESS_MSG:
                    if (!mCustomVideoView.isPlaying()) {
                        if (isPause) {
                            mLoadingView.setVisibility(View.VISIBLE);
                            mBlackView.setVisibility(View.VISIBLE);
                        } else {
                            mLoadingView.setVisibility(View.GONE);
                            mBlackView.setVisibility(View.GONE);
                        }
                    } else {
                        mLoadingView.setVisibility(View.GONE);
                        mBlackView.setVisibility(View.GONE);
                    }
                    mSeekBar.setProgress(mCustomVideoView.getCurrentPosition());
                    mTvCurrentTimes.setText(updateCurrentTimes(mCustomVideoView.getCurrentPosition()));
                    mHandler.sendEmptyMessageDelayed(PROGRESS_MSG, 1000);
                    break;
                case SHOWAUDIO_LAYOUT:
                    mLinearLayoutSound.setVisibility(View.VISIBLE);
                    mSeekBarSound.setMax(AudioUtil.getStreamMaxVolume(
                            MainActivity.this.getApplicationContext()));
                    mSeekBarSound.setProgress(AudioUtil.getStreamVolume(
                            MainActivity.this.getApplicationContext()));
                    break;
                case HIDEAUDIO_LAYOUT:
                    mLinearLayoutSound.setVisibility(View.GONE);
                    break;
                case SHOWLIGHT_LAYOUT:
                    mLinearLayoutLight.setVisibility(View.VISIBLE);
                    Log.i(TAG, "handleMessage: Brightness.getSystemBrightness(getApplicationContext())" + Brightness.getSystemBrightness(getApplicationContext()));
                    mSeekBarLight.setProgress(Brightness.getSystemBrightness(getApplicationContext()));
                    break;
                case HIDELIGHT_LAYOUT:
                    mLinearLayoutLight.setVisibility(View.GONE);
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Brightness.isAutoBrightness(getApplicationContext())) {

            Brightness.closeAutoBrightness(getApplicationContext());
        }

        //申请android.permission.WRITE_SETTINGS权限的方式
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //如果当前平台版本大于23平台
            if (!Settings.System.canWrite(this)) {
                //如果没有修改系统的权限这请求修改系统的权限
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityForResult(intent, 0);
            } else {
                Brightness.setScreenMode(getApplicationContext(),
                        Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
                init();

            }
        }

    }

    private void init() {

        mBtnStartAndPause = findViewById(R.id.btn_start_pause);
        mBtnFullScreen = findViewById(R.id.btn_fullScreen);
        mCustomVideoView = findViewById(R.id.custom_videoView);
        mImgBack = findViewById(R.id.img_back);
        mBtnStartAndPause = findViewById(R.id.btn_start_pause);
        mBtnFullScreen = findViewById(R.id.btn_fullScreen);
        mTvTitle = findViewById(R.id.tv_title);
        mTopView = findViewById(R.id.top_view);
        mBottomView = findViewById(R.id.bottom_view);
        mSeekBar = findViewById(R.id.seek_bar);
        mTvCurrentTimes = findViewById(R.id.tv_current_time);
        mTvTotalTimes = findViewById(R.id.tv_total_time);
        mLoadingView = findViewById(R.id.load_view);
        mBlackView = findViewById(R.id.bg_black_view);
        mImgError = findViewById(R.id.img_error);
        mTVErrorText = findViewById(R.id.error_text);
        mTvOr = findViewById(R.id.tv_or);
        mImgSound = findViewById(R.id.img_sound);
        mSeekBarSound = findViewById(R.id.seek_bar_sound);
        mSeekBarLight = findViewById(R.id.seek_bar_light);
        mLinearLayoutLight = findViewById(R.id.linear_light);
        mLinearLayoutSound = findViewById(R.id.linear_sound);
        start();
        onLisenter();
    }

    private String updateCurrentTimes(int millisecond) {

        return TimerUtil.updateTimes(millisecond);

    }

    private String updateTotalTimes(int millisecond) {

        return TimerUtil.updateTimes(millisecond);

    }

    private void onLisenter() {

        mBtnStartAndPause.setOnClickListener(v -> {

            if (mCustomVideoView.isPlaying()) {
                mBtnStartAndPause.setBackgroundResource(R.drawable.ic_exo_start);
                mCustomVideoView.pause();
                mHandler.removeMessages(PROGRESS_MSG);
            } else {
                mBtnStartAndPause.setBackgroundResource(R.drawable.ic_exo_pause);
                mCustomVideoView.start();
                mHandler.sendEmptyMessageDelayed(PROGRESS_MSG, 1000);
            }
            isPause = !isPause;

        });

        mImgBack.setOnClickListener(view -> {
            exitFullScreen();

        });

        mCustomVideoView.setOnPreparedListener(mp -> {

            //  Log.i(TAG, "onLisenter: duration:" + mCustomVideoView.getDuration());
            mTvTotalTimes.setText(updateTotalTimes(mCustomVideoView.getDuration()));
            mSeekBar.setMax(mCustomVideoView.getDuration());
            //     Log.i(TAG, "start: duration:" + mCustomVideoView.getDuration());
            mHandler.sendEmptyMessageDelayed(PROGRESS_MSG, 1000);
        });
        mCustomVideoView.setOnCompletionListener(mp -> {
            mBtnStartAndPause.setBackgroundResource(R.drawable.ic_replay_);
            mHandler.removeMessages(PROGRESS_MSG);
        });
        mCustomVideoView.setOnErrorListener((mp, what, ertra) -> {
            mHandler.removeMessages(PROGRESS_MSG);
            mImgError.setVisibility(View.VISIBLE);
            mTVErrorText.setVisibility(View.VISIBLE);
            mLoadingView.setVisibility(View.GONE);
            mBlackView.setVisibility(View.GONE);
            mTvTitle.setVisibility(View.GONE);
            mTvCurrentTimes.setVisibility(View.GONE);
            mTvTotalTimes.setVisibility(View.GONE);
            mSeekBar.setVisibility(View.GONE);
            mBtnStartAndPause.setVisibility(View.GONE);
            mBtnFullScreen.setVisibility(View.GONE);
            mTvOr.setVisibility(View.GONE);
            mImgBack.setVisibility(View.GONE);
            return true;
        });
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    Log.i(TAG, "onProgressChanged: progress:" + progress);
                    mCustomVideoView.seekTo(progress);
                    mHandler.sendEmptyMessageDelayed(PROGRESS_MSG, 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mBtnFullScreen.setOnClickListener(view -> {
            if (isFullScreen) {

                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            } else {

                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }

            isFullScreen = !isFullScreen;
        });

        mCustomVideoView.setOnTouchListener((view, event) -> {
            if (isFullScreen && fromUser) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mLastX = event.getX();
                        mLastY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        //   Log.i(TAG, "onLisenter: up:mLastY - event.getY:" + (mLastY - event.getY()));
                        if (Math.abs(mLastY - event.getY()) > 0) {
                            if (screenWidth / 2 > mLastX) {
                                setHidelightLayout();
                            } else {
                                setHideAudioLayout();
                            }
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float currentX = event.getX();
                        float currentY = event.getY();

                        if (mLastY - currentY > 0) {
                            if (screenWidth / 2 > mLastX) {
                                Brightness.turnUpSystemBrightness(this, (int) ((mLastY - currentY) / 4));
                                // Brightness.saveScreenBrightness(getApplicationContext(), (int) ((mLastY - currentY) / 2));
                                setShowlightLayout();
                            } else {
                                audioUp(((mLastY - currentY) / screenWidth / 3));
                                setShowAudioLayout();
                            }
                        } else {
                            if (screenWidth / 2 > mLastX) {
                                Brightness.turnDownSystemBrightness(this, Math.abs((int) ((mLastY - currentY) / 4)));
                                setShowlightLayout();

                            } else {
                                audioDown(Math.abs(((mLastY - currentY) / screenWidth / 6)));
                                setShowAudioLayout();
                            }

                        }

                        break;
                }

            }
            return true;
        });


    }

    private void audioDown(float precent) {
        if (precent < 0) {
            return;
        }
        AudioUtil.turnDown(getApplicationContext(), precent);

    }

    /**
     * 全屏模式下，右半边屏幕上下滑动改变音量大小
     *
     * @param precent
     */
    private void audioUp(float precent) {
        if (precent < 0) {
            return;
        }
        AudioUtil.turnUp(getApplicationContext(), precent);

    }


    private void setHideAudioLayout() {
        mHandler.sendEmptyMessageDelayed(HIDEAUDIO_LAYOUT, 2500);
    }

    private void setShowAudioLayout() {
        mHandler.sendEmptyMessage(SHOWAUDIO_LAYOUT);
    }

    private void setShowlightLayout() {
        mHandler.sendEmptyMessage(SHOWLIGHT_LAYOUT);
    }

    private void setHidelightLayout() {
        mHandler.sendEmptyMessageDelayed(HIDELIGHT_LAYOUT, 2500);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            fromUser = false;
            mBtnFullScreen.setBackgroundResource(R.drawable.ic_fullscreen_white);
            if (getSupportActionBar() != null) {
                getSupportActionBar().show();
            }
            mTopView.setVisibility(View.GONE);
            mBottomView.setVisibility(View.GONE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, dip2px(250f));
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            mCustomVideoView.setLayoutParams(params);
        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            screenWidth = getWindowManager().getDefaultDisplay().getWidth();
            screenHeight = getWindowManager().getDefaultDisplay().getHeight();
            fromUser = true;
            mBtnFullScreen.setBackgroundResource(R.drawable.ic_fullscreen_exit_white);
            if (getSupportActionBar() != null) {
                getSupportActionBar().hide();
            }
            mTopView.setVisibility(View.VISIBLE);
            mBottomView.setVisibility(View.VISIBLE);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            mCustomVideoView.setLayoutParams(new RelativeLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT));
        }

    }


    @Override
    public void onBackPressed() {
        exitFullScreen();
        if (System.currentTimeMillis() - mLastTime < 2000) {
            super.onBackPressed();
        } else {
            Toast.makeText(getApplicationContext(),
                    "After pressing an exit...", Toast.LENGTH_SHORT).
                    show();
        }
        mLastTime = System.currentTimeMillis();

    }

    private void exitFullScreen() {
        if (isFullScreen) {

            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            isFullScreen = !isFullScreen;
        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dip2px(float dpValue) {
        final float scale = getApplicationContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);

    }


    private void start() {
        mCustomVideoView.setVideoURI(Uri.parse(MP4_URL));
        mCustomVideoView.start();


    }


    @Override
    protected void onResume() {
        super.onResume();
        // Log.i(TAG, "onResume: -----------------");
        if (mCustomVideoView == null) {
            return;
        }
        if (isPause) {
            mCustomVideoView.start();
        }
        mCustomVideoView.seekTo(mCurrentPosition);
        //mCustomVideoView.resume();
        mHandler.sendEmptyMessage(PROGRESS_MSG);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //   Log.i(TAG, "onPause: ---------------");
        if (mCustomVideoView == null) {
            return;
        }
        mCurrentPosition = mCustomVideoView.getCurrentPosition();
        mHandler.removeMessages(PROGRESS_MSG);
        mCustomVideoView.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
