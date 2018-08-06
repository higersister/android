package cn.cmy.rxjavaretrofit;

import android.util.Log;

public class Translation {

    private String status;
    private content content;
    private static class content{
        private String from;
        private String to;
        private String vendor;
        private String out;
        private int errNo;
    }

    public void show() {
        Log.i(OprationsymbolActivity.TAG, "show: status:" + status);
        Log.i(OprationsymbolActivity.TAG, "show: from:" + content.from);
        Log.i(OprationsymbolActivity.TAG, "show: to:" + content.to);
        Log.i(OprationsymbolActivity.TAG, "show: vendor:" + content.vendor);
        Log.i(OprationsymbolActivity.TAG, "show: out:" + content.out);
        Log.i(OprationsymbolActivity.TAG, "show: errNo:" + content.errNo);

    }

}
