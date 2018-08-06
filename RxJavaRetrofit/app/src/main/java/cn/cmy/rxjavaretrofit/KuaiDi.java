package cn.cmy.rxjavaretrofit;

import android.util.Log;

import java.util.List;

import static cn.cmy.rxjavaretrofit.OprationsymbolActivity.TAG;

public class KuaiDi {

    private String message;

    private String nu;

    private String ischeck;

    private String condition;

    private String com;

    private String status;

    private String state;

    private List<data> data;


    public List<KuaiDi.data> getData() {
        return data;
    }

    public static class data {

        public String time;

        public String ftime;

        public String context;

        public Object location;


    }

    public String getKuaiDiInfo() {
        return "message:" + message + ",nu:" + nu + ",ischeck:" + ischeck
                + ",condition:" + condition + ",com:" + com + ",status:"
                + status + ",state:" + state;
    }

    public void show() {
        Log.i(TAG, "show: message:" + message);
        Log.i(TAG, "show: nu:" + nu);
        Log.i(TAG, "show: ischeck:" + ischeck);
        Log.i(TAG, "show: condition:" + condition);
        Log.i(TAG, "show: com:" + com);
        Log.i(TAG, "show: status:" + status);
        Log.i(TAG, "show: state:" + state);
    }


}
