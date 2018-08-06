package cn.cmy.rxjavaretrofit;

import android.util.Log;

import java.util.List;

import static cn.cmy.rxjavaretrofit.OprationsymbolActivity.TAG;

public class Movie {

    private int count;

    private int start;

    private int total;

    public void show(){
        Log.i(TAG, "show: count:" + count);
        Log.i(TAG, "show: start:" + start);
        Log.i(TAG, "show: total:" + total);
    }

}
