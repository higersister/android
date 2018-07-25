package cn.cmy.smartapp.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import cn.cmy.smartapp.persenter.BasePresenter;

//抽象绑定和解绑
//为了能够兼容多个模块，多个activity,采用泛型
public abstract class BaseActivity<V extends BaseView, P extends BasePresenter<V>> extends AppCompatActivity {

    private P p;
    private V v;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (this.p == null) {
            p = createP();
        }
        if (this.v == null) {
            v = createV();
        }
        if (this.p != null && this.v != null) {
            p.attachView(v);
        }
    }

    public P getP() {
        return p;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (this.p != null && this.v != null) {
            p.detachView();
        }

    }

    public abstract P createP();

    public abstract V createV();
}
