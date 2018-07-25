package cn.cmy.smartapp.persenter;

import cn.cmy.smartapp.view.BaseView;

public abstract class BasePresenter<V extends BaseView> {

    private V v;

    public void attachView(V v) {
        this.v = v;
    }

    public void detachView() {
        //终止请求
        this.v = null;
    }

    public V getV(){
        return v;
    }
}
