package cn.cmy.custom;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.LineDataSet;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import cn.cmy.custom.dao.UserDao;
import cn.cmy.custom.model.UserModel;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private static final String TAG = "$$$$$$$$$$$$$";
    /*private TextView tv;
    private Button btn_insert;
    private Button btn_query;
    private UserDao userDao;*/
    private List<String> mLists;
    private ViewFlipper mVp;
    private HorizontalBarChart mBarChart1;
    private HorizontalBarChart mBarChart2;
    private CalendarView mCalendarView;
    private List<String> mDates;
    private Button mBtnDate;
    private Button mBtnStartSecond;
    private Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // initView();
     //   init();

    }

   /* private void init() {
      //  test();
        mLists = new ArrayList<>();
        mBarChart1 = findViewById(R.id.horizontal_bar_chart1);
        mBarChart2 = findViewById(R.id.horizontal_bar_chart2);
        mCalendarView = findViewById(R.id.calendar_view);
        mDates = new ArrayList<>();
        mBtnDate = findViewById(R.id.btn_date);
        mBtnStartSecond = findViewById(R.id.btn_start_second);
        *//*mVp = findViewById(R.id.view_flipper);
        mLists.add("aaaaaaaaaaaaa");
        mLists.add("bbbbbbbbbbbbb");
        mLists.add("ccccccccccccc");
        mLists.add("ddddddddddddd");
        mLists.add("eeeeeeeeeeeee");
        for (int i = 0; i < mLists.size(); i++) {
            View view = LayoutInflater.from(this)
                    .inflate(R.layout.viewflipper_item, null, false);
            TextView tv = view.findViewById(R.id.item_tv_content);
            tv.setText(mLists.get(i));
            mVp.addView(view);
        }*//*

        drawBarChart1();
        drawBarChart2();

        mCalendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {

            StringBuilder builder = new StringBuilder();
            builder.append(year);
            builder.append("年");
            builder.append(month + 1);
            builder.append("月");
            builder.append(dayOfMonth);
            builder.append("日");
            if (mLists.contains(builder.toString())) {
                mLists.remove(builder.toString());
            } else {
                mLists.add(builder.toString());
            }
        });
        mBtnDate.setOnClickListener(view -> {
           *//* Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:110"));
            startActivity(intent);*//*
            *//*if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CALL_PHONE},1);
            }*//*

            for (int i = 0; i < mLists.size(); i++) {
                Log.i(TAG, "init: date:" + mLists.get(i));
            }
        });
        mBtnStartSecond.setOnClickListener(view -> {
            startActivity(new Intent(this, SecondActivity.class));
        });


    }*/

    private void test() {
        new Thread(() -> {
            Looper.prepare();
            handler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    Log.i(TAG, "handleMessage: thread:" + Thread.currentThread().getName()+
                   "--msg:"+ msg.obj);
                }
            };
            Looper.loop();
            Log.i(TAG, "test: ");
        }).start();
        new Thread(() -> {
            Message msg = handler.obtainMessage();
            msg.obj = " is " + Thread.currentThread().getName()+"传过来的消息";
            handler.sendMessage(msg);
        }).start();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void drawBarChart1() {
        List<BarEntry> yVals = new ArrayList<>();
        String[] xVals = new String[]{"0", "0"};
        yVals.add(new BarEntry(new float[]{1100, 3190}, 1));
        yVals.add(new BarEntry(new float[]{1100, 1256}, 0));

        BarDataSet dataSet = new BarDataSet(yVals, null);
        //dataSet.setDrawValues(false);
        dataSet.setColors(new int[]{Color.RED, Color.GREEN});
        BarData data = new BarData(xVals, dataSet);
        mBarChart1.setDrawBorders(false);
        mBarChart1.setGridBackgroundColor(Color.RED);
        Legend legend = mBarChart1.getLegend();
        legend.setEnabled(false);
        XAxis xAxis = mBarChart1.getXAxis();
        xAxis.setEnabled(false);
        YAxis leftAxis = mBarChart1.getAxisLeft();
        leftAxis.setEnabled(false);
        //leftAxis.setLabelCount(3, true);
        YAxis rightAxis = mBarChart1.getAxisRight();
        rightAxis.setEnabled(false);
        mBarChart1.setData(data);
        mBarChart1.setDescription("");
        mBarChart1.animateXY(2000, 2000);
        mBarChart1.invalidate();
    }

    private void drawBarChart2() {
        List<BarEntry> yVals = new ArrayList<>();
        String[] xVals = new String[]{"0"};
        yVals.add(new BarEntry(new float[]{1256}, 0));
        BarDataSet dataSet = new BarDataSet(yVals, null);
        //dataSet.setDrawValues(false);

        dataSet.setColors(new int[]{Color.BLUE});
        BarData data = new BarData(xVals, dataSet);
        // mBarChart2.setDrawBorders(false);
        mBarChart2.setGridBackgroundColor(Color.WHITE);
        Legend legend = mBarChart2.getLegend();
        legend.setEnabled(false);
        XAxis xAxis = mBarChart2.getXAxis();
        xAxis.setEnabled(false);
        YAxis leftAxis = mBarChart2.getAxisLeft();
        leftAxis.setEnabled(false);
        //leftAxis.setLabelCount(3, true);
        YAxis rightAxis = mBarChart2.getAxisRight();
        rightAxis.setEnabled(false);
        mBarChart2.setData(data);
        mBarChart2.setDescription("");
        mBarChart2.animateXY(2000, 2000);
        mBarChart2.invalidate();
    }

/*
    private void initView() {
        userDao = new UserDao(this);
        tv = (TextView) findViewById(R.id.tv);
        btn_insert = (Button) findViewById(R.id.btn_insert);
        btn_query = (Button) findViewById(R.id.btn_query);

        btn_insert.setOnClickListener(this);
        btn_query.setOnClickListener(this);
    }*/

    @Override
    public void onClick(View v) {

       /* switch (v.getId()) {
            case R.id.btn_insert:
                for (int i = 0; i < 10; i++) {
                    userDao.insert(new UserModel("name:" + i, "pwd:" + i, "email:" + i));
                }
                break;
            case R.id.btn_query:
                EventBus.getDefault().post(userDao.query());
                break;
        }*/
    }

   /* @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(List<UserModel> list) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            builder.append(list.get(i).toString());
            builder.append("\n");
        }
        tv.setText(builder.toString());
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }*/


}
