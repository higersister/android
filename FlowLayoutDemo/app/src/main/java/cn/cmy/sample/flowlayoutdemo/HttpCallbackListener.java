package cn.cmy.sample.flowlayoutdemo;

public interface HttpCallbackListener {

    void onFinish(String response);

    void onError(Exception e);

}
