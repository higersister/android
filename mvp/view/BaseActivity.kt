package com.cmy.mvp.view

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import com.cmy.mvp.presenter.BasePresenter

open abstract class BaseActivity<V : IBaseView, P : BasePresenter<V>> : AppCompatActivity() {

    var v: V? = null

    var p: P? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        v = provideV()
        p = provideP()
        p!!.attach(v!!)
    }

    override fun onDestroy() {
        p?.detach()
        super.onDestroy()
    }

    abstract fun provideV(): V
    abstract fun provideP(): P
}