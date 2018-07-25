package cn.cmy.smartapp.model;

public interface HttpCallBack {

    //成功
    void onSuccess(String data);

    //失败
    void onFailure(Exception e);

}
