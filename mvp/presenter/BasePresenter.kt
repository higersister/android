package com.cmy.mvp.presenter

import com.cmy.mvp.view.IBaseView

open abstract class BasePresenter<V : IBaseView> {

    var v:V? = null

    fun attach(v:V){
        this.v = v
    }

    fun detach(){
        this.v = null
    }

}