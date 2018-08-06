package cn.cmy.rxjavaretrofit;

public interface DownLoadListener {

    void onStart();

    void onProgress(int progress);

    void onFinish();

    void onFailue(String failueInfo);

}
